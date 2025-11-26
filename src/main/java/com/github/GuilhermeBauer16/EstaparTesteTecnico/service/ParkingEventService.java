package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ParkingEventNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.ParkingEventServiceContract;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventService implements ParkingEventServiceContract {

    private static final String PARKING_EVENT_NOT_FOUND_EXCEPTION_MESSAGE = "A vehicle with that license plate %s was not found";
    private final ParkingEventRepository repository;


    public ParkingEventService(ParkingEventRepository repository) {
        this.repository = repository;

    }

    @Override
    public ParkingEventModel saveParkingEvent(ParkingEventModel parkingEventModel) {
        return repository.save(parkingEventModel);
    }

    @Override
    public ParkingEventModel findParkingEventByLicensePlate(String licensePlate) {

        return repository.findByLicensePlateAndExitTimeIsNull(licensePlate)
                .orElseThrow(() -> new ParkingEventNotFoundException(
                        String.format(PARKING_EVENT_NOT_FOUND_EXCEPTION_MESSAGE, licensePlate)));


    }


    @Override
    public boolean isVehicleCurrentlyParked(String licensePlate) {
        return repository.findByLicensePlateAndExitTimeIsNull(licensePlate).isPresent();
    }
}
