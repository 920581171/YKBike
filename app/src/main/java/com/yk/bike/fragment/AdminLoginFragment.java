package com.yk.bike.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnCommonResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MD5Utils;
import com.yk.bike.utils.SharedPreferencesUtils;

public class AdminLoginFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout tilInputAdminOrPhone;
    private TextInputLayout tilInputPassword;
    private EditText etInputAdminOrPhone;
    private EditText etInputPassword;
    private Button btnLogin;
    private TextView tvToStart;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_admin_login, container, false);
        init();
        return mRootView;
    }

    public void init() {
        tilInputAdminOrPhone = mRootView.findViewById(R.id.til_input_admin_or_phone);
        tilInputPassword = mRootView.findViewById(R.id.til_input_password);
        etInputAdminOrPhone = mRootView.findViewById(R.id.et_input_admin_or_phone);
        etInputPassword = mRootView.findViewById(R.id.et_input_password);
        btnLogin = mRootView.findViewById(R.id.btn_login);
        tvToStart = mRootView.findViewById(R.id.tv_to_start);

        btnLogin.setOnClickListener(this);
        tvToStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = etInputAdminOrPhone.getText().toString();
        String password = etInputPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btn_login:
                if (isEmptyString(name)) {
                    tilInputAdminOrPhone.setError("管理员或手机号不能为空");
                } else if (isEmptyString(password)) {
                    tilInputPassword.setError("密码不能为空");
                } else {
                    ApiUtils.getInstance().appAdminLogin(name, MD5Utils.getMD5(password), new OnCommonResponseListener<AdminInfoResponse>() {
                        @Override
                        public void onStart() {
                            showShort("登陆中");
                            btnLogin.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            btnLogin.setEnabled(true);
                        }

                        @Override
                        public void onError(String errorMsg) {
                            showShort(errorMsg);
                        }

                        @Override
                        public void onSuccess(AdminInfoResponse adminInfoResponse) {
                            if (isResponseSuccess(adminInfoResponse)) {
                                AdminInfoResponse.AdminInfo adminInfo = adminInfoResponse.getData();
                                showShort("登陆成功");
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_ID,adminInfo.getAdminId());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_NAME, adminInfo.getAdminName());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_PASSWORD, adminInfo.getAdminPassword());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_TYPE,Consts.LOGIN_TYPE_ADMIN);
                                sendBroadcast(Consts.BR_ACTION_LOGIN);
                                getActivity().finish();
                            } else {
                                showShort(adminInfoResponse.getMsg());
                            }
                        }
                    });
                }
                break;
            case R.id.tv_to_start:
                if (getActivity()!=null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new StartFragment()).commit();
                break;
        }
    }
}
