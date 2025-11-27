package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RevenueDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.RevenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevenueServiceTest {

    private ParkingEventRepository parkingEventRepository;
    private RevenueService revenueService;

    @BeforeEach
    void setup() {
        parkingEventRepository = mock(ParkingEventRepository.class);
        revenueService = new RevenueService(parkingEventRepository);
    }

    @Test
    void getRevenueBySectorAndDate_shouldReturnCorrectRevenue() {
        String sector = "A1";
        String currency = "BRL";
        OffsetDateTime date = OffsetDateTime.of(2024, 10, 5, 15, 0, 0, 0, ZoneOffset.UTC);

        ParkingEventModel event1 = new ParkingEventModel();
        event1.setFinalAmount(30.0);

        ParkingEventModel event2 = new ParkingEventModel();
        event2.setFinalAmount(45.0);

        when(parkingEventRepository.findCompletedEventsBySectorAndExitDateRange(
                eq(sector),
                any(OffsetDateTime.class),
                any(OffsetDateTime.class))
        ).thenReturn(List.of(event1, event2));

        RevenueDTO result = revenueService.getRevenueBySectorAndDate(sector, date, currency);

        assertNotNull(result);
        assertEquals(75.0, result.getAmount());
        assertEquals(currency, result.getCurrency());
        assertEquals(date, result.getTimestamp());
    }

    @Test
    void getRevenueBySectorAndDate_shouldReturnZeroWhenNoEvents() {
        String sector = "B2";
        String currency = "USD";
        OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC);

        when(parkingEventRepository.findCompletedEventsBySectorAndExitDateRange(
                anyString(), any(), any())
        ).thenReturn(List.of());

        RevenueDTO result = revenueService.getRevenueBySectorAndDate(sector, date, currency);

        assertNotNull(result);
        assertEquals(0.0, result.getAmount());
        assertEquals(currency, result.getCurrency());
    }

    @Test
    void getRevenueBySectorAndDate_shouldPassCorrectDateRangeToRepository() {

        String sector = "C1";
        String currency = "EUR";

        OffsetDateTime date = OffsetDateTime.of(2024, 11, 10, 10, 30, 0, 0, ZoneOffset.UTC);

        when(parkingEventRepository.findCompletedEventsBySectorAndExitDateRange(
                anyString(), any(), any())
        ).thenReturn(List.of());

        revenueService.getRevenueBySectorAndDate(sector, date, currency);


        ArgumentCaptor<OffsetDateTime> startCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> endCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);

        verify(parkingEventRepository).findCompletedEventsBySectorAndExitDateRange(
                eq(sector),
                startCaptor.capture(),
                endCaptor.capture()
        );

        OffsetDateTime start = startCaptor.getValue();
        OffsetDateTime end = endCaptor.getValue();

        assertEquals(0, start.getHour());
        assertEquals(0, start.getMinute());
        assertEquals(0, start.getSecond());
        assertEquals(0, start.getNano());

        assertEquals(23, end.getHour());
        assertEquals(59, end.getMinute());
        assertEquals(59, end.getSecond());
        assertEquals(999_999_999, end.getNano());
    }
}
