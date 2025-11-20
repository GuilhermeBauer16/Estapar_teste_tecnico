package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

public class WebhookEvent {

    private String event;
    private String plate;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Override
    public String toString() {
        return "WebhookEvent{" +
                "event='" + event + '\'' +
                ", plate='" + plate + '\'' +
                '}';
    }
}
