package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.GarageServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class GarageService implements GarageServiceContract {

    private final GarageRepository garageRepository;

    @Autowired
    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @Override
    public GarageModel saveGarage(GarageModel garageModel) {


        return garageRepository.save(garageModel);
    }

    @Override
    public boolean isGarageOpen(LocalTime entryTime, LocalTime openHour, LocalTime closeHour) {


        if (openHour.isBefore(closeHour) || openHour.equals(closeHour)) {

            return !entryTime.isBefore(openHour) && !entryTime.isAfter(closeHour);

        } else {

            return !entryTime.isBefore(openHour) || !entryTime.isAfter(closeHour);
        }
    }

}

