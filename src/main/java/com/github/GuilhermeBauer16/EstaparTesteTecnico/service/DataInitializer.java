package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String SIMULADOR_URL = "http://localhost:8080";
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

    }

    private void fetchAndPersistGarageConfiguration(){

    }
}
