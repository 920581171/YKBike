package com.yk.bike.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtils {
    private static Gson gson;

    private GsonUtils() {
    }

    private static Gson getGson() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static <T>T fromJson(String string,Type type) throws IllegalStateException{
        return getGson().fromJson(string,type);
    }
}
