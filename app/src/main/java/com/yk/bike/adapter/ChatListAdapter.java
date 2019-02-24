package com.yk.bike.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.utils.BitmapCache;
import com.yk.bike.utils.SpUtils;
import com.yk.bike.websocket.ChatMessage;

import java.util.List;

public class ChatListAdapter extends BaseAdapter<ChatMessage> {

    private Bitmap fromBitmap;
    private Bitmap toBitmap;

    public ChatListAdapter(List<ChatMessage> list, String fromId) {
        super(list);
        new Thread(() -> {
            int toDrawableId = SpUtils.isLoginAdmin() ? R.drawable.admin : R.drawable.avatar;
            int fromDrawableId = !SpUtils.isLoginAdmin() ? R.drawable.admin : R.drawable.avatar;
            BitmapCache.getAvatar(fromDrawableId, fromId, new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    fromBitmap = resource;
                    notifyDataSetChanged();
                    return false;
                }
            });
            BitmapCache.getAvatar(toDrawableId, SpUtils.getLoginId(), new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    toBitmap = resource;
                    notifyDataSetChanged();
                    return false;
                }
            });
            notifyDataSetChanged();
        }).start();
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_chat;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0) {
            return;
        }

        if (list.get(i).getFromId().equals(SpUtils.getLoginId())) {
            baseViewHolder.getView(R.id.ctl_from).setVisibility(View.GONE);
            baseViewHolder.getTextView(R.id.tv_to).setText(list.get(i).getChatContent());
            baseViewHolder.getImageView(R.id.cv_to).setImageBitmap(toBitmap);
        } else {
            baseViewHolder.getView(R.id.ctl_to).setVisibility(View.GONE);
            baseViewHolder.getTextView(R.id.tv_from).setText(list.get(i).getChatContent());
            baseViewHolder.getImageView(R.id.cv_from).setImageBitmap(fromBitmap);
        }
    }
}
