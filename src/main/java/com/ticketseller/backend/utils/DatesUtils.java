package com.ticketseller.backend.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class DatesUtils {

    public static LocalDateTime addDaysAndGetDeadline(int days) {
        // East Africa zone, GMT+3 (Same as Turkey)
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("EAT")));

        localDateTime = localDateTime.plusDays(days);
        localDateTime = localDateTime.withHour(23);
        localDateTime = localDateTime.withMinute(59);
        localDateTime = localDateTime.withSecond(59);

        return localDateTime;
    }

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("EAT")));
    }

    public static String getStringLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }


}
