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
    private boolean isOccupied;
    private String occupiedByLicensePlate;

}
