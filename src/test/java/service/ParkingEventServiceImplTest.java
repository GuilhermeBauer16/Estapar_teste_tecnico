package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.WebhookEventDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEventTypeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventServiceImpl;
import constants.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    private WebhookEventDTO createBaseDTO(String eventType) {

        WebhookEventDTO dto = new WebhookEventDTO(TestConstants.SECTOR, TestConstants.LICENSE_PLATE,
                eventType, TestConstants.ENTRY_TIME, TestConstants.EXIT_TIME, TestConstants.LAT, TestConstants.LNG);

        return dto;
    }


    @Test
    void parkedEvent_Failure_EventTypeIsNull() {

        WebhookEventDTO dto = createBaseDTO(null);


        InvalidEventTypeException exception = assertThrows(InvalidEventTypeException.class,
                () -> parkingEventService.parkedEvent(dto));


        assertEquals(ParkingEventServiceImpl.EVENT_TYPE_REQUIRED, exception.getMessage());


        verify(eventTypeService, never()).handleEntryEvent(TestConstants.SECTOR, TestConstants.LICENSE_PLATE, TestConstants.ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(TestConstants.LICENSE_PLATE, TestConstants.LAT, TestConstants.LNG);
        verify(eventTypeService, never()).handleExitEvent(TestConstants.LICENSE_PLATE, TestConstants.EXIT_TIME);
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

        WebhookEventDTO dto = createBaseDTO(TestConstants.EVENT_TYPE_ENTRY);


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleEntryEvent(TestConstants.SECTOR, TestConstants.LICENSE_PLATE, TestConstants.ENTRY_TIME);


        verify(eventTypeService, never()).handleParkedEvent(TestConstants.LICENSE_PLATE, TestConstants.LAT, TestConstants.LNG);
        verify(eventTypeService, never()).handleExitEvent(TestConstants.LICENSE_PLATE, TestConstants.EXIT_TIME);
    }

    @Test
    void parkedEvent_Success_ParkedEvent() {

        WebhookEventDTO dto = createBaseDTO(TestConstants.EVENT_TYPE_PARKED);


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleParkedEvent(TestConstants.LICENSE_PLATE, TestConstants.LAT, TestConstants.LNG);


        verify(eventTypeService, never()).handleEntryEvent(TestConstants.SECTOR, TestConstants.LICENSE_PLATE, TestConstants.ENTRY_TIME);
        verify(eventTypeService, never()).handleExitEvent(TestConstants.LICENSE_PLATE, TestConstants.EXIT_TIME);
    }

    @Test
    void parkedEvent_Success_ExitEvent() {

        WebhookEventDTO dto = createBaseDTO(TestConstants.EVENT_TYPE_EXIT);


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleExitEvent(TestConstants.LICENSE_PLATE, TestConstants.EXIT_TIME);


        verify(eventTypeService, never()).handleEntryEvent(TestConstants.SECTOR, TestConstants.LICENSE_PLATE, TestConstants.ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(TestConstants.LICENSE_PLATE, TestConstants.LAT, TestConstants.LNG);
    }

    @Test
    void parkedEvent_Success_HandlesLowercaseInput() {

        WebhookEventDTO dto = createBaseDTO("entry");


        parkingEventService.parkedEvent(dto);


        verify(eventTypeService).handleEntryEvent(TestConstants.SECTOR, TestConstants.LICENSE_PLATE, TestConstants.ENTRY_TIME);
        verify(eventTypeService, never()).handleParkedEvent(TestConstants.LICENSE_PLATE, TestConstants.LAT, TestConstants.LNG);
    }
}