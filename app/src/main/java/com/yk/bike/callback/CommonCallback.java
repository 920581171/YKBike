package com.yk.bike.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.MainHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonCallback<T> implements Callback {

    private static final String TAG = "CommonCallback";

    private OnResponseListener<T> onResponseListener;
    private Class<T> tClass;

    public CommonCallback(OnResponseListener<T> onResponseListener, Class<T> tClass) {
        this.onResponseListener = onResponseListener;
        this.tClass = tClass;
        if (onResponseListener instanceof OnCommonResponseListener) {
            ((OnCommonResponseListener<T>) onResponseListener).onStart();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onFailure: ");
        MainHandler.getInstance().post(() -> {
            if (onResponseListener instanceof OnCommonResponseListener) {
                ((OnCommonResponseListener<T>) onResponseListener).onError();
                ((OnCommonResponseListener<T>) onResponseListener).onFinish();
            } else if (onResponseListener instanceof OnBaseResponseListener) {
                ((OnBaseResponseListener<T>) onResponseListener).onError();
            } else if (onResponseListener instanceof OnErrorResponseListener) {
                ((OnErrorResponseListener<T>) onResponseListener).onError();
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        T t;
        if (response.body() != null) {
            String responseStr = response.body().string();
            Log.d(TAG, "onResponse: "+ responseStr);
//            t = (T) GsonUtils.fromJson(responseStr, tClass);
            Gson gson = new Gson();
            t = gson.fromJson(responseStr,tClass);
        } else {
            Log.d(TAG, "onResponse: null");
            t = null;
        }
        MainHandler.getInstance().post(() -> {
            if (onResponseListener instanceof OnCommonResponseListener) {
                ((OnCommonResponseListener<T>) onResponseListener).onResponse(t);
                ((OnCommonResponseListener<T>) onResponseListener).onFinish();
            } else if (onResponseListener instanceof OnBaseResponseListener) {
                ((OnBaseResponseListener<T>) onResponseListener).onResponse(t);
            } else if (onResponseListener instanceof OnSuccessResponseListener) {
                ((OnSuccessResponseListener<T>) onResponseListener).onResponse(t);
            }
        });
    }
}
