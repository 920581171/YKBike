package com.yk.bike.callback;

public interface OnResponseListener<T> {
    void onStart();
    void onFinish();
    void onError(String errorMsg);
    void onSuccess(T t);
}
