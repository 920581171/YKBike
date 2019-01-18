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

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    private LocationBinder locationBinder;

    private AMapLocation aMapLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        locationBinder = new LocationBinder();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationListener = aMapLocation -> {
            this.aMapLocation = aMapLocation;
        };
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (locationBinder==null)
            locationBinder=new LocationBinder();
        return locationBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mLocationClient.onDestroy();
    }

    public class LocationBinder extends Binder {
        public AMapLocation getAMapLocation(){
            return LocationService.this.aMapLocation;
        }
    }
}
