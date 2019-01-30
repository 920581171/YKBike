package com.yk.bike.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.yk.bike.R;
import com.yk.bike.base.BaseApplication;

public class BitmapCache {
    private static BitmapCache instance;

    public static BitmapCache getInstance() {
        if (instance == null) {
            synchronized (BitmapCache.class) {
                if (instance == null) {
                    instance = new BitmapCache();
                }
            }
        }
        return instance;
    }

    private BitmapCache() {
    }

    private Bitmap locationOn;
    private Bitmap locationOff;
    private Bitmap locationUser;

    public void init() {
        locationOn = getBitmapByDrawable(R.drawable.ic_location_on);
        locationOff = getBitmapByDrawable(R.drawable.ic_location_off);
        locationUser = getBitmapByDrawable(R.drawable.ic_location_user);
    }

    public void relese() {
        locationOn = null;
        locationOff = null;
        locationUser = null;
    }

    public Bitmap getLocationOn() {
        return locationOn;
    }

    public Bitmap getLocationOff() {
        return locationOff;
    }

    public Bitmap getLocationUser() {
        return locationUser;
    }

    public static Bitmap getBitmapByDrawable(int drawableId) {
        Drawable vectorDrawable = BaseApplication.getApplication().getResources().getDrawable(drawableId, null);
        vectorDrawable.mutate();
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapByDrawableWithColor(int drawableId, int Color) {
        Drawable vectorDrawable = BaseApplication.getApplication().getResources().getDrawable(drawableId, null);
        vectorDrawable.mutate().setTint(Color);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        return bitmap;
    }
}
