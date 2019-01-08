package com.yk.bike.callback;

import okhttp3.Response;

public interface OnResponseListener<T> {
    void onError();
    void onResponse(T t);
}
