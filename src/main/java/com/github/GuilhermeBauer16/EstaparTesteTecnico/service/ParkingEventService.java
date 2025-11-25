package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;

public interface ParkingEventService {

    ParkingEventModel parkedEvent(WebhookEventDTO webhookEventDTO);
}
