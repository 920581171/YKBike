package com.yk.bike.websocket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.yk.bike.R;
import com.yk.bike.activity.ChatActivity;
import com.yk.bike.base.BaseApplication;
import com.yk.bike.constant.Consts;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {

    private static NotificationUtils instance;

    private NotificationUtils() {
    }

    public static NotificationUtils getInstance() {
        if (instance == null) {
            synchronized (NotificationUtils.class) {
                if (instance == null) {
                    instance = new NotificationUtils();
                }
            }
        }
        return instance;
    }

    public void init() {
        //获取一个Notification构造器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chatChannel = new NotificationChannel(Consts.CHANNEL_ID_CHAT, Consts.CHANNEL_NAME_CHAT, NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel otherChannel = new NotificationChannel(Consts.CHANNEL_ID_OTHER, Consts.CHANNEL_NAME_OTHER, NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = (NotificationManager) BaseApplication.getApplication().getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chatChannel);
            manager.createNotificationChannel(otherChannel);
        }
    }

    public void relese() {
        instance = null;
    }

    public void showChatMessage(String toId, String content) {
        Notification.Builder builder = new Notification.Builder(BaseApplication.getApplication());

        Intent nfIntent = new Intent(BaseApplication.getApplication(), ChatActivity.class).putExtra(Consts.INTENT_STRING_TO_ID, toId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(Consts.CHANNEL_ID_CHAT);

        Icon icon = Icon.createWithResource(BaseApplication.getApplication(), R.drawable.ic_message);

        builder.setContentIntent(PendingIntent.getActivity(BaseApplication.getApplication(), 0, nfIntent, PendingIntent.FLAG_UPDATE_CURRENT)) // 设置PendingIntent
                .setLargeIcon(icon) // 设置下拉列表中的图标(大图标)
                .setContentTitle("你有一条新消息") // 设置下拉列表里的标题
                .setSmallIcon(icon) // 设置状态栏内的小图标
                .setContentText(content)// 设置上下文内容
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setPriority(Notification.PRIORITY_HIGH)//设置该通知优先级
                .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) BaseApplication.getApplication().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Consts.NOTIFICATION_ID_CHAT, builder.build());
    }
}
