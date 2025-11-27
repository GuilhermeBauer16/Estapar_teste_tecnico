package com.github.GuilhermeBauer16.EstaparTesteTecnico.constants;// package com.github.GuilhermeBauer16.EstaparTesteTecnico.constants;

public final class ValidationConstants {
    private ValidationConstants() {}


    public static final String GARAGE_FULL_EXCEPTION_MESSAGE = "Have nothing available places to parking in this garage at the moment, please try again.";

    public static final String INVALID_EXIT_TIME_EXCEPTION_MESSAGE = "Invalid exit time for license plate %s: %s.";
    public static final String REASON_EXIT_TIME_NULL = "cannot be null";
    public static final String REASON_EXIT_TIME_BEFORE_ENTRY = "cannot be before the entry time (%s)";

    public static final String INVALID_ENTRY_DAY_MESSAGE = "The entry date (%s) must be the current date (%s) for license plate %s.";

}