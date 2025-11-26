package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.enums.EventType;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEventTypeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.ParkingEventServiceImplContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventServiceImpl implements ParkingEventServiceImplContract {

    private final EventTypeService eventTypeService;

    public static final String EVENT_TYPE_REQUIRED = "The 'eventType' field is mandatory and cannot be empty.";

    public static final String INVALID_EVENT_TYPE_FORMAT = "Invalid event type ('%s'). Valid types are: %s";

    public static final String VALID_EVENT_TYPES_LIST = "ENTRY, PARKED, or EXIT";


    @Autowired
    public ParkingEventServiceImpl(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }


    @Override
    public void parkedEvent(WebhookEventDTO webhookEventDTO) {

        if (webhookEventDTO.getEventType() == null || webhookEventDTO.getEventType().isBlank()) {
            throw new InvalidEventTypeException(EVENT_TYPE_REQUIRED);
        }

        EventType event;

        try {

            event = EventType.valueOf(webhookEventDTO.getEventType().toUpperCase());

        } catch (IllegalArgumentException e) {

            String receivedEvent = webhookEventDTO.getEventType();
            String message = String.format(
                    INVALID_EVENT_TYPE_FORMAT,
                    receivedEvent,
                    VALID_EVENT_TYPES_LIST
            );
            throw new InvalidEventTypeException(message);
        }


        switch (event) {

            case ENTRY:
                eventTypeService.handleEntryEvent(webhookEventDTO.getSector(), webhookEventDTO.getLicensePlate(), webhookEventDTO.getEntryTime());
                break;

            case PARKED:

                eventTypeService.handleParkedEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getLat(), webhookEventDTO.getLng());
                break;

            case EXIT:

                eventTypeService.handleExitEvent(webhookEventDTO.getLicensePlate(), webhookEventDTO.getExitTime());
                break;

        }
    }


}
