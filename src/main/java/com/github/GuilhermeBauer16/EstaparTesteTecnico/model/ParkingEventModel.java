package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Table(name = "parking_records")
public class ParkingEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "entry_time")
    private ZonedDateTime entryTime;
    @Column(name = "exit_time")
    private ZonedDateTime exitTime;
    @Column(name = "final_amount")
    private Double finalAmount;

    @Column(name = "applied_discount_rate")
    private Double appliedDiscountRate;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id_fk", referencedColumnName = "id", nullable = false)
    private SpotModel spotModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_fk", referencedColumnName = "sector", nullable = false)
    private GarageModel garageModel;


}
