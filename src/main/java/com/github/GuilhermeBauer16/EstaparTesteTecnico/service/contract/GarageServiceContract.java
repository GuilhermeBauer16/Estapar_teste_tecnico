package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;

import java.time.LocalTime;

public interface GarageServiceContract {

    GarageModel saveGarage(GarageModel garageModel);

    boolean isGarageOpen(LocalTime entryTime, LocalTime openHour, LocalTime closeHour);
}
