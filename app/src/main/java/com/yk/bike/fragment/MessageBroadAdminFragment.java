package com.yk.bike.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.activity.MessageBroadActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.MessageBroadResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.Objects;

public class MessageBroadAdminFragment extends BaseFragment<MessageBroadActivity> implements View.OnClickListener {

    private TextView tvMessageContent;
    private TextInputEditText etAdviceHelp;
    private MessageBroadResponse.MessageBroad messageBroad;
    private AdminInfoResponse.AdminInfo adminInfo;

    @Override
    public int initLayout() {
        return R.layout.fragment_message_broad_handler;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        tvMessageContent = rootView.findViewById(R.id.tv_message_content);
        etAdviceHelp = rootView.findViewById(R.id.et_advice_help);
        TextView tvContact = rootView.findViewById(R.id.tv_contact);
        TextView tvReply = rootView.findViewById(R.id.tv_reply);

        tvContact.setOnClickListener(this);
        tvReply.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findMessageBroadByMessageId(messageBroad.getMessageId(), new ResponseListener<MessageBroadResponse>() {
            @Override
            public void onSuccess(MessageBroadResponse messageBroadResponse) {
                if (isResponseSuccess(messageBroadResponse)) {
                    messageBroad = messageBroadResponse.getData();
                    tvMessageContent.setText(messageBroad.getMessageContent());
                } else {
                    showShort(messageBroadResponse.getMsg());
                }
            }
        });

        ApiUtils.getInstance().findAdminByAdminId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<AdminInfoResponse>() {
            @Override
            public void onSuccess(AdminInfoResponse adminInfoResponse) {
                if (isResponseSuccess(adminInfoResponse)) {
                    adminInfo = adminInfoResponse.getData();
                } else {
                    showShort(adminInfoResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_content:
                break;
            case R.id.tv_reply:
                updateMessageBroad();
                break;
        }
    }

    public void updateMessageBroad() {
        if (messageBroad == null) {
            showShort("获取留言内容失败");
            return;
        }

        messageBroad.setHandlerId(adminInfo.getAdminId());
        messageBroad.setHandlerName(adminInfo.getAdminName());
        messageBroad.setMessageReply(Objects.requireNonNull(etAdviceHelp.getText()).toString());
        messageBroad.setMessageStatus("1");
        ApiUtils.getInstance().updateMessageBroad(messageBroad, new ResponseListener<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse commonResponse) {
                if (isResponseSuccess(commonResponse)) {
                    showShort("回复成功");
                    getActivityContext().finish();
                } else {
                    showShort(commonResponse.getMsg());
                }
            }
        });
    }

    public MessageBroadAdminFragment setMessageId(String messageId) {
        this.messageBroad = new MessageBroadResponse.MessageBroad().setMessageId(messageId);
        return this;
    }
}
