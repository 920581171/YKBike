package com.yk.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.yk.bike.R;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.UserInfoFragment;
import com.yk.bike.utils.SharedPreferencesUtils;


public class AccountActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SharedPreferencesUtils.getString(Consts.SP_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN)) {

        }else{
            replaceFragment(R.id.ll_fragment, new UserInfoFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
