package com.yk.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yk.bike.R;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.MessageBroadAdminFragment;
import com.yk.bike.fragment.MessageBroadUserFragment;
import com.yk.bike.utils.SharedPreferencesUtils;

public class MessageBroadActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        setResult(Consts.RESULT_CODE_MESSAGE_BROAD);

        BaseFragment fragment = SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN) ?
                new MessageBroadAdminFragment().setMessageId(getIntent().getStringExtra(Consts.INTENT_STRING_MESSAGE_ID)) :
                new MessageBroadUserFragment();

        replaceFragment(R.id.ll_fragment, fragment);
    }
}
