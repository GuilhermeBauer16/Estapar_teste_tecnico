package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "parking_events")

public class ParkingEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "entry_time")
    private OffsetDateTime entryTime;
    @Column(name = "exit_time")
    private OffsetDateTime exitTime;
    @Column(name = "final_amount")
    private Double finalAmount;

    private Double lat;
    private Double lgn;

    @Column(name = "applied_discount_rate")
    private Double dynamicPriceMultiplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id_fk", referencedColumnName = "id", nullable = false)
    private SpotModel spotModel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_fk", referencedColumnName = "sector", nullable = false)
    private GarageModel garageModel;

    public ParkingEventModel() {
    }

    public ParkingEventModel(Long id, String licensePlate, OffsetDateTime entryTime, OffsetDateTime exitTime,
                             Double finalAmount, Double lat, Double lgn, Double dynamicPriceMultiplier, SpotModel spotModel, GarageModel garageModel) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.finalAmount = finalAmount;
        this.lat = lat;
        this.lgn = lgn;
        this.dynamicPriceMultiplier = dynamicPriceMultiplier;
        this.spotModel = spotModel;
        this.garageModel = garageModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public OffsetDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(OffsetDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public OffsetDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(OffsetDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Double getDynamicPriceMultiplier() {
        return dynamicPriceMultiplier;
    }

    public void setDynamicPriceMultiplier(Double dynamicPriceMultiplier) {
        this.dynamicPriceMultiplier = dynamicPriceMultiplier;
    }

    public SpotModel getSpotModel() {
        return spotModel;
    }

    public void setSpotModel(SpotModel spotModel) {
        this.spotModel = spotModel;
    }

    public GarageModel getGarageModel() {
        return garageModel;
    }

    public void setGarageModel(GarageModel garageModel) {
        this.garageModel = garageModel;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLgn() {
        return lgn;
    }

    public void setLgn(Double lgn) {
        this.lgn = lgn;
    }
}
