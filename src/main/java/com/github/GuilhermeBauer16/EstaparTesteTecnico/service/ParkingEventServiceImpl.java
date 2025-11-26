package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.enums.EventType;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.ParkingEventServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventServiceImpl {

    private final EventTypeService parkingService;


    @Autowired
    public ParkingEventServiceImpl(EventTypeService parkingService) {
        this.parkingService = parkingService;
    }


    public ParkingEventModel parkedEvent(WebhookEventDTO webhookEventDTO) {
        EventType event = EventType.valueOf(webhookEventDTO.getEventType().toUpperCase());

        switch (event) {

            case ENTRY:
                ParkingEventModel parkingEventModel = parkingService.handleEntryEvent(webhookEventDTO.getSector(), webhookEventDTO.getLicensePlate(), webhookEventDTO.getEntryTime());
                return parkingEventModel;

            case PARKED:

                ParkingEventModel parkingEventModel1 = parkingService.handleParkedEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getLat(), webhookEventDTO.getLng());
                return parkingEventModel1;


            case EXIT:

                ParkingEventModel parkingEventModelExit = parkingService.handleExitEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getExitTime());
                return parkingEventModelExit;


            default:
                throw new IllegalArgumentException("Event type not supported");
        }
    }


}
