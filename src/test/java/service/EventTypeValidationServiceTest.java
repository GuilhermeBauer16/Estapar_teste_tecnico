package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.constants.ValidationConstants;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEntryDayException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidExitTimeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeValidationService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.utils.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EventTypeValidationServiceTest {

    private EventTypeValidationService eventTypeValidationService;

    private static final String PLATE = "TEST-1234";
    private static final ZoneId GARAGE_ZONE = DateTimeUtils.GARAGE_ZONE;

    private static final OffsetDateTime MOCK_NOW =
            OffsetDateTime.of(2025, 1, 10, 15, 0, 0, 0, ZoneOffset.of("-03:00"));

    private static final OffsetDateTime ENTRY_TIME =
            OffsetDateTime.of(2025, 1, 10, 14, 0, 0, 0, ZoneOffset.of("-03:00"));

    @BeforeEach
    void setup() {
        eventTypeValidationService = new EventTypeValidationService();
    }


    @Test
    void shouldThrowInvalidEntryDayException_whenEntryDayIsDifferent() {
        try (MockedStatic<OffsetDateTime> mockedNow = Mockito.mockStatic(OffsetDateTime.class)) {

            mockedNow.when(() -> OffsetDateTime.now(GARAGE_ZONE)).thenReturn(MOCK_NOW);

            OffsetDateTime invalidEntry = MOCK_NOW.minusDays(1);

            InvalidEntryDayException ex = assertThrows(
                    InvalidEntryDayException.class,
                    () -> eventTypeValidationService.validateEntryDay(invalidEntry, PLATE)
            );

            String expectedMessage = String.format(
                    ValidationConstants.INVALID_ENTRY_DAY_MESSAGE,
                    invalidEntry.toLocalDate().format(DateTimeUtils.DATE_FORMATTER),
                    MOCK_NOW.toLocalDate().format(DateTimeUtils.DATE_FORMATTER),
                    PLATE
            );

            assertEquals(expectedMessage, ex.getMessage());
        }
    }

    @Test
    void shouldNotThrow_whenEntryDayIsValid() {
        try (MockedStatic<OffsetDateTime> mockedNow =
                     Mockito.mockStatic(OffsetDateTime.class)) {

            mockedNow.when(() -> OffsetDateTime.now(GARAGE_ZONE)).thenReturn(MOCK_NOW);

            OffsetDateTime entry = MOCK_NOW.minusHours(1);

            assertDoesNotThrow(() ->
                    eventTypeValidationService.validateEntryDay(entry, PLATE)
            );
        }
    }


    @Test
    void shouldThrowInvalidExitTimeException_whenExitTimeIsNull() {
        InvalidExitTimeException ex = assertThrows(
                InvalidExitTimeException.class,
                () -> eventTypeValidationService.validateExitTime(ENTRY_TIME, null, PLATE)
        );

        String expectedMessage = String.format(
                ValidationConstants.INVALID_EXIT_TIME_EXCEPTION_MESSAGE,
                PLATE,
                ValidationConstants.REASON_EXIT_TIME_NULL
        );

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldThrowInvalidExitTimeException_whenExitTimeIsBeforeEntry() {

        OffsetDateTime exitTime = ENTRY_TIME.minusMinutes(30);

        OffsetDateTime entryTimeLocal =
                ENTRY_TIME.atZoneSameInstant(GARAGE_ZONE).toOffsetDateTime();
        String formattedEntryTime = entryTimeLocal.format(DateTimeUtils.DATE_TIME_FORMATTER);

        InvalidExitTimeException ex = assertThrows(
                InvalidExitTimeException.class,
                () -> eventTypeValidationService.validateExitTime(ENTRY_TIME, exitTime, PLATE)
        );

        String expectedMessage = String.format(
                ValidationConstants.INVALID_EXIT_TIME_EXCEPTION_MESSAGE,
                PLATE,
                String.format(ValidationConstants.REASON_EXIT_TIME_BEFORE_ENTRY, formattedEntryTime)
        );

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldNotThrow_whenExitTimeIsAfterEntry() {
        OffsetDateTime exitTime = ENTRY_TIME.plusHours(2);

        assertDoesNotThrow(() ->
                eventTypeValidationService.validateExitTime(ENTRY_TIME, exitTime, PLATE)
        );
    }
}
