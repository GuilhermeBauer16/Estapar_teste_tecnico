package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import java.util.List;

public class GarageConfigResponse {

    private List<RawGarageConfigDTO> garage;

    private List<RawSpotConfigDTO> spots;

    public GarageConfigResponse() {
    }

    public List<RawGarageConfigDTO> getGarage() {
        return garage;
    }

    public void setGarage(List<RawGarageConfigDTO> garage) {
        this.garage = garage;
    }

    public List<RawSpotConfigDTO> getSpots() {
        return spots;
    }

    public void setSpots(List<RawSpotConfigDTO> spots) {
        this.spots = spots;
    }
}
