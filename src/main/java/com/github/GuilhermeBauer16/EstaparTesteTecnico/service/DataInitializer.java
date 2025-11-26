package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SimulatorConfigurationService garageConfigurationService;


    @Autowired
    public DataInitializer(SimulatorConfigurationService garageConfigurationService) {
        this.garageConfigurationService = garageConfigurationService;

    }

    @Override
    public void run(String... args) {
        garageConfigurationService.fetchAndPersistGarageConfiguration();
    }

}
