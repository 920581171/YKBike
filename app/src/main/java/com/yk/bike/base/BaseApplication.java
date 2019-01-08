package com.yk.bike.base;

import android.app.Application;

import com.mob.MobSDK;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }
}
