package com.yk.bike.utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class APIUtils {
    private static APIUtils instance;

    private APIUtils() {
    }

    public static APIUtils getInstance() {
        if (instance == null) {
            synchronized (APIUtils.class) {
                if (instance == null) {
                    instance = new APIUtils();
                }
            }
        }
        return instance;
    }

    private OkHttpClient okHttpClient = new OkHttpClient();

    private void post(FormBody formBody, String url, Callback callback){
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private void get(String url,Callback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
