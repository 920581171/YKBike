package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.ChatRoomListResponse;

import java.util.List;

public class ChatRoomAdapter extends BaseAdapter<ChatRoomListResponse.ChatRoom> {

    public ChatRoomAdapter(List<ChatRoomListResponse.ChatRoom> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_chat_room;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0) {
            return;
        }

        baseViewHolder.getTextView(R.id.tv_id).setText(list.get(i).getSecondId());
        baseViewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, baseViewHolder, i);
            }
        });
    }
}
