package com.yk.bike.callback;

import com.yk.bike.response.BaseResponse;

public interface OnResponseListener<T extends BaseResponse> {
    void onStart();
    void onFinish();
    void onError(String errorMsg);
    void onSuccess(T t);
}
