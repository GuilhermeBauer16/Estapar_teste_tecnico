package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;

public interface ParkingEventService {

    WebhookEventDTO parkedEvent(WebhookEventDTO webhookEventDTO);
}
