package com.github.GuilhermeBauer16.EstaparTesteTecnico.service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.constants.ValidationConstants;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidEntryDayException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidExitTimeException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract.EventTypeValidationServiceContract;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class EventTypeValidationService implements EventTypeValidationServiceContract {

    @Override
    public void validateEntryDay(OffsetDateTime entryTime, String licensePlate) {

        LocalDate today = OffsetDateTime.now(DateTimeUtils.GARAGE_ZONE).toLocalDate();
        LocalDate entryDate = entryTime.toLocalDate();

        if (!entryDate.isEqual(today)) {
            String formattedEntryDate = entryDate.format(DateTimeUtils.DATE_FORMATTER);
            String formattedToday = today.format(DateTimeUtils.DATE_FORMATTER);

            throw new InvalidEntryDayException(
                    String.format(ValidationConstants.INVALID_ENTRY_DAY_MESSAGE,
                            formattedEntryDate,
                            formattedToday,
                            licensePlate)
            );
        }
    }


    @Override
    public void validateExitTime(OffsetDateTime entryTime, OffsetDateTime exitTime, String licensePlate) {
        if (exitTime == null || exitTime.isBefore(entryTime)) {

            OffsetDateTime entryTimeLocal = entryTime.atZoneSameInstant(DateTimeUtils.GARAGE_ZONE).toOffsetDateTime();
            String formattedEntryTime = entryTimeLocal.format(DateTimeUtils.DATE_TIME_FORMATTER);

            String reason = exitTime == null
                    ? ValidationConstants.REASON_EXIT_TIME_NULL
                    : String.format(ValidationConstants.REASON_EXIT_TIME_BEFORE_ENTRY, formattedEntryTime);

            throw new InvalidExitTimeException(
                    String.format(ValidationConstants.INVALID_EXIT_TIME_EXCEPTION_MESSAGE, licensePlate, reason)
            );
        }
    }
}
