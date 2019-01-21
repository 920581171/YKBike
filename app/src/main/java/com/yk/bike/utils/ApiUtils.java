package com.yk.bike.utils;

import com.yk.bike.callback.CommonCallback;
import com.yk.bike.callback.OnResponseListener;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.UserInfoResponse;

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

    public void registerUserByName(String name,String password, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", name)
                .add("userPassword",password)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_NAME, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    public void registerUserByPhone(String phone, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", phone)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_PHONE, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
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

    public void appLogin(String userName,String userPassword,OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName",userName)
                .add("userPassword",userPassword)
                .build();

        post(formBody,UrlConsts.POST_USER_INFO_UPDATE_APP_LOGIN,new CommonCallback<>(onResponseListener,UserInfoResponse.class));
    }

    public void appAdminLogin(String adminNameOrPhone,String adminPassword,OnResponseListener<AdminInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("adminNameOrPhone",adminNameOrPhone)
                .add("adminPassword",adminPassword)
                .build();

        post(formBody,UrlConsts.POST_ADMIN_INFO_APP_ADMIN_LOGIN,new CommonCallback<>(onResponseListener,AdminInfoResponse.class));
    }

    public void findBikeByBikeId(String bikeId, OnResponseListener<BikeInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId",bikeId)
                .build();

        post(formBody,UrlConsts.POST_BIKE_INFO_FIND_BIKE_BY_BIKE_ID,new CommonCallback<>(onResponseListener,BikeInfoResponse.class));
    }

    public void addBikeInfo(String bikeId,double latitude,double longitude,OnResponseListener<CommonResponse> onResponseListener){
        FormBody formBody = new FormBody.Builder()
                .add("bikeId",bikeId)
                .add("latitude",String.valueOf(latitude))
                .add("longitude",String.valueOf(longitude))
                .build();

        post(formBody,UrlConsts.POST_BIKE_INFO_ADD_BIKE_INFO,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }

    public void updateBikeInfo(BikeInfoResponse.BikeInfo bikeInfo, OnResponseListener<CommonResponse> onResponseListener){
        FormBody formBody = new FormBody.Builder()
                .add("bikeId",bikeInfo.getBikeId())
                .add("userId",bikeInfo.getUserId())
                .add("latitude",String.valueOf(bikeInfo.getLatitude()))
                .add("longitude",String.valueOf(bikeInfo.getLongitude()))
                .add("mileage",String.valueOf(bikeInfo.getMileage()))
                .add("fix",String.valueOf(bikeInfo.getFix()))
                .build();

        post(formBody,UrlConsts.POST_BIKE_INFO_UPDATE_BIKE_INFO,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }

    public void findAllBikeInfo(OnResponseListener<BikeInfoListResponse> onResponseListener){
        FormBody formBody = new FormBody.Builder().build();
        post(formBody,UrlConsts.POST_BIKE_INFO_ALL_BIKE_INFO,new CommonCallback<>(onResponseListener,BikeInfoListResponse.class));
    }

    public void deleteBikeInfo(String bikeId,OnResponseListener<CommonResponse> onResponseListener){
        FormBody formBody = new FormBody.Builder()
                .add("bikeId",bikeId)
                .build();

        post(formBody,UrlConsts.POST_BIKE_INFO_DELETE_BIKE_INFO,new CommonCallback<>(onResponseListener,CommonResponse.class));
    }
}
