package com.yk.bike.service;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.NullObjectUtils;

import java.util.LinkedList;

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    public static final int COUNT_DOWN = 100;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    private LocationBinder locationBinder;

    private AMapLocation aMapLocation;

    private long serviceTime = 0;
    private long t = 0;

    private int count = COUNT_DOWN;

    private LinkedList<OnServiceTimeListener> onServiceTimeListeners = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        locationBinder = new LocationBinder();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        //在监听的同时计算服务器时间
        mLocationListener = aMapLocation -> {
            this.aMapLocation = aMapLocation;
            serviceTime += System.currentTimeMillis() - this.t;
            this.t = System.currentTimeMillis();
            count--;
            //每隔一段时间同步一次服务器时间
            if (count <= 0) {
                count = COUNT_DOWN;
                initServiceTime(false);
            }
            for (OnServiceTimeListener listener : onServiceTimeListeners)
                listener.onServiceTime(serviceTime);

            Log.d(TAG, "mLocationListener: " + serviceTime);
        };

        initServiceTime(true);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport)
                .setInterval(1000);

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    /**
     * 获取服务器时间
     *
     * @param isFrist 是否为初始化
     */
    public void initServiceTime(boolean isFrist) {
        ApiUtils.getInstance().getServiceTime(new ResponseListener<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse commonResponse) {
                if (NullObjectUtils.isResponseSuccess(commonResponse)) {
                    serviceTime = Long.parseLong((String) commonResponse.getData());
                    t = System.currentTimeMillis();
                    count = 60;
                    if (isFrist)
                        mLocationClient.setLocationListener(mLocationListener);
                    Log.d(TAG, "onSuccess: initServiceTime");
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (locationBinder == null)
            locationBinder = new LocationBinder();
        return locationBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mLocationClient.onDestroy();
    }

    public class LocationBinder extends Binder {
        public AMapLocation getAMapLocation() {
            return LocationService.this.aMapLocation;
        }

        public void addOnServiceTimeListener(OnServiceTimeListener onServiceTimeListener) {
            LocationService.this.onServiceTimeListeners.add(onServiceTimeListener);
        }

        public void removeOnServiceTimeListener(OnServiceTimeListener onServiceTimeListener) {
            LocationService.this.onServiceTimeListeners.remove(onServiceTimeListener);
        }
    }

    public interface OnServiceTimeListener {
        void onServiceTime(long serviceTime);
    }
}
