package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ActiveParkingNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageClosedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.VehicleAlreadyParkedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.utils.PricingCalculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class EventTypeService {


    private static final String GARAGE_FULL_EXCEPTION_MESSAGE = "Have nothing available places to parking in this garage at the moment, please try again.";
    private static final String VEHICLE_ALREADY_PARKED_EXCEPTION_MESSAGE = "The vehicle with license plate %s already has an active parking record.";
    private static final String GARAGE_CLOSED_EXCEPTION_MESSAGE = "This garage is already closed.";
    private static final ZoneId GARAGE_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final String ACTIVE_PARKING_NOT_FOUND_EXCEPTION_MESSAGE = "No active parking event found for license plate: %s. A vehicle must register entry before updating the parked location.";

    private final ParkingEventRepository parkingEventRepository;
    private final GarageService garageService;
    private final SpotService spotService;
    private final ParkingEventService parkingEventService;
    private final EventTypeValidationService eventValidationService;

    @Autowired
    public EventTypeService(ParkingEventRepository parkingEventRepository, GarageService garageService,
                            SpotService spotService, ParkingEventService parkingEventService
            , EventTypeValidationService eventValidationService) {

        this.parkingEventRepository = parkingEventRepository;
        this.garageService = garageService;
        this.spotService = spotService;
        this.parkingEventService = parkingEventService;
        this.eventValidationService = eventValidationService;

    }


    @Transactional
    public ParkingEventModel handleEntryEvent(String sector, String licensePlate, OffsetDateTime entryTime) {


        if (parkingEventService.isVehicleCurrentlyParked(licensePlate)) {

            throw new VehicleAlreadyParkedException(String.format(VEHICLE_ALREADY_PARKED_EXCEPTION_MESSAGE, licensePlate));

        }


        SpotModel spotModel = spotService.findFistAvailableSpot(sector);

        GarageModel garageModel = spotModel.getGarageModel();

        if (garageModel.getCurrentOccupancy() >= garageModel.getMaxCapacity()) {

            throw new GarageFullException(GARAGE_FULL_EXCEPTION_MESSAGE);

        }

        if (entryTime == null) {
            entryTime = OffsetDateTime.now(GARAGE_ZONE);
        }

        eventValidationService.validateEntryDay(entryTime, licensePlate);


        LocalTime entryLocalTime = entryTime.atZoneSameInstant(GARAGE_ZONE).toLocalTime();

        LocalTime openHour = garageModel.getOpenHour();

        LocalTime closeHour = garageModel.getCloseHour();


        if (!garageService.isGarageOpen(entryLocalTime, openHour, closeHour)) {

            throw new GarageClosedException(GARAGE_CLOSED_EXCEPTION_MESSAGE);

        }

        double multiplier = PricingCalculator.calculateDynamicMultiplier(garageModel);


        spotModel.setOccupied(true);
        spotModel.setOccupiedByLicensePlate(licensePlate);

        SpotModel updatedSpot = spotService.saveSpot(spotModel);


        garageModel.setCurrentOccupancy(garageModel.getCurrentOccupancy() + 1);

        GarageModel updatedGarageModel = garageService.saveGarage(garageModel);


        ParkingEventModel parkingEventModel = new ParkingEventModel();

        parkingEventModel.setLicensePlate(licensePlate);
        parkingEventModel.setEntryTime(entryTime);
        parkingEventModel.setSpotModel(updatedSpot);
        parkingEventModel.setGarageModel(updatedGarageModel);
        parkingEventModel.setDynamicPriceMultiplier(multiplier);


        return parkingEventService.saveParkingEvent(parkingEventModel);
    }


    @Transactional
    public ParkingEventModel handleParkedEvent(String licensePlate, Double lat, Double lng) {

        Optional<ParkingEventModel> activeEventOptional = parkingEventRepository
                .findByLicensePlateAndExitTimeIsNull(licensePlate);

        if (activeEventOptional.isPresent()) {


            ParkingEventModel parkingEventModel = activeEventOptional.get();

            parkingEventModel.setLat(lat);
            parkingEventModel.setLgn(lng);

            return parkingEventService.saveParkingEvent(parkingEventModel);

        } else {

            throw new ActiveParkingNotFoundException(String.format(ACTIVE_PARKING_NOT_FOUND_EXCEPTION_MESSAGE, licensePlate));


        }
    }

    @Transactional
    public ParkingEventModel handleExitEvent(String licensePlate, OffsetDateTime exitTime) {

        ParkingEventModel parkingEventModel = parkingEventService.findParkingEventByLicensePlate(licensePlate);

        OffsetDateTime entryTime = parkingEventModel.getEntryTime();

        eventValidationService.validateExitTime(entryTime, exitTime, licensePlate);

        long totalMinutes = ChronoUnit.MINUTES.between(entryTime, exitTime);
        double finalAmount = 0D;

        if (totalMinutes > 30) {
            double hours = totalMinutes / 60.0;
            long chargeableHours = (long) Math.ceil(hours);

            Double basePrice = parkingEventModel.getGarageModel().getBasePrice();
            Double multiplier = parkingEventModel.getDynamicPriceMultiplier();
            double dynamicPricePerHour = basePrice * multiplier;
            finalAmount = chargeableHours * dynamicPricePerHour;
        }


        parkingEventModel.setExitTime(exitTime);
        parkingEventModel.setFinalAmount(finalAmount);


        ParkingEventModel updateParkingModel = parkingEventService.saveParkingEvent(parkingEventModel);

        SpotModel spotModel = parkingEventModel.getSpotModel();
        spotModel.setOccupied(false);
        spotModel.setOccupiedByLicensePlate(null);
        spotService.saveSpot(spotModel);

        GarageModel sector = parkingEventModel.getGarageModel();
        sector.setCurrentOccupancy(sector.getCurrentOccupancy() - 1);

        garageService.saveGarage(sector);


        return updateParkingModel;


    }

}