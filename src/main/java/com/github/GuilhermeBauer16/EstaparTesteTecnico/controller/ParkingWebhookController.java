package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventServiceImpl;
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

    private final ParkingEventServiceImpl service;

    @Autowired
    public ParkingWebhookController(ParkingEventServiceImpl service) {
        this.service = service;


    }

    @PostMapping
    public ResponseEntity<ParkingEventModel> handleWebhookEvent(@RequestBody WebhookEventDTO eventDTO) {


        ParkingEventModel webhookEventDTO = service.parkedEvent(eventDTO);

        return new ResponseEntity<>(webhookEventDTO, HttpStatus.OK);
    }
}
