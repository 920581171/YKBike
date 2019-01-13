package com.yk.bike.callback;

public interface OnErrorResponseListener<T> extends OnResponseListener<T> {
    void onError();
}
