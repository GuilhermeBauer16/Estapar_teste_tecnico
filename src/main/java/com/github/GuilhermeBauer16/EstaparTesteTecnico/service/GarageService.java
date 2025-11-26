package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class GarageService {

    private final GarageRepository garageRepository;


    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public GarageModel saveGarage(GarageModel garageModel) {


        return garageRepository.save(garageModel);
    }

    public boolean isGarageOpen(LocalTime entryTime, LocalTime openHour, LocalTime closeHour) {


        if (openHour.isBefore(closeHour) || openHour.equals(closeHour)) {

            return !entryTime.isBefore(openHour) && !entryTime.isAfter(closeHour);

        } else {

            return !entryTime.isBefore(openHour) || !entryTime.isAfter(closeHour);
        }
    }

}

