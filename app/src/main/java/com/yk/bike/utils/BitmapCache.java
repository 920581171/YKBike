package com.yk.bike.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.yk.bike.R;
import com.yk.bike.base.BaseApplication;
import com.yk.bike.constant.UrlConsts;

import java.util.concurrent.ExecutionException;

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

    public static void getAvatar(int drawableId, String loginId, ImageView avatar) {
        Glide.with(BaseApplication.getApplication())
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(drawableId)
                        //禁用内存缓存
                        .skipMemoryCache(true)
                        //硬盘缓存功能
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(UrlConsts.HEADIPORT + UrlConsts.GET_COMMON_DOWNLOAD_AVATAR + "?id=" + loginId)
                .into(avatar);
    }

    public static void getAvatar(int drawableId, String loginId,RequestListener<Bitmap> listener) {
        Glide.with(BaseApplication.getApplication())
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(drawableId)
                        //禁用内存缓存
                        .skipMemoryCache(true)
                        //硬盘缓存功能
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .asBitmap()
                .listener(listener)
                .load(UrlConsts.HEADIPORT + UrlConsts.GET_COMMON_DOWNLOAD_AVATAR + "?id=" + loginId)
                .submit();
    }
}
