package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "spots")
public class SpotModel {

    @Id
    private Long id;
    private double lat;
    private double lng;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector",
            referencedColumnName = "sector",
            nullable = false)
    private GarageModel garageModel;
    private Boolean isOccupied;
    private String occupiedByLicensePlate;

    public SpotModel() {
    }

    public SpotModel(Long id, double lat, double lng, GarageModel garageModel, boolean isOccupied, String occupiedByLicensePlate) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.garageModel = garageModel;
        this.isOccupied = isOccupied;
        this.occupiedByLicensePlate = occupiedByLicensePlate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public GarageModel getGarageModel() {
        return garageModel;
    }

    public void setGarageModel(GarageModel garageModel) {
        this.garageModel = garageModel;
    }

    public Boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getOccupiedByLicensePlate() {
        return occupiedByLicensePlate;
    }

    public void setOccupiedByLicensePlate(String occupiedByLicensePlate) {
        this.occupiedByLicensePlate = occupiedByLicensePlate;
    }
}
