package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;

public interface ParkingEventServiceImplContract {

    void parkedEvent(WebhookEventDTO webhookEventDTO);
}
