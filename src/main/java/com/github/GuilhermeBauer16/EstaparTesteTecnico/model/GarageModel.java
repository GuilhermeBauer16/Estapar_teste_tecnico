package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "garages")
public class GarageModel {

    @Id
    private String sector;
    private String basePrice;
    private int maxCapacity;
    private int currentCapacity;

    public GarageModel() {
    }

    public GarageModel(String sector, String basePrice, int maxCapacity, int currentCapacity) {
        this.sector = sector;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
}
