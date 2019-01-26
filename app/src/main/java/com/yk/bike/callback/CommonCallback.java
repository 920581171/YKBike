package com.yk.bike.callback;

import android.support.annotation.NonNull;
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
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Log.d(TAG, "onFailure: ");
        MainHandler.getInstance().post(() -> {
            onError("网络错误");
        });
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull final Response response) {
        try {
            T t;
            if (response.body() != null) {
                String responseStr = response.body().string();
                Log.d(TAG, "onResponse: " + responseStr);
                t = GsonUtils.fromJson(responseStr, tClass);
            } else {
                Log.d(TAG, "onResponse: null");
                t = null;
            }
            MainHandler.getInstance().post(() -> {
                if (onResponseListener instanceof OnCommonResponseListener) {
                    ((OnCommonResponseListener<T>) onResponseListener).onSuccess(t);
                    ((OnCommonResponseListener<T>) onResponseListener).onFinish();
                } else if (onResponseListener instanceof OnBaseResponseListener) {
                    ((OnBaseResponseListener<T>) onResponseListener).onSuccess(t);
                } else if (onResponseListener instanceof OnSuccessResponseListener) {
                    ((OnSuccessResponseListener<T>) onResponseListener).onSuccess(t);
                }
            });
        } catch (IOException|IllegalStateException e) {
            onError("数据解析错误");
            e.printStackTrace();
        }
    }

    private void onError(String errorMsg){
        if (onResponseListener instanceof OnCommonResponseListener) {
            ((OnCommonResponseListener<T>) onResponseListener).onError(errorMsg);
            ((OnCommonResponseListener<T>) onResponseListener).onFinish();
        } else if (onResponseListener instanceof OnBaseResponseListener) {
            ((OnBaseResponseListener<T>) onResponseListener).onError(errorMsg);
        } else if (onResponseListener instanceof OnErrorResponseListener) {
            ((OnErrorResponseListener<T>) onResponseListener).onError(errorMsg);
        }
    }
}
