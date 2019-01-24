package com.yk.bike.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.callback.OnCommonResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MD5Utils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.Objects;

public class RegisterFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private TextInputLayout tilInputName;
    private TextInputLayout tilInputPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText etInputName;
    private TextInputEditText etInputPassword;
    private TextInputEditText etConfirmPassword;
    private Button btnRigister;
    private TextView tvUserLogin;
    private TextView tvPhoneLogin;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_register, container, false);
        init();
        return mRootView;
    }

    public void init() {
        tilInputName = mRootView.findViewById(R.id.til_input_name);
        tilInputPassword = mRootView.findViewById(R.id.til_input_password);
        tilConfirmPassword = mRootView.findViewById(R.id.til_confirm_password);
        etInputName = mRootView.findViewById(R.id.et_input_name);
        etInputPassword = mRootView.findViewById(R.id.et_input_password);
        etConfirmPassword = mRootView.findViewById(R.id.et_confirm_password);
        btnRigister = mRootView.findViewById(R.id.btn_register);
        tvUserLogin = mRootView.findViewById(R.id.tv_user_login);
        tvPhoneLogin = mRootView.findViewById(R.id.tv_phone_login);

        btnRigister.setOnClickListener(this);
        tvUserLogin.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);

        etInputName.setOnFocusChangeListener(this);
        etInputPassword.setOnFocusChangeListener(this);
        etConfirmPassword.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (checkInput())
                    ApiUtils.getInstance().findUserByUserName(etInputName.getText().toString(), new OnBaseResponseListener<CommonResponse>() {
                        @Override
                        public void onError(String errorMsg) {
                            showShort(errorMsg);
                        }

                        @Override
                        public void onSuccess(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse))
                                tilInputName.setError("用户名已存在");
                            else
                                register();
                        }
                    });
                break;
            case R.id.tv_user_login:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login, new LoginFragment()).commit();
                break;
            case R.id.tv_phone_login:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login, new PhoneLoginFragment()).commit();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_input_name:
            case R.id.et_input_password:
            case R.id.et_confirm_password:
                checkInput();
                break;
        }
    }

    public boolean checkInput() {
        String name = etInputName.getText().toString();
        String password = etInputPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();
        tilInputName.setErrorEnabled(false);
        tilInputPassword.setErrorEnabled(false);
        tilConfirmPassword.setErrorEnabled(false);
        if (isEmptyString(name)) {
            tilInputName.setError("用户名不能为空");
        } else if (isEmptyString(password)) {
            tilInputPassword.setError("密码不能为空");
        } else if (password.length() < 8) {
            tilInputPassword.setError("密码不能少于8位");
        } else if (isEmptyString(confirm)) {
            tilConfirmPassword.setError("确认密码不能为空");
        } else if (!password.equals(confirm)) {
            tilConfirmPassword.setError("两次密码不相同");
        } else {
            return true;
        }
        return false;
    }

    public void register(){
        String name = etInputName.getText().toString();
        String password = etInputPassword.getText().toString();
        ApiUtils.getInstance().registerUserByName(name, MD5Utils.getMD5(password), new OnCommonResponseListener<UserInfoResponse>() {
            @Override
            public void onStart() {
                showShort("请稍后");
                btnRigister.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btnRigister.setEnabled(true);
            }

            @Override
            public void onError(String errorMsg) {
                showShort(errorMsg);
            }

            @Override
            public void onSuccess(UserInfoResponse userInfoResponse) {
                System.out.println("success");
                if (isResponseSuccess(userInfoResponse)) {
                    showShort("注册成功！");
                    UserInfoResponse.UserInfo userInfo = userInfoResponse.getData();
                    SharedPreferencesUtils.put(Consts.SP_LOGIN_ID,userInfo.getUserId());
                    SharedPreferencesUtils.put(Consts.SP_LOGIN_NAME, userInfo.getUserName());
                    SharedPreferencesUtils.put(Consts.SP_LOGIN_PASSWORD, userInfo.getUserPassword());
                    SharedPreferencesUtils.put(Consts.SP_LOGIN_TYPE,Consts.LOGIN_TYPE_USER);
                    sendBroadcast(Consts.BR_ACTION_LOGIN);
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    showShort("注册失败，请重试！");
                }
            }
        });
    }
}
