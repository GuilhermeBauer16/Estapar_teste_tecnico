package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "parking_records")
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

    @Column(name = "applied_discount_rate")
    private Double dynamicPriceMultiplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id_fk", referencedColumnName = "id", nullable = false)
    private SpotModel spotModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_fk", referencedColumnName = "sector", nullable = false)
    private GarageModel garageModel;


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




}
