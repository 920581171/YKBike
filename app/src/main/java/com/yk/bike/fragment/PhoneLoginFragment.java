package com.yk.bike.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.MobResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.AccountValidatorUtil;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.MainHandler;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PhoneLoginFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout tilInputPhone;
    private TextInputLayout tilInputCode;
    private TextInputEditText etInputPhone;
    private TextInputEditText etInputCode;
    private Button btnGetCode;
    private Button btnRigister;
    private Handler handler;

    private long timeOut;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long time = (System.currentTimeMillis() - timeOut) / 1000;
            if (time <= 60) {
                btnGetCode.setText(String.valueOf(60 - time));
                btnGetCode.setEnabled(false);
                handler.postDelayed(runnable, 1000);
            } else {
                btnGetCode.setText(getResources().getText(R.string.string_get_code));
                btnGetCode.setEnabled(true);
            }
        }
    };

    @Override
    public int initLayout() {
        return R.layout.fragment_phone_login;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        tilInputPhone = rootView.findViewById(R.id.til_input_phone);
        tilInputCode = rootView.findViewById(R.id.til_input_code);
        etInputPhone = rootView.findViewById(R.id.et_input_phone);
        etInputCode = rootView.findViewById(R.id.et_input_code);
        btnGetCode = rootView.findViewById(R.id.btn_get_code);
        btnRigister = rootView.findViewById(R.id.btn_register);
        TextView tvUserLogin = rootView.findViewById(R.id.tv_user_login);

        etInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()==11){
                    ApiUtils.getInstance().findUserByUserPhone(s.toString(), new ResponseListener<CommonResponse>() {
                        @Override
                        public void onError(String errorMsg) {
                            showShort(errorMsg);
                        }

                        @Override
                        public void onSuccess(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse)){
                                btnRigister.setText(R.string.string_login);
                            }else{
                                btnRigister.setText(R.string.string_register);
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilInputCode.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnGetCode.setOnClickListener(this);
        btnRigister.setOnClickListener(this);
        tvUserLogin.setOnClickListener(this);

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void initData() {
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                        String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                        ApiUtils.getInstance().registerUserByPhone(phone, new ResponseListener<UserInfoResponse>() {
                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }

                            @Override
                            public void onSuccess(UserInfoResponse userInfoResponse) {
                                tilInputCode.setErrorEnabled(false);
                                UserInfoResponse.UserInfo userInfo = userInfoResponse.getData();
                                showShort(btnRigister.getText().toString()+"成功");
                                SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_ID,userInfo.getUserId());
                                SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_NAME,userInfo.getUserName());
                                SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_PHONE,userInfo.getUserPhone());
                                SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_TYPE,Consts.LOGIN_TYPE_PHONE);
                                sendBroadcast(Consts.BR_ACTION_LOGIN);
                                getActivity().finish();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        MainHandler.getInstance().post(() -> showShort("验证码已发送"));
                        timeOut = System.currentTimeMillis();
                        handler.post(runnable);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    String s = ((Throwable) data).getMessage();
                    MobResponse mobResponse = (MobResponse) GsonUtils.fromJson(s,MobResponse.class);
                    runOnUiThread(() -> {
                        if (mobResponse!=null)
                            tilInputCode.setError(mobResponse.getDetail());
                    });
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    public void onClick(View v) {
        String phone = etInputPhone.getText().toString();
        String code = etInputCode.getText().toString();
        switch (v.getId()) {
            case R.id.btn_get_code:
                if ("".equals(phone)) {
                    tilInputPhone.setError("请输入手机号");
                } else if (!AccountValidatorUtil.isMobile(phone)) {
                    tilInputPhone.setError("请输入正确手机号");
                } else {
                    SMSSDK.getVerificationCode("86", phone);
                }
                break;
            case R.id.btn_register:
                if ("".equals(phone)) {
                    tilInputPhone.setError("请输入手机号");
                } else if (!AccountValidatorUtil.isMobile(phone)) {
                    tilInputPhone.setError("请输入正确手机号");
                } else if ("".equals(code)) {
                    tilInputCode.setError("请输入验证码");
                } else if (code.length() != 4) {
                    tilInputCode.setError("请输入4位验证码");
                } else {
                    SMSSDK.submitVerificationCode("86", phone, code);
                }
                break;
            case R.id.tv_user_login:
                if (getActivity()!=null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new LoginFragment()).commit();
                break;
        }
    }
}
