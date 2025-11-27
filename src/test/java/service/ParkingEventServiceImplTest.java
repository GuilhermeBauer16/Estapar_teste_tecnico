package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEventTypeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ParkingEventServiceImplTest {


    @Mock
    private EventTypeService eventTypeService;


    @InjectMocks
    private ParkingEventServiceImpl parkingEventService;


    private final String LICENSE_PLATE = "ABC1234";
    private final String SECTOR = "A1";
    private final OffsetDateTime ENTRY_TIME = OffsetDateTime.now(ZoneOffset.ofHours(-3));
    private final Double LAT = -23.5505;
    private final Double LNG = -46.6333;
    private final OffsetDateTime EXIT_TIME = ENTRY_TIME.plusHours(2);


    private WebhookEventDTO createBaseDTO(String eventType) {

        WebhookEventDTO dto = new WebhookEventDTO();


        dto.setEventType(eventType);
        dto.setSector(SECTOR);
        dto.setLicensePlate(LICENSE_PLATE);
        dto.setEntryTime(ENTRY_TIME);
        dto.setLat(LAT);
        dto.setLng(LNG);
        dto.setExitTime(EXIT_TIME);

        return dto;
    }


    @Test
    void parkedEvent_Failure_EventTypeIsNull() {

        WebhookEventDTO dto = createBaseDTO(null);


        InvalidEventTypeException exception = assertThrows(InvalidEventTypeException.class,
                () -> parkingEventService.parkedEvent(dto));


        assertEquals(ParkingEventServiceImpl.EVENT_TYPE_REQUIRED, exception.getMessage());


        verify(eventTypeService, never()).handleEntryEvent(SECTOR, LICENSE_PLATE, ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(LICENSE_PLATE, LAT, LNG);
        verify(eventTypeService, never()).handleExitEvent(LICENSE_PLATE, EXIT_TIME);
    }

    @Test
    void parkedEvent_Failure_EventTypeIsBlank() {

        WebhookEventDTO dto = createBaseDTO(" ");


        InvalidEventTypeException exception = assertThrows(InvalidEventTypeException.class,
                () -> parkingEventService.parkedEvent(dto));


        assertEquals(ParkingEventServiceImpl.EVENT_TYPE_REQUIRED, exception.getMessage());
    }

    @Test
    void parkedEvent_Failure_InvalidEventTypeFormat() {

        String invalidType = "NOT_AN_EVENT";
        WebhookEventDTO dto = createBaseDTO(invalidType);


        String expectedMessage = String.format(
                ParkingEventServiceImpl.INVALID_EVENT_TYPE_FORMAT,
                invalidType,
                ParkingEventServiceImpl.VALID_EVENT_TYPES_LIST
        );


        InvalidEventTypeException exception = assertThrows(InvalidEventTypeException.class,
                () -> parkingEventService.parkedEvent(dto));


        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void parkedEvent_Success_EntryEvent() {

        WebhookEventDTO dto = createBaseDTO("ENTRY");


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleEntryEvent(SECTOR, LICENSE_PLATE, ENTRY_TIME);


        verify(eventTypeService, never()).handleParkedEvent(LICENSE_PLATE, LAT, LNG);
        verify(eventTypeService, never()).handleExitEvent(LICENSE_PLATE, EXIT_TIME);
    }

    @Test
    void parkedEvent_Success_ParkedEvent() {

        WebhookEventDTO dto = createBaseDTO("PARKED");


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleParkedEvent(LICENSE_PLATE, LAT, LNG);


        verify(eventTypeService, never()).handleEntryEvent(SECTOR, LICENSE_PLATE, ENTRY_TIME);
        verify(eventTypeService, never()).handleExitEvent(LICENSE_PLATE, EXIT_TIME);
    }

    @Test
    void parkedEvent_Success_ExitEvent() {

        WebhookEventDTO dto = createBaseDTO("EXIT");


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleExitEvent(LICENSE_PLATE, EXIT_TIME);


        verify(eventTypeService, never()).handleEntryEvent(SECTOR, LICENSE_PLATE, ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(LICENSE_PLATE, LAT, LNG);
    }

    @Test
    void parkedEvent_Success_HandlesLowercaseInput() {

        WebhookEventDTO dto = createBaseDTO("entry");


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleEntryEvent(SECTOR, LICENSE_PLATE, ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(LICENSE_PLATE, LAT, LNG);
    }
}