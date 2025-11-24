package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryEventDTO {

    private String license_plate;
    private Instant entry_time;


    public EntryEventDTO() {
    }

    public EntryEventDTO(String license_plate, Instant entry_time) {
        this.license_plate = license_plate;
        this.entry_time = entry_time;

    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public Instant getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(Instant entry_time) {
        this.entry_time = entry_time;
    }
}
