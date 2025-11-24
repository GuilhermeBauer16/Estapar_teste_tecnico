package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class ParkingWebhookController {

    private final ParkingEventService service;

    @Autowired
    public ParkingWebhookController(ParkingEventService service) {
        this.service = service;


    }

    @PostMapping
    public ResponseEntity<WebhookEventDTO> handleWebhookEvent(@RequestBody WebhookEventDTO eventDTO) {


        WebhookEventDTO webhookEventDTO = service.parkedEvent(eventDTO);

        return new ResponseEntity<>(webhookEventDTO, HttpStatus.OK);
    }
}
