package com.github.GuilhermeBauer16.EstaparTesteTecnico.service.contract;

import java.time.OffsetDateTime;

public interface EventTypeValidationServiceContract {

    void validateEntryDay(OffsetDateTime entryTime, String licensePlate);

    void validateExitTime(OffsetDateTime entryTime, OffsetDateTime exitTime, String licensePlate);
}
