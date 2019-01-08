package com.yk.bike.callback;

import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.MainHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonCallback<T> implements Callback {

    private OnResponseListener<T> onResponseListener;
    private Class<T> tClass;

    public CommonCallback(OnResponseListener<T> onResponseListener, Class<T> tClass) {
        this.onResponseListener = onResponseListener;
        this.tClass = tClass;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        MainHandler.getInstance().post(() -> {
            if (onResponseListener != null)
                onResponseListener.onError();
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        final T t;
        if (response.body() != null) {
            t = (T) GsonUtils.fromJson(response.body().string(), tClass);
        } else {
            t = null;
        }
        MainHandler.getInstance().post(() -> {
            if (onResponseListener != null) {
                onResponseListener.onResponse(t);
            }
        });
    }
}
