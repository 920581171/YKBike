package com.yk.bike.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.MessageBroadResponse;
import com.yk.bike.utils.NullObjectUtils;

import java.util.List;

public class MessageBroadAdapter extends BaseAdapter<MessageBroadResponse.MessageBroad> {

    public MessageBroadAdapter(List<MessageBroadResponse.MessageBroad> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_meessage_broad;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0)
            return;

        String status = list.get(i).getMessageStatus().equals("0") ? "待回复" : "已回复：" + list.get(i).getHandlerName();
        int color = list.get(i).getMessageStatus().equals("0") ? Color.RED : Color.BLACK;

        baseViewHolder.getTextView(R.id.tv_message_id).setText(list.get(i).getMessageId());
        baseViewHolder.getTextView(R.id.tv_status).setText(status);
        baseViewHolder.getTextView(R.id.tv_status).setTextColor(color);
        baseViewHolder.getTextView(R.id.tv_content).setText(list.get(i).getMessageContent());
        baseViewHolder.getTextView(R.id.tv_reply).setText(NullObjectUtils.emptyString(list.get(i).getMessageReply()));

        baseViewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(v, baseViewHolder, i);
        });
    }
}
