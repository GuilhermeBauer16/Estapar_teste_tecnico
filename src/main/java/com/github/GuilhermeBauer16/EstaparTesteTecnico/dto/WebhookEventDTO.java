package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;


public class WebhookEventDTO {

    private String sector;

    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("entry_time")
    private OffsetDateTime entryTime;
    @JsonProperty("exit_time")
    private OffsetDateTime exitTime;

    private Double lat;

    private Double lng;

    public WebhookEventDTO() {
    }

    public WebhookEventDTO(String sector, String licensePlate, String eventType, OffsetDateTime entryTime, OffsetDateTime exitTime, Double lat, Double lng) {
        this.sector = sector;
        this.licensePlate = licensePlate;
        this.eventType = eventType;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.lat = lat;
        this.lng = lng;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}

