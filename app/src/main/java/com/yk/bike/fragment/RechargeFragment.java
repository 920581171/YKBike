package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.activity.AccountActivity;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.utils.SpUtils;

public class RechargeFragment extends BaseFragment<AccountActivity> implements View.OnClickListener {

    private TextView tvShowDeposit;
    private TextView tvDeposit;
    private TextView tvShowBalance;
    private TextView tvBalance;

    UserInfoResponse.UserInfo userInfo;
    private TextView tvDepositCharge;
    private ConstraintLayout ctlChargeList;

    @Override
    public int initLayout() {
        return R.layout.fragment_recharge;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        tvShowDeposit = rootView.findViewById(R.id.tv_show_deposit);
        tvDeposit = rootView.findViewById(R.id.tv_deposit);
        tvShowBalance = rootView.findViewById(R.id.tv_show_balance);
        tvBalance = rootView.findViewById(R.id.tv_balance);
        tvDepositCharge = rootView.findViewById(R.id.tv_deposit_charge);
        ctlChargeList = rootView.findViewById(R.id.ctl_charge_list);

        ctlChargeList.setVisibility(View.GONE);

        TextView tcCharge10 = rootView.findViewById(R.id.charge10);
        TextView tcCharge20 = rootView.findViewById(R.id.charge20);
        TextView tcCharge30 = rootView.findViewById(R.id.charge30);
        TextView tcCharge50 = rootView.findViewById(R.id.charge50);
        TextView tcCharge100 = rootView.findViewById(R.id.charge100);
        TextView tcCharge150 = rootView.findViewById(R.id.charge150);

        ConstraintLayout ctlBalanceCharge = rootView.findViewById(R.id.ctl_balance_charge);
        ConstraintLayout ctlDepositCharge = rootView.findViewById(R.id.ctl_deposit_charge);

        tvShowDeposit.setOnClickListener(this);
        tvDeposit.setOnClickListener(this);
        tvShowBalance.setOnClickListener(this);
        tvBalance.setOnClickListener(this);

        tcCharge10.setOnClickListener(this);
        tcCharge20.setOnClickListener(this);
        tcCharge30.setOnClickListener(this);
        tcCharge50.setOnClickListener(this);
        tcCharge100.setOnClickListener(this);
        tcCharge150.setOnClickListener(this);

        ctlBalanceCharge.setOnClickListener(this);
        ctlDepositCharge.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findUserByUserId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse userInfoResponse) {
                if (isResponseSuccess(userInfoResponse)) {
                    userInfo = userInfoResponse.getData();
                    tvBalance.setText(String.valueOf(userInfo.getBalance()));
                    tvBalance.setTextColor(userInfo.getBalance() <= 10 ? Color.RED : Color.BLACK);
                    tvDeposit.setText(userInfo.getDeposit() < 0 ? "押金退还中" : String.valueOf(userInfo.getDeposit()));
                    tvDeposit.setTextColor(userInfo.getDeposit() <= 0 ? Color.RED : Color.BLACK);
                    tvDepositCharge.setText(userInfo.getDeposit() < 0 ? "已申请退还押金" :
                            userInfo.getDeposit() == 0 ? "支付押金" : "申请退还押金");
                } else {
                    showShort(userInfoResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_deposit:
            case R.id.tv_deposit:
                getActivityContext().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new DepositRecordFragment()).addToBackStack(null).commit();
                break;
            case R.id.tv_show_balance:
            case R.id.tv_balance:
                getActivityContext().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new BalanceRecordFragment()).addToBackStack(null).commit();
                break;
            case R.id.ctl_balance_charge:
                v.setSelected(ctlChargeList.getVisibility() == View.GONE);
                ctlChargeList.setVisibility(ctlChargeList.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.ctl_deposit_charge:
                if (tvDepositCharge.getText().toString().equals("支付押金"))
                    showAlertDialog("支付押金", "是否支付199元押金？", new String[]{"支付", "取消"}, new AlertDialogListener() {
                        @Override
                        public void onPositiveClick(DialogInterface dialog, int which) {
                            ApiUtils.getInstance().addDepositRecord(SpUtils.getLoginId(), 199, new ResponseListener<CommonResponse>() {
                                @Override
                                public void onSuccess(CommonResponse commonResponse) {
                                    if (isResponseSuccess(commonResponse)) {
                                        showShort("支付成功");
                                        initData();
                                    } else {
                                        showShort(commonResponse.getMsg());
                                    }
                                }
                            });
                        }
                    });
                else if (tvDepositCharge.getText().toString().equals("申请退还押金"))
                    showAlertDialog("押金", "确认申请退还押金？", new String[]{"申请", "取消"}, new AlertDialogListener() {
                        @Override
                        public void onPositiveClick(DialogInterface dialog, int which) {
                            ApiUtils.getInstance().updateUserInfo(userInfo.copy().setDeposit(-199), new ResponseListener<UserInfoResponse>() {
                                @Override
                                public void onSuccess(UserInfoResponse userInfoResponse) {
                                    if (isResponseSuccess(userInfoResponse)) {
                                        showShort("申请成功");
                                        initData();
                                    } else {
                                        showShort(userInfoResponse.getMsg());
                                    }
                                }
                            });
                        }
                    });
                else
                    showAlertDialog("退还押金", "已提交申请，请耐心等待", new String[]{"确定", "取消"}, new AlertDialogListener() {
                    });
                break;
            case R.id.charge10:
                charge(10);
                break;
            case R.id.charge20:
                charge(20);
                break;
            case R.id.charge30:
                charge(30);
                break;
            case R.id.charge50:
                charge(50);
                break;
            case R.id.charge100:
                charge(100);
                break;
            case R.id.charge150:
                charge(150);
                break;
        }
    }

    public void charge(float charge) {
        showAlertDialog("余额充值", "是否充值" + charge + "元到余额中？", new String[]{"充值", "取消"}, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                ApiUtils.getInstance().addBalanceRecord(SpUtils.getLoginId(),charge, new ResponseListener<CommonResponse>() {
                    @Override
                    public void onSuccess(CommonResponse commonResponse) {
                        if (isResponseSuccess(commonResponse)) {
                            showShort("充值成功");
                            initData();
                        } else {
                            showShort(commonResponse.getMsg());
                        }
                    }
                });
            }
        });
    }
}
