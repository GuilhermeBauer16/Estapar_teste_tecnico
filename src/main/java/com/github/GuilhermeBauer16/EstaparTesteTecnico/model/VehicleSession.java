package com.github.GuilhermeBauer16.EstaparTesteTecnico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name="vehicle_session")
public class VehicleSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String licensePlate;
    private String sector;
    private OffsetDateTime entryTime;
    private OffsetDateTime exitTime;

}
