package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.SpotServiceContract;
import org.springframework.stereotype.Service;

@Service
public class SpotService implements SpotServiceContract {

    private static final String GARAGE_FULL_EXCEPTION_MESSAGE = "Have nothing available places to parking in this garage at the moment, please try again.";
    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;

    }

    @Override
    public SpotModel saveSpot(SpotModel spotModel) {


        return spotRepository.save(spotModel);
    }


    @Override
    public SpotModel findFistAvailableSpot(String sector) {

        return spotRepository.findFirstByGarageModel_SectorAndIsOccupied(sector, false).orElseThrow(
                () -> new GarageFullException(GARAGE_FULL_EXCEPTION_MESSAGE));

    }
}
