package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;

public interface SpotServiceContract {

    SpotModel saveSpot(SpotModel spotModel);
    SpotModel findFistAvailableSpot(String sector);
}
