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

    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidLong(Long longNum) {
        return (longNum != null && longNum > 0L);
    }

    public static boolean isValidInteger(Integer intNum) {
        return (intNum != null && intNum > 0);
    }

    public static boolean isBoolean(String strBool) {
        return (strBool.equalsIgnoreCase("TRUE") || strBool.equalsIgnoreCase("FALSE"));
    }

    public static boolean isArrayEmpty(Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        } else {
            boolean empty = true;
            // check every object is it empty?
            for (Object object : objects) {
                if (object != null) {
                    empty = false;
                    break;
                }
            }

            return empty;
        }
    }
}
