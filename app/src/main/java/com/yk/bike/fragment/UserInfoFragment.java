package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yk.bike.R;
import com.yk.bike.activity.AccountActivity;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.MobResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.AccountValidatorUtil;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.MD5Utils;
import com.yk.bike.utils.MainHandler;
import com.yk.bike.utils.NullObjectUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Objects;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoFragment extends BaseFragment<AccountActivity> implements View.OnClickListener {

    private CircleImageView cvAvatar;
    private TextView tvId;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvDeposit;
    private TextView tvBalance;

    private UserInfoResponse.UserInfo userInfo;

    @Override
    public int initLayout() {
        return R.layout.fragment_user_info;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        cvAvatar = rootView.findViewById(R.id.cv_avatar);
        tvId = rootView.findViewById(R.id.tv_id);
        tvName = rootView.findViewById(R.id.tv_name);
        tvPhone = rootView.findViewById(R.id.tv_phone);
        tvDeposit = rootView.findViewById(R.id.tv_deposit);
        tvBalance = rootView.findViewById(R.id.tv_balance);

        ConstraintLayout ctlAvatar = rootView.findViewById(R.id.ctl_avatar);
        ConstraintLayout ctlName = rootView.findViewById(R.id.ctl_name);
        ConstraintLayout ctlPhone = rootView.findViewById(R.id.ctl_phone);
        ConstraintLayout ctlBalance = rootView.findViewById(R.id.ctl_balance);
        ConstraintLayout ctlPassword = rootView.findViewById(R.id.ctl_password);
        ConstraintLayout ctlLogout = rootView.findViewById(R.id.ctl_logout);

        ctlAvatar.setOnClickListener(this);
        ctlName.setOnClickListener(this);
        ctlPhone.setOnClickListener(this);
        ctlBalance.setOnClickListener(this);
        ctlPassword.setOnClickListener(this);
        ctlLogout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findUserByUserId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse userInfoResponse) {
                if (isResponseSuccess(userInfoResponse)) {
                    userInfo = userInfoResponse.getData();
                    tvId.setText(userInfo.getUserId());
                    if (NullObjectUtils.isEmptyString(userInfo.getUserName())) {
                        tvName.setText("需要完善信息");
                        tvName.setTextColor(Color.RED);
                    } else {
                        tvName.setText(userInfo.getUserName());
                        tvName.setTextColor(Color.BLACK);
                    }
                    if (NullObjectUtils.isEmptyString(userInfo.getUserPhone())) {
                        tvPhone.setText("未验证手机号");
                        tvPhone.setTextColor(Color.RED);
                    } else {
                        tvPhone.setText(userInfo.getUserPhone());
                        tvPhone.setTextColor(Color.BLACK);
                    }
                    tvBalance.setText(String.valueOf(userInfo.getBalance()));
                    tvBalance.setTextColor(userInfo.getBalance() <= 10 ? Color.RED : Color.BLACK);
                    tvDeposit.setText(userInfo.getDeposit() < 0 ? "押金退还中" : String.valueOf(userInfo.getDeposit()));
                    tvDeposit.setTextColor(userInfo.getDeposit() <= 0 ? Color.RED : Color.BLACK);

                    Glide.with(getActivityContext())
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .error(R.drawable.avatar)
                                    //禁用内存缓存
                                    .skipMemoryCache(true)
                                    //硬盘缓存功能
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .load(UrlConsts.IPORT + UrlConsts.GET_COMMON_DOWNLOAD_AVATAR + "?id=" + userInfo.getUserId())
                            .into(cvAvatar);
                } else {
                    showShort(userInfoResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctl_avatar:
                getActivityContext().showSelectAvatar();
                break;
            case R.id.ctl_name:
                if (NullObjectUtils.isEmptyString(userInfo.getUserName()))
                    improveInfo();
                else
                    showAlertDialog("用户名", SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_NAME), new String[]{"确定", "取消"}, new AlertDialogListener());
                break;
            case R.id.ctl_phone:
                improvePhone();
                break;
            case R.id.ctl_balance:
                getActivityContext().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new RechargeFragment()).addToBackStack(null).commit();
                break;
            case R.id.ctl_password:
                if (NullObjectUtils.isEmptyString(userInfo.getUserName()))
                    improveInfo();
                else
                    resetPassword();
                break;
            case R.id.ctl_logout:
                showAlertDialog("注销账号", "是否注销：" + SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_NAME) + "？", new String[]{"注销", "取消"}, new AlertDialogListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        getActivityContext().setResult(Consts.RESULT_CODE_LOGOUT);
                        getActivityContext().finish();
                    }
                });
                break;
        }
    }

    public void improvePhone() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivityContext())
                .setTitle("请输入手机号")
                .setView(R.layout.dialog_improve_phone)
                .setPositiveButton("验证", null)
                .setNegativeButton("取消", (dialog, which) -> {
                })
                .show();

        TextInputLayout tilInputPhone = Objects.requireNonNull(alertDialog.findViewById(R.id.til_input_phone));
        TextInputLayout tilInputCode = Objects.requireNonNull(alertDialog.findViewById(R.id.til_input_code));
        TextInputEditText etInputPhone = Objects.requireNonNull(alertDialog.findViewById(R.id.et_input_phone));
        TextInputEditText etInputCode = Objects.requireNonNull(alertDialog.findViewById(R.id.et_input_code));

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                        String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                        //更新数据
                        ApiUtils.getInstance().updateUserInfo(UserInfoFragment.this.userInfo.copy().setUserPhone(phone), new ResponseListener<UserInfoResponse>() {
                            @Override
                            public void onSuccess(UserInfoResponse userInfoResponse) {
                                if (isResponseSuccess(userInfoResponse)) {
                                    showShort("验证成功");
                                    alertDialog.dismiss();
                                    initData();
                                } else {
                                    showShort(userInfoResponse.getMsg());
                                }
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        MainHandler.getInstance().post(() -> showShort("验证码已发送"));
                        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_GET_CODE_TIME, System.currentTimeMillis());
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    String s = ((Throwable) data).getMessage();
                    MobResponse mobResponse = (MobResponse) GsonUtils.fromJson(s, MobResponse.class);
                    runOnUiThread(() -> {
                        if (mobResponse != null)
                            showShort(mobResponse.getDetail());
                    });
                    ((Throwable) data).printStackTrace();
                }
            }
        };

        Button btnGetCode = Objects.requireNonNull(alertDialog.findViewById(R.id.btn_get_code));

        btnGetCode.setOnClickListener(v1 -> {
            String phone = etInputPhone.getText().toString();
            String code = etInputCode.getText().toString();
            long time = System.currentTimeMillis() - (long) SharedPreferencesUtils.get(Consts.SP_STRING_LOGIN_GET_CODE_TIME, 0L);
            time /= 1000;
            if (time <= 60) {
                showShort((60 - time) + "秒后可以重新发送");
            } else if ("".equals(phone)) {
                tilInputPhone.setError("请输入手机号");
            } else if (!AccountValidatorUtil.isMobile(phone)) {
                tilInputPhone.setError("请输入正确手机号");
            } else {
                ApiUtils.getInstance().findUserByUserPhone(phone, new ResponseListener<CommonResponse>() {
                    @Override
                    public void onSuccess(CommonResponse commonResponse) {
                        if (isResponseSuccess(commonResponse)) {
                            showShort("手机号已存在");
                        } else {
                            SMSSDK.getVerificationCode("86", phone);
                        }
                    }
                });
            }
        });

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            String phone = etInputPhone.getText().toString();
            String code = etInputCode.getText().toString();

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
        });

        alertDialog.setOnDismissListener(dialog -> {
            SMSSDK.unregisterEventHandler(eventHandler);//注销短信回调
        });

        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    public void improveInfo() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivityContext())
                .setTitle("请完善信息")
                .setView(R.layout.dialog_improve_info)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", (dialog, which) -> {
                })
                .show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            TextInputLayout tilInputName = Objects.requireNonNull(alertDialog.findViewById(R.id.til_input_name));
            TextInputLayout tilInputPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.til_input_password));
            TextInputLayout tilConfirmPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.til_confirm_password));
            TextInputEditText etInputName = alertDialog.findViewById(R.id.et_input_name);
            TextInputEditText etInputPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.et_input_password));
            TextInputEditText etConfirmPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.et_confirm_password));

            String name = etInputName.getText().toString();
            String password = etInputPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();
            if (NullObjectUtils.isEmptyString(name)) {
                tilInputName.setError("用户名不能为空");
            } else if (NullObjectUtils.isEmptyString(password)) {
                tilInputPassword.setError("密码不能为空");
            } else if (password.length() < 8) {
                tilInputPassword.setError("密码不能少于8位");
            } else if (NullObjectUtils.isEmptyString(confirm)) {
                tilConfirmPassword.setError("确认密码不能为空");
            } else if (!password.equals(confirm)) {
                tilConfirmPassword.setError("两次密码不相同");
            } else {
                ApiUtils.getInstance().findUserByUserName(name, new ResponseListener<CommonResponse>() {
                    @Override
                    public void onSuccess(CommonResponse commonResponse) {
                        if (isResponseSuccess(commonResponse))
                            showShort("用户名已存在");
                        else
                            ApiUtils.getInstance().updateUserInfo(userInfo.copy().setUserName(name).setUserPassword(MD5Utils.getMD5(password)), new ResponseListener<UserInfoResponse>() {
                                @Override
                                public void onSuccess(UserInfoResponse userInfoResponse) {
                                    if (isResponseSuccess(userInfoResponse)) {
                                        showShort("提交成功");
                                        alertDialog.dismiss();
                                        initData();
                                    } else {
                                        showShort(userInfoResponse.getMsg());
                                    }
                                }
                            });
                    }
                });
            }
        });
    }

    public void resetPassword() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivityContext())
                .setView(R.layout.dialog_reset_password)
                .setTitle("重置密码")
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", (dialog, which) -> {
                })
                .show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            TextInputLayout tilInputPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.til_input_password));
            TextInputLayout tilConfirmPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.til_confirm_password));
            TextInputEditText etInputPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.et_input_password));
            TextInputEditText etConfirmPassword = Objects.requireNonNull(alertDialog.findViewById(R.id.et_confirm_password));

            String password = etInputPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();

            if (NullObjectUtils.isEmptyString(password)) {
                tilInputPassword.setError("密码不能为空");
            } else if (password.length() < 8) {
                tilInputPassword.setError("密码不能少于8位");
            } else if (NullObjectUtils.isEmptyString(confirm)) {
                tilConfirmPassword.setError("确认密码不能为空");
            } else if (!password.equals(confirm)) {
                tilConfirmPassword.setError("两次密码不相同");
            } else {
                ApiUtils.getInstance().updateUserInfo(userInfo.copy().setUserPassword(MD5Utils.getMD5(password)), new ResponseListener<UserInfoResponse>() {
                    @Override
                    public void onSuccess(UserInfoResponse userInfoResponse) {
                        if (isResponseSuccess(userInfoResponse)) {
                            showShort("密码重置成功");
                            alertDialog.dismiss();
                            getActivityContext().setResult(Consts.RESULT_CODE_LOGOUT);
                            getActivityContext().finish();
                        } else {
                            showShort(userInfoResponse.getMsg());
                        }
                    }
                });
            }
        });
    }
}
