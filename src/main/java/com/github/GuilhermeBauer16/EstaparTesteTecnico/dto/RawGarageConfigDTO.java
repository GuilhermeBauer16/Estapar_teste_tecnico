package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RawGarageConfigDTO {

    private String sector;
    @JsonProperty("base_price")
    private Double basePrice;
    @JsonProperty("max_capacity")
    private Integer max_capacity;

    public RawGarageConfigDTO() {
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(Integer max_capacity) {
        this.max_capacity = max_capacity;
    }
}
