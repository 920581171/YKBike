package com.yk.bike.fragment;

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
import android.widget.Toast;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.callback.OnCommonResponseListener;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MD5Utils;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout tilInputName;
    private TextInputLayout tilInputPassword;
    private EditText etInputName;
    private EditText etInputPassword;
    private Button btnLogin;
    private TextView tvToStart;
    private TextView tvPhoneLogin;

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return mRootView;
    }

    public void init() {
        tilInputName = mRootView.findViewById(R.id.til_input_name);
        tilInputPassword = mRootView.findViewById(R.id.til_input_password);
        etInputName = mRootView.findViewById(R.id.et_input_name);
        etInputPassword = mRootView.findViewById(R.id.et_input_password);
        btnLogin = mRootView.findViewById(R.id.btn_login);
        tvToStart = mRootView.findViewById(R.id.tv_to_start);
        tvPhoneLogin = mRootView.findViewById(R.id.tv_phone_login);

        btnLogin.setOnClickListener(this);
        tvToStart.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);
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
                    ApiUtils.getInstance().appLogin(name, MD5Utils.getMD5(password), new OnCommonResponseListener<CommonResponse>() {
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
                        public void onError() {
                            showShort("网络出错");
                        }

                        @Override
                        public void onResponse(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse)) {
                                showShort("登陆成功");
                                getActivity().finish();
                            } else {
                                showShort(commonResponse.getMsg());
                            }
                        }
                    });
                }
                break;
            case R.id.tv_to_start:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new StartFragment()).commit();
                break;
            case R.id.tv_phone_login:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new PhoneLoginFragment()).commit();
                break;
        }
    }
}
