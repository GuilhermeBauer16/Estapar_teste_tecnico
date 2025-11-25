package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventServiceImpl implements ParkingEventService {

    private final ParkingService parkingService;

    @Autowired
    public ParkingEventServiceImpl(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Override
    public WebhookEventDTO parkedEvent(WebhookEventDTO webhookEventDTO) {
        String eventType = webhookEventDTO.getEventType();

        switch (eventType) {
            case "PARKED":
                return webhookEventDTO;


            case "ENTRY":
                parkingService.handleEntryEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getEntryTime());
                return webhookEventDTO;


            case "EXIT":

                parkingService.handleExitEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getExitTime());
                return webhookEventDTO;


            default:
                throw new IllegalArgumentException("Event type not supported");
        }
    }


}
