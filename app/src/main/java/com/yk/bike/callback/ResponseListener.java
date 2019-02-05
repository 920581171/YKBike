package com.yk.bike.callback;

import com.yk.bike.base.BaseFragment;
import com.yk.bike.response.BaseResponse;
import com.yk.bike.response.CommonResponse;

public class ResponseListener<T extends BaseResponse> implements OnResponseListener<T>{
    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onSuccess(T t) {

    }
}
