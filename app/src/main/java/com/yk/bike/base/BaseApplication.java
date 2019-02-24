package com.yk.bike.base;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.yk.bike.utils.BitmapCache;
import com.yk.bike.websocket.NotificationUtils;

public class BaseApplication extends Application {

    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MobSDK.init(this);
        BitmapCache.getInstance().init();
        NotificationUtils.getInstance().init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        application = null;
        BitmapCache.getInstance().relese();
        NotificationUtils.getInstance().relese();
    }

    public static BaseApplication getApplication() {
        return application;
    }
}
