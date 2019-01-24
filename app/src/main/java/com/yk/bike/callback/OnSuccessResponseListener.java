package com.yk.bike.callback;

public interface OnSuccessResponseListener<T> extends OnResponseListener<T> {
    void onSuccess(T t);
}
