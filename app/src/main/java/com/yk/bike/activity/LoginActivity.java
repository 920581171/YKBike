package com.yk.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yk.bike.R;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.fragment.PhoneLoginFragment;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new PhoneLoginFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
