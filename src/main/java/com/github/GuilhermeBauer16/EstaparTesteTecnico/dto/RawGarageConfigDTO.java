package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public class RawGarageConfigDTO {

    private String sector;
    @JsonProperty("base_price")
    private Double basePrice;
    @JsonProperty("max_capacity")
    private Integer maxCapacity;
    @JsonProperty("current_occupancy")
    private Integer currentOccupancy;

    @JsonProperty("open_hour")
    private LocalTime openHour;

    @JsonProperty("close_hour")
    private LocalTime closeHour;

    public RawGarageConfigDTO() {
    }

    public RawGarageConfigDTO(String sector, Double basePrice, Integer max_capacity, Integer currentOccupancy, LocalTime openHour, LocalTime closeHour) {
        this.sector = sector;
        this.basePrice = basePrice;
        this.maxCapacity = max_capacity;
        this.currentOccupancy = currentOccupancy;
        this.openHour = openHour;
        this.closeHour = closeHour;
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

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(Integer currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(LocalTime openHour) {
        this.openHour = openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(LocalTime closeHour) {
        this.closeHour = closeHour;
    }
}
