package com.yk.bike.utils;

public class NullObjectUtils {
    public static boolean isEmptyString(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotEmptyString(String s) {
        return !(s == null || "".equals(s));
    }

    public static String emptyString(String s) {
        if (isEmptyString(s))
            return "";
        else
            return s;
    }
}
