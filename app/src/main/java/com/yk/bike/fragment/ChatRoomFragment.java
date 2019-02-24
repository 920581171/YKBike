package com.yk.bike.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.activity.ChatActivity;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.ChatRoomAdapter;
import com.yk.bike.adapter.ItemClickListener;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.ChatRoomListResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SpUtils;

import java.util.List;

public class ChatRoomFragment extends BaseRecyclerFragment<MainActivity> {
    @Override
    public void initData() {
        ApiUtils.getInstance().findALLChatRoomByFristId(SpUtils.getLoginId(), new ResponseListener<ChatRoomListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(ChatRoomListResponse chatRoomListResponse) {
                if (isResponseSuccess(chatRoomListResponse)) {
                    List<ChatRoomListResponse.ChatRoom> chatRooms = chatRoomListResponse.getData();
                    ChatRoomAdapter adapter = new ChatRoomAdapter(chatRooms);
                    onDataChange(adapter);
                    adapter.setOnItemClickListener(new ItemClickListener<ChatRoomListResponse.ChatRoom>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            startActivity(new Intent(getActivityContext(), ChatActivity.class).putExtra(Consts.INTENT_STRING_TO_ID, chatRooms.get(position).getSecondId()));
                        }
                    });
                } else {
                    showShort(chatRoomListResponse.getMsg());
                }
            }
        });
    }
}
