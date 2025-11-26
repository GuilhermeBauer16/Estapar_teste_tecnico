package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.GarageConfigResponse;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidSimulatorException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.SimulatorConfigurationServiceContract;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class SimulatorConfigurationService implements SimulatorConfigurationServiceContract {

    private static final String SIMULATOR_BASE_URL = "http://localhost:3000";
    private static final String GARAGE_URL = "/garage";
    private final RestTemplate restTemplate;
    private final GarageRepository garageRepository;
    private final GarageService garageService;
    private final SpotService spotService;
    private final ParkingEventRepository parkingEventRepository;


    public SimulatorConfigurationService(RestTemplate restTemplate, GarageRepository garageRepository, GarageService garageService, SpotService spotService, ParkingEventRepository parkingEventRepository) {
        this.restTemplate = restTemplate;
        this.garageRepository = garageRepository;
        this.garageService = garageService;
        this.spotService = spotService;

        this.parkingEventRepository = parkingEventRepository;
    }

    @Transactional
    @Override
    public void fetchAndPersistGarageConfiguration() {

        if (garageRepository.count() > 0) {
            return;
        }


        try {
            GarageConfigResponse response = restTemplate.getForObject(SIMULATOR_BASE_URL + GARAGE_URL, GarageConfigResponse.class);

            if (response == null || response.getGarage() == null) {

                System.out.println("The simulator response was invalid or null!!");
                return;
            }

            Map<String, GarageModel> sectorMap = new HashMap<>();
            Map<String, Integer> initialOccupancyCounts = new HashMap<>();
            Map<String, SpotModel> savedSpotsMap = new HashMap<>();

            response.getGarage().forEach(garage -> {
                GarageModel garageModel = new GarageModel(garage.getSector(), garage.getBasePrice(), garage.getMaxCapacity()
                        , garage.getCurrentOccupancy(), garage.getOpenHour(), garage.getCloseHour());

                sectorMap.put(garage.getSector(), garageService.saveGarage(garageModel));
                initialOccupancyCounts.put(garage.getSector(), 0);
            });

            response.getSpots().forEach(spot -> {
                GarageModel sector = sectorMap.get(spot.getSector());
                if (sector != null) {
                    String licensePlate = spot.isOccupied() ? "INIT_PLACA_SIM_" + spot.getId() : null;
                    SpotModel spotModel = new SpotModel(spot.getId(), spot.getLat(),
                            spot.getLng(), sector, spot.isOccupied(), licensePlate);
//                    spotModel.setId(spot.getId());
//                    spotModel.setLat(spot.getLat());
//                    spotModel.setLng(spot.getLng());
//                    spotModel.setGarageModel(sector);
//                    spotModel.setOccupied(spot.isOccupied());
//                    spotModel.setOccupiedByLicensePlate(licensePlate);
                    SpotModel savedSpot = spotService.saveSpot(spotModel);

                    if (spot.isOccupied()) {
                        initialOccupancyCounts.merge(sector.getSector(), 1, Integer::sum);
                        savedSpotsMap.put(licensePlate, savedSpot);
                    }

                }
            });

            initialOccupancyCounts.forEach((sectorKey, count) -> {
                GarageModel sector = sectorMap.get(sectorKey);
                if (sector != null) {
                    sector.setCurrentOccupancy(count);
                    garageRepository.save(sector);
                }
            });

            OffsetDateTime initialEntryTime = OffsetDateTime.now(ZoneOffset.ofHours(-3)).minusHours(1);

            savedSpotsMap.forEach((licensePlate, spot) -> {
                ParkingEventModel parkingEvent = new ParkingEventModel();
                parkingEvent.setLicensePlate(licensePlate);
                parkingEvent.setEntryTime(initialEntryTime);
                parkingEvent.setSpotModel(spot);
                parkingEvent.setGarageModel(spot.getGarageModel());
                parkingEvent.setDynamicPriceMultiplier(1.0);
                parkingEvent.setLat(spot.getLat());
                parkingEvent.setLgn(spot.getLng());

                parkingEventRepository.save(parkingEvent);
            });


        } catch (Exception e) {

            e.printStackTrace();
            throw new InvalidSimulatorException("ERRO ao buscar configuração do simulador. Verifique se o Docker está rodando.");


        }
    }
}
