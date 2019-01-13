package com.yk.bike.callback;

public interface OnCommonResponseListener<T> extends OnBaseResponseListener<T>{
    void onStart();
    void onFinish();
}
