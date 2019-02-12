package com.yk.bike.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yk.bike.R;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.StartFragment;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.NullObjectUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        transparentWindow();

        getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new StartFragment()).commit();

        String name = SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_NAME);
        String phone = SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_PHONE);
        String password = SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_PASSWORD);
        String type = SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE);


        if ((NullObjectUtils.isNotEmptyString(name) || NullObjectUtils.isNotEmptyString(phone)) && NullObjectUtils.isNotEmptyString(type))
            switch (type) {
                case Consts.LOGIN_TYPE_USER:
                    ApiUtils.getInstance().appLogin(name, password, new ResponseListener<UserInfoResponse>() {
                        @Override
                        public void onSuccess(UserInfoResponse userInfoResponse) {
                            sendBroadcast(new Intent().setAction(Consts.BR_ACTION_LOGIN));
                            showShort("登陆成功");
                            finish();
                        }
                    });
                    break;
                case Consts.LOGIN_TYPE_PHONE:
                    ApiUtils.getInstance().findUserByUserPhone(phone, new ResponseListener<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse)) {
                                sendBroadcast(new Intent().setAction(Consts.BR_ACTION_LOGIN));
                                showShort("登陆成功");
                                finish();
                            } else {
                                showShort(commonResponse.getMsg());
                            }
                        }
                    });
                    break;
                case Consts.LOGIN_TYPE_ADMIN:
                    ApiUtils.getInstance().appAdminLogin(name, password, new ResponseListener<AdminInfoResponse>() {
                        @Override
                        public void onSuccess(AdminInfoResponse adminInfoResponse) {
                            sendBroadcast(new Intent().setAction(Consts.BR_ACTION_LOGIN));
                            showShort("登陆成功");
                            finish();
                        }
                    });
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBroadcast(new Intent().setAction(Consts.BR_ACTION_EXIT));
    }

    /**
     * 透明状态栏，暂时只找到代码版的
     */
    private void transparentWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
