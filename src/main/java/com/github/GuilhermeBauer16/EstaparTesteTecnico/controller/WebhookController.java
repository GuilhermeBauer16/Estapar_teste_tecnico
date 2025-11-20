package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

import io.swagger.v3.oas.annotations.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/webhook")
@RestController
public class WebhookController {

    @PostMapping
    public ResponseEntity<String> receiveEvent(@RequestBody WebhookEvent webhookEvent) {

        System.out.println("Event: recebido " + webhookEvent);
        return ResponseEntity.ok(webhookEvent.toString());
    }
}
