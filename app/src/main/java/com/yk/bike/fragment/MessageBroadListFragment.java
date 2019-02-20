package com.yk.bike.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.activity.MainActivity;
import com.yk.bike.activity.MessageBroadActivity;
import com.yk.bike.adapter.ItemClickListener;
import com.yk.bike.adapter.MessageBroadAdapter;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.MessageBroadListResponse;
import com.yk.bike.response.MessageBroadResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.List;

public class MessageBroadListFragment extends BaseRecyclerFragment<MainActivity> {
    @Override
    public void initData() {
        if (SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN)) {
            initHandlerList();
        } else {
            initSenderList();
        }
    }

    public void initSenderList() {
        ApiUtils.getInstance().findMessageBroadBySenderId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<MessageBroadListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(MessageBroadListResponse messageBroadListResponse) {
                if (isResponseSuccess(messageBroadListResponse)) {
                    MessageBroadAdapter adapter = new MessageBroadAdapter(messageBroadListResponse.getData());
                    onDataChange(adapter);
                    adapter.setOnItemClickListener(new ItemClickListener<MessageBroadResponse.MessageBroad>() {
                    });
                } else {
                    showShort(messageBroadListResponse.getMsg());
                }
            }
        });
    }

    public void initHandlerList() {
        ApiUtils.getInstance().findMessageBroadByType(new ResponseListener<MessageBroadListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(MessageBroadListResponse messageBroadListResponse) {
                if (isResponseSuccess(messageBroadListResponse)) {
                    List<MessageBroadResponse.MessageBroad> messageBroads = messageBroadListResponse.getData();
                    MessageBroadAdapter adapter = new MessageBroadAdapter(messageBroads);
                    onDataChange(adapter);
                    adapter.setOnItemClickListener(new ItemClickListener<MessageBroadResponse.MessageBroad>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            if (messageBroads.get(position).getMessageType().equals("0"))
                                startActivity(new Intent(getActivityContext(), MessageBroadActivity.class).putExtra(Consts.INTENT_STRING_MESSAGE_ID, messageBroads.get(position).getMessageId()));
                        }
                    });
                } else {
                    showShort(messageBroadListResponse.getMsg());
                }
            }
        });
    }
}
