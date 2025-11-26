package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RevenueDTO;

import java.time.OffsetDateTime;

public interface RevenueServiceContract {

    RevenueDTO getRevenueBySectorAndDate(String sectorName, OffsetDateTime date, String currency);
}
