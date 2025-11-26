package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RevenueDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.RevenueServiceContract;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RevenueService implements RevenueServiceContract {
    private final ParkingEventRepository parkingEventRepository;

    public RevenueService(ParkingEventRepository parkingEventRepository) {
        this.parkingEventRepository = parkingEventRepository;
    }

    @Override
    public RevenueDTO getRevenueBySectorAndDate(String sectorName, OffsetDateTime date, String currency) {


        OffsetDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);


        List<ParkingEventModel> events = parkingEventRepository.findCompletedEventsBySectorAndExitDateRange(
                sectorName, startOfDay, endOfDay);
        double totalAmount = events.stream()

                .mapToDouble(ParkingEventModel::getFinalAmount)
                .sum();

        return new RevenueDTO(currency, totalAmount, date);
    }
}
