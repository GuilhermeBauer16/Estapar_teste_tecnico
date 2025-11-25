package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageClosedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
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
public class ParkingService {
    private static final String GARAGE_FULL_EXCEPTION_MESSAGE = "Have nothing available places to parking in this garage at the moment, please try again.";
    private static final String GARAGE_CLOSED_EXCEPTION_MESSAGE = "The garage is already closed.";
    private static final ZoneId GARAGE_ZONE = ZoneId.of("America/Sao_Paulo");
    private final SpotRepository spotRepository;
    private final GarageRepository garageRepository;
    private final ParkingEventRepository parkingEventRepository;

    @Autowired
    public ParkingService(SpotRepository spotRepository, GarageRepository garageRepository, ParkingEventRepository parkingEventRepository) {
        this.spotRepository = spotRepository;
        this.garageRepository = garageRepository;
        this.parkingEventRepository = parkingEventRepository;
    }

    public ParkingEventModel handleEntryEvent(String licensePlate, OffsetDateTime entryTime) {

        Optional<SpotModel> availableSpot = spotRepository.findFirstByIsOccupied(false);


        if (availableSpot.isEmpty()) {
            throw new GarageFullException(GARAGE_FULL_EXCEPTION_MESSAGE);
        }

        SpotModel spotModel = availableSpot.get();
        GarageModel sector = spotModel.getGarageModel();

        if (sector.getCurrentOccupancy() >= sector.getMaxCapacity()) {
            throw new GarageClosedException(GARAGE_FULL_EXCEPTION_MESSAGE);
        }

        LocalTime entryLocalTime = entryTime.atZoneSameInstant(GARAGE_ZONE).toLocalTime();
        LocalTime openHour = sector.getOpenHour();
        LocalTime closeHour = sector.getCloseHour();

        if (!isGarageOpen(entryLocalTime, openHour, closeHour)) {
            throw new GarageFullException(GARAGE_CLOSED_EXCEPTION_MESSAGE);
        }


        double multiplier = PricingCalculator.calculateDynamicMultiplier(sector);

        spotModel.setOccupied(true);
        spotModel.setOccupiedByLicensePlate(licensePlate);
        spotRepository.save(spotModel);

        sector.setCurrentOccupancy(sector.getCurrentOccupancy() + 1);
        garageRepository.save(sector);

        ParkingEventModel parkingEventModel = new ParkingEventModel();
        parkingEventModel.setLicensePlate(licensePlate);
        parkingEventModel.setEntryTime(entryTime);
        parkingEventModel.setSpotModel(spotModel);
        parkingEventModel.setGarageModel(sector);
        parkingEventModel.setDynamicPriceMultiplier(multiplier);
        return parkingEventRepository.save(parkingEventModel);

    }

    @Transactional
    public ParkingEventModel handleExitEvent(String licensePlate, OffsetDateTime exitTime) {

        ParkingEventModel parkingEventModel = parkingEventRepository
                .findByLicensePlateAndExitTimeIsNull(licensePlate)
                .orElseThrow(() -> new RuntimeException("I need to implement that!!!"));

        OffsetDateTime entryTime = parkingEventModel.getEntryTime();
        if (entryTime == null) {
            throw new IllegalStateException("Active parking record for license plate " + licensePlate + " has a null entry time. Data inconsistency.");
        }
        long totalMinutes = ChronoUnit.MINUTES.between(entryTime, exitTime);
        double finalAmount = 0D;

        if (totalMinutes > 30) {
            double hours = (double) totalMinutes / 60.0;
            long changeableHours = (long) Math.ceil(hours);

            Double basePrice = parkingEventModel.getGarageModel().getBasePrice();
            Double multiplier = parkingEventModel.getDynamicPriceMultiplier();
            double dynamicPricePerHour = basePrice * multiplier;
            finalAmount = changeableHours * dynamicPricePerHour;
        }


        parkingEventModel.setExitTime(exitTime);
        parkingEventModel.setFinalAmount(finalAmount);
        parkingEventRepository.save(parkingEventModel);

        SpotModel spotModel = parkingEventModel.getSpotModel();
        spotModel.setOccupied(false);
        spotModel.setOccupiedByLicensePlate(null);
        spotRepository.save(spotModel);

        GarageModel sector = parkingEventModel.getGarageModel();
        sector.setCurrentOccupancy(sector.getCurrentOccupancy() - 1);
        garageRepository.save(sector);

        return parkingEventModel;


    }

    private boolean isGarageOpen(LocalTime entryTime, LocalTime openHour, LocalTime closeHour) {

        if (openHour.isBefore(closeHour) || openHour.equals(closeHour)) {

            return !entryTime.isBefore(openHour) && !entryTime.isAfter(closeHour);

        } else {

            return !entryTime.isBefore(openHour) || !entryTime.isAfter(closeHour);
        }
    }
}
