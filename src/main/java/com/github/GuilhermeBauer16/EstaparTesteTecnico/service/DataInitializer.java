package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final GarageConfigurationService garageConfigurationService;


    @Autowired
    public DataInitializer(GarageConfigurationService garageConfigurationService) {
        this.garageConfigurationService = garageConfigurationService;

    }

    @Override
    public void run(String... args) throws Exception {
        garageConfigurationService.fetchAndPersistGarageConfiguration();
    }

}
