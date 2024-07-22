package com.kelvin.ms_app.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Common {

    public static String Null2String(Object any) {
        if (any == null) {
            return "";
        } else {
            return any.toString().trim();
        }
    }

    public LocalDate convertToLocalDateInTimeZone(String isoDate, String timeZone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDate);
        ZonedDateTime adjustedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone));
        return adjustedDateTime.toLocalDate();
    }
}
