package com.yk.bike.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.adapter.ChatListAdapter;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.ChatMessageListResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.SpUtils;
import com.yk.bike.websocket.ChatMessage;
import com.yk.bike.websocket.WebSocketManager;
import com.yk.bike.websocket.WebSocketMessage;

import java.util.Date;
import java.util.Objects;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChatActivity";

    public static boolean isFront = false;

    private TextInputEditText etSendContent;
    private RecyclerView recyclerView;
    private String toId;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Consts.BR_ACTION_CHAT.equals(intent.getAction())) {
                notifyData(GsonUtils.fromJson(intent.getStringExtra(Consts.INTENT_STRING_CHAT), ChatMessage.class));
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.BR_ACTION_CHAT);
        registerReceiver(broadcastReceiver, intentFilter);

        initView();

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFront = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFront = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFront = false;
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public void initView() {
        toId = getIntent().getStringExtra(Consts.INTENT_STRING_TO_ID);

        ImageView ivImage = findViewById(R.id.iv_image);
        etSendContent = findViewById(R.id.et_send_content);
        TextView tvSend = findViewById(R.id.tv_send);
        recyclerView = findViewById(R.id.recyclerView);

        ivImage.setOnClickListener(this);
        tvSend.setOnClickListener(this);
    }

    public void notifyData(ChatMessage chatMessage) {
        ChatListAdapter adapter = (ChatListAdapter) recyclerView.getAdapter();
        adapter.getList().add(chatMessage);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void initData() {
        ApiUtils.getInstance().findALLChatMessageByBothId(SpUtils.getLoginId(), toId, new ResponseListener<ChatMessageListResponse>() {
            @Override
            public void onSuccess(ChatMessageListResponse chatMessageListResponse) {
                if (isResponseSuccess(chatMessageListResponse)) {
                    ChatListAdapter adapter = new ChatListAdapter(chatMessageListResponse.getData(), toId);
                    adapter.notifyDataSetChanged();
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                } else {
                    showShort(chatMessageListResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                break;
            case R.id.tv_send:
                ChatMessage chatMessage = new ChatMessage()
                        .setChatContent(Objects.requireNonNull(etSendContent.getText()).toString())
                        .setFromId(SpUtils.getLoginId())
                        .setToId(toId)
                        .setChatType(Consts.CHAT_TYPE_TEXT)
                        .setSendTime(new Date(System.currentTimeMillis()));
                WebSocketMessage webSocketMessage = new WebSocketMessage()
                        .setData(GsonUtils.toJson(chatMessage))
                        .setType(Consts.WEBSOCKET_TYPE_CHAT);
                WebSocketManager.getInstance().sendText(GsonUtils.toJson(webSocketMessage));
                etSendContent.setText("");
                notifyData(chatMessage);
                break;
        }
    }
}
