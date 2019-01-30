package com.yk.bike.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MD5Utils;
import com.yk.bike.utils.SharedPreferencesUtils;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout tilInputName;
    private TextInputLayout tilInputPassword;
    private EditText etInputName;
    private EditText etInputPassword;
    private Button btnLogin;

    @Override
    public int initLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        tilInputName = rootView.findViewById(R.id.til_input_name);
        tilInputPassword = rootView.findViewById(R.id.til_input_password);
        etInputName = rootView.findViewById(R.id.et_input_name);
        etInputPassword = rootView.findViewById(R.id.et_input_password);
        btnLogin = rootView.findViewById(R.id.btn_login);
        TextView tvToStart = rootView.findViewById(R.id.tv_to_start);
        TextView tvPhoneLogin = rootView.findViewById(R.id.tv_phone_login);

        btnLogin.setOnClickListener(this);
        tvToStart.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        String name = etInputName.getText().toString();
        String password = etInputPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btn_login:
                if (isEmptyString(name)) {
                    tilInputName.setError("用户名不能为空");
                } else if (isEmptyString(password)) {
                    tilInputPassword.setError("密码不能为空");
                } else {
                    ApiUtils.getInstance().appLogin(name, MD5Utils.getMD5(password), new ResponseListener<UserInfoResponse>() {
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
                        public void onSuccess(UserInfoResponse userInfoResponse) {
                            if (isResponseSuccess(userInfoResponse)) {
                                showShort("登陆成功");
                                UserInfoResponse.UserInfo userInfo = userInfoResponse.getData();
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_ID,userInfo.getUserId());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_NAME, userInfo.getUserName());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_PASSWORD, userInfo.getUserPassword());
                                SharedPreferencesUtils.put(Consts.SP_LOGIN_TYPE,Consts.LOGIN_TYPE_USER);
                                sendBroadcast(Consts.BR_ACTION_LOGIN);
                                getActivity().finish();
                            } else {
                                showShort(userInfoResponse.getMsg());
                            }
                        }
                    });
                }
                break;
            case R.id.tv_to_start:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login, new StartFragment()).commit();
                break;
            case R.id.tv_phone_login:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login, new PhoneLoginFragment()).commit();
                break;
        }
    }
}
