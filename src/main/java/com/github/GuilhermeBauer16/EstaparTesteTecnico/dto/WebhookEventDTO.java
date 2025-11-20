package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

public class WebhookEventDTO {
    private String license_plate;
    private String entry_time;


    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

}
