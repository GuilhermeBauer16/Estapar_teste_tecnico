package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "parking_records")
public class ParkingRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licensePlate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector",
            referencedColumnName = "sector",
            nullable = false,
            insertable = false, updatable = false)
    private GarageModel garageModel;
    private Instant entryTime;
    private Instant exitTime;
    private double effectiveHourlyRate;
    private Double calculatedHourlyPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private SpotModel spotModel;
}
