package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;


public class WebhookEventDTO {

        @JsonProperty("license_plate")
        private String licensePlate;

        @JsonProperty("event_type")
        private String eventType;


        @JsonProperty("entry_time")
        private ZonedDateTime entryTime;


        @JsonProperty("exit_time")
        private ZonedDateTime exitTime;


        private Double lat;
        private Double lng;

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

    public ZonedDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(ZonedDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public ZonedDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(ZonedDateTime exitTime) {
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


    }

