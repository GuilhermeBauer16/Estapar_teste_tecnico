package com.github.GuilhermeBauer16.EstaparTesteTecnico.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    private DateTimeUtils() {}

    public static final ZoneId GARAGE_ZONE = ZoneId.of("America/Sao_Paulo");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}