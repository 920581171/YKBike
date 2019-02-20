package com.yk.bike.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.activity.MessageBroadActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnResponseListener;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.MessageBroadListResponse;
import com.yk.bike.response.MessageBroadResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.Objects;

public class MessageBroadUserFragment extends BaseFragment<MessageBroadActivity> implements View.OnClickListener {

    private TextView tvAdvice;
    private TextView tvHelp;
    private TextInputEditText etAdviceHelp;

    @Override
    public int initLayout() {
        return R.layout.fragment_message_broad_sender;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        tvAdvice = rootView.findViewById(R.id.tv_advice);
        tvHelp = rootView.findViewById(R.id.tv_help);
        etAdviceHelp = rootView.findViewById(R.id.et_advice_help);
        TextView tvSubmit = rootView.findViewById(R.id.tv_submit);

        tvAdvice.setSelected(true);

        tvAdvice.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    public void submit() {
        String messageContent = Objects.requireNonNull(etAdviceHelp.getText()).toString();
        String messageType = tvAdvice.isSelected()?"0":"1";
        MessageBroadResponse.MessageBroad messageBroad = new MessageBroadResponse.MessageBroad()
                .setSenderId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID))
                .setMessageContent(messageContent)
                .setMessageType(messageType);
        ApiUtils.getInstance().addMessageBroad(messageBroad,new ResponseListener<CommonResponse>(){
            @Override
            public void onSuccess(CommonResponse CommonResponse) {
                super.onSuccess(CommonResponse);
                if (isResponseSuccess(CommonResponse)){
                    showShort("提交成功");
                    getActivityContext().finish();
                }else{
                    showShort(CommonResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_advice:
                v.setSelected(true);
                tvHelp.setSelected(false);
                etAdviceHelp.setHint(getString(R.string.string_hint_advice));
                break;
            case R.id.tv_help:
                v.setSelected(true);
                tvAdvice.setSelected(false);
                etAdviceHelp.setHint(getString(R.string.string_hint_help));
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }
}
