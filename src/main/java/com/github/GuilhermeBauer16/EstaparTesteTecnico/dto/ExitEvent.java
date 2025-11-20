package com.github.GuilhermeBauer16.EstaparTesteTecnico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExitEvent {
    private String license_plate;
    private String exit_time;
    private String event_type;

    public ExitEvent(String license_plate, String exit_time, String event_type) {
        this.license_plate = license_plate;
        this.exit_time = exit_time;
        this.event_type = event_type;
    }
}
