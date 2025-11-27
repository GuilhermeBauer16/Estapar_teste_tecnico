package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;

@Entity
@Table(name = "garages")
public class GarageModel {

    @Id
    private String sector;
    @Column(name = "base_price")
    private Double basePrice;
    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy;

    @Column(name = "open_hour")
    private LocalTime openHour;

    @Column(name = "close_hour")
    private LocalTime closeHour;


    public GarageModel() {
    }

    public GarageModel(String sector, Double basePrice, int maxCapacity, Integer currentOccupancy, LocalTime openHour, LocalTime closeHour) {
        this.sector = sector;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
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
