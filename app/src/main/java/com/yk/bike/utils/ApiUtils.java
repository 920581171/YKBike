package com.yk.bike.utils;

import com.yk.bike.callback.CommonCallback;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.callback.OnResponseListener;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.CommonResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiUtils {
    private static ApiUtils instance;

    private ApiUtils() {
    }

    public static ApiUtils getInstance() {
        if (instance == null) {
            synchronized (ApiUtils.class) {
                if (instance == null) {
                    instance = new ApiUtils();
                }
            }
        }
        return instance;
    }

    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(5,TimeUnit.SECONDS).build();

    private void post(FormBody formBody, String url, CommonCallback callback) {
        Request request = new Request.Builder().url(UrlConsts.IPORT + url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private void get(String url, CommonCallback callback) {
        Request request = new Request.Builder().url(UrlConsts.IPORT + url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void registerUserByName(String name,String password, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", name)
                .add("userPassword",password)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_NAME, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    public void registerUserByPhone(String phone, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", phone)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_PHONE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    public void findUserByUserName(String userName,OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName",userName)
                .build();

        post(formBody,UrlConsts.POST_USER_INFO_FIND_USER_BY_NAME,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }

    public void findUserByUserPhone(String userPhone,OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", userPhone)
                .build();

        post(formBody,UrlConsts.POST_USER_INFO_FIND_USER_BY_PHONE,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }

    public void appLogin(String userName,String userPassword,OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName",userName)
                .add("userPassword",userPassword)
                .build();

        post(formBody,UrlConsts.POST_USER_INFO_UPDATE_APP_LOGIN,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }
}
