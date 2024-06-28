package com.kelvin.ms_app.util;

public class Common {

    public static String Null2String(Object any) {
        if (any == null) {
            return "";
        } else {
            return any.toString().trim();
        }
    }
}
