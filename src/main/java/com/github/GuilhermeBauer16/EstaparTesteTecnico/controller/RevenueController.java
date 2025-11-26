package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RevenueDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    private final EventTypeService parkingService;

    @Autowired
    public RevenueController(RevenueService revenueService, EventTypeService parkingService) {
        this.revenueService = revenueService;
        this.parkingService = parkingService;
    }

    @GetMapping
    public ResponseEntity<RevenueDTO> getRevenueBySectorAndDate(
            @RequestParam("sector") String sectorName,
            @RequestParam("date") String dateString,
            @RequestParam(value = "currency", required = false, defaultValue = "BRL") String currency) {

        try {

            OffsetDateTime date = OffsetDateTime.parse(dateString + "T00:00:00Z");


            RevenueDTO revenueDTO = revenueService.getRevenueBySectorAndDate(sectorName, date, currency);


            return ResponseEntity.ok(revenueDTO);

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Formato de data inválido. Use o formato YYYY-MM-DD. Exemplo: 2025-01-01.");
        }
    }

}
