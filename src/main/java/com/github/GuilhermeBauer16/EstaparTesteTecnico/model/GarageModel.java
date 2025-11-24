package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name= "garages")
public class GarageModel {

    @Id
    private String sector;
    @Column(name = "base_price")
    private Double basePrice;
    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy;

    @OneToMany(mappedBy = "garageModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SpotModel> spots;

    public GarageModel() {
    }

    public GarageModel(String sector, Double basePrice, int maxCapacity, int currentOccupancy) {
        this.sector = sector;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.currentOccupancy = currentOccupancy;
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

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }
}
