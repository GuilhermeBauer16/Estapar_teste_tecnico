package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;

public interface ParkingEventServiceContract {


    ParkingEventModel saveParkingEvent(ParkingEventModel parkingEventModel);

    ParkingEventModel findParkingEventByLicensePlate(String licensePlate);

    boolean isVehicleCurrentlyParked(String licensePlate);

}
