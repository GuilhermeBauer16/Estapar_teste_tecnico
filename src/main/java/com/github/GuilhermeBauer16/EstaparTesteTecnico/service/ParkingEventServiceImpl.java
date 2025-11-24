package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventServiceImpl implements ParkingEventService {
    @Override
    public WebhookEventDTO parkedEvent(WebhookEventDTO webhookEventDTO) {
        String eventType = webhookEventDTO.getEventType();

        switch (eventType) {
            case "PARKED":
                handlerWithParkedEvent(webhookEventDTO);
                return webhookEventDTO;


            case "ENTRY":
                handlerWithEntryEvent(webhookEventDTO);
                return webhookEventDTO;


            case "EXIT":
                handlerWithExitEvent(webhookEventDTO);
                return webhookEventDTO;


            default:
                throw new IllegalArgumentException("Event type not supported");
        }
    }

    private String handlerWithParkedEvent(WebhookEventDTO webhookEventDTO) {

        return String.format("Parked event %s", webhookEventDTO.getEventType());
    }

    private String handlerWithExitEvent(WebhookEventDTO webhookEventDTO) {
        return String.format("Exit event %s", webhookEventDTO.getEventType());
    }

    private String handlerWithEntryEvent(WebhookEventDTO webhookEventDTO) {

        System.out.println("I arrive here!!!!");
        return String.format("Entry event %s", webhookEventDTO.getEventType());
    }
}
