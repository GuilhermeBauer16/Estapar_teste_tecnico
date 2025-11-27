package constants;

import java.time.LocalTime;
import java.time.OffsetDateTime;

public final class TestConstants {

    public static final Double DYNAMIC_PRICE_MULTIPLIER = 1.0;
    public static final OffsetDateTime ENTRY_TIME = OffsetDateTime.now().minusHours(1);
    public static final Long ID = 1L;
    public static final double LAT = -23.5505;
    public static final double LNG = -46.6333;
    public static final Boolean IS_OCCUPIED_FALSE = false;
    public static final Boolean IS_OCCUPIED_TRUE = true;
    public static final String LICENSE_PLATE = "ABC-1234";
    public static final String EVENT_TYPE_ENTRY = "ENTRY";
    public static final String EVENT_TYPE_PARKED = "PARKED";
    public static final String EVENT_TYPE_EXIT = "EXIT";


    public static final String SECTOR = "A1";
    public static final Double BASE_PRICE = 10.0;
    public static final int MAX_CAPACITY = 100;
    public static final Integer CURRENT_OCCUPANCY = 50;
    public static final LocalTime OPEN_HOUR = LocalTime.of(8, 0);
    public static final LocalTime CLOSE_HOUR = LocalTime.of(20, 0);

    public static final OffsetDateTime EXIT_TIME = ENTRY_TIME.plusHours(2);

}
