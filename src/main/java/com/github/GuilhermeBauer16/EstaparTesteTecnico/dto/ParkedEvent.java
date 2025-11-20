package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkedEvent {

    private String license_plate;
    private Double lat;
    private Double lgn;
    private Instant exit_time;
    @Setter
    private String eventType;

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
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

    public String getEventType() {
        return eventType;
    }

    public Instant getExit_time() {
        return exit_time;
    }

    public void setExit_time(Instant exit_time) {
        this.exit_time = exit_time;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
