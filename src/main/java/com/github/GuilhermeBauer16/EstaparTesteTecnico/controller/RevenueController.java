package com.github.GuilhermeBauer16.EstaparTesteTecnico.controller;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RevenueDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.RevenueService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.RevenueServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/revenue")
public class RevenueController implements RevenueServiceContract {

    private final RevenueService revenueService;


    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;

    }

    @Override
    public RevenueDTO getRevenueBySectorAndDate(String sectorName, OffsetDateTime date, String currency) {
        return null;
    }

    @GetMapping

    public ResponseEntity<RevenueDTO> getRevenueBySectorAndDate(
            @RequestParam("sector") String sectorName,
            @RequestParam("date") String dateString,
            @RequestParam(value = "currency", required = false, defaultValue = "BRL") String currency) {


        OffsetDateTime date = OffsetDateTime.parse(dateString + "T00:00:00Z");


        RevenueDTO revenueDTO = revenueService.getRevenueBySectorAndDate(sectorName, date, currency);


        return ResponseEntity.ok(revenueDTO);


    }


}
