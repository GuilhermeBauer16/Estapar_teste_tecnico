package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.GarageConfigResponse;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String SIMULATOR_BASE_URL = "http://localhost:8080";
    private static final String GARAGE_URL = "/garage";
    private final RestTemplate restTemplate;
    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;

    public DataInitializer(RestTemplate restTemplate, GarageRepository garageRepository, SpotRepository spotRepository) {
        this.restTemplate = restTemplate;
        this.garageRepository = garageRepository;
        this.spotRepository = spotRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Buscando configuração inicial da garagem no simulador...");
        fetchAndPersistGarageConfiguration();
    }

    private void fetchAndPersistGarageConfiguration() {

        try {
            GarageConfigResponse response = restTemplate.getForObject(SIMULATOR_BASE_URL + GARAGE_URL, GarageConfigResponse.class);
            if (response != null && response.getGarage() != null) {
                response.getGarage().forEach(rawGarage -> {

                    GarageModel garageModel = new GarageModel(
                            rawGarage.getSector(),
                            rawGarage.getBasePrice(),
                            rawGarage.getMax_capacity(),
                            0
                    );
                    garageRepository.save(garageModel);
                });
                
                response.getSpots().forEach(rawSpot -> {
                    GarageModel sector = garageRepository.findById(rawSpot.getSector()).orElse(null);

                    if(sector != null) {
                        SpotModel spotModel = new SpotModel();
                        spotModel.setId(rawSpot.getId());
                        spotModel.setLat(rawSpot.getLat());
                        spotModel.setLng(rawSpot.getLng());
                        spotModel.setGarageModel(sector);
                        spotModel.setOccupied(rawSpot.isOccupied());


                        spotRepository.save(spotModel);

                        if (rawSpot.isOccupied()) {
                            sector.setCurrentOccupancy(sector.getCurrentOccupancy() + 1);
                            garageRepository.save(sector); // Salva o setor atualizado
                        }
                    }

                });

                System.out.println("Configuração inicial da garagem salva com sucesso!");
            }
        }catch (Exception e) {
            System.err.println("ERRO ao buscar configuração do simulador. Verifique se o Docker está rodando na porta 8080.");
            e.printStackTrace();
        }

    }
}
