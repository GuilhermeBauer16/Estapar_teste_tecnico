package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
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
    public ParkingEventModel parkedEvent(WebhookEventDTO webhookEventDTO) {
        String eventType = webhookEventDTO.getEventType();

        switch (eventType) {

            case "ENTRY":
                ParkingEventModel parkingEventModel = parkingService.handleEntryEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getEntryTime());
                return parkingEventModel;

            case "PARKED":

                ParkingEventModel parkingEventModel1 = parkingService.handleParkedEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getLat(), webhookEventDTO.getLng());
                return parkingEventModel1;


            case "EXIT":

                ParkingEventModel parkingEventModelExit = parkingService.handleExitEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getExitTime());
                return parkingEventModelExit;


            default:
                throw new IllegalArgumentException("Event type not supported");
        }
    }


}
