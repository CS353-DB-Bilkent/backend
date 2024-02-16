package com.ticketseller.backend.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Mail {
    public static final int BATCH_SIZE = 5;
    public static final int BATCH_SLEEP_IN_MS = (Time.MINUTE_IN_SECONDS + 1) * Time.SECOND_IN_MS;
}
