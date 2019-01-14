package com.yk.bike.base;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

public class BaseApplication extends Application {

    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MobSDK.init(this);
    }

    public static BaseApplication getApplication() {
        return application;
    }
}
