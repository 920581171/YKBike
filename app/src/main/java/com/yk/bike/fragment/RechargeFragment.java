package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
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
    private TextView tvShowScore;
    private TextView tvScore;

    UserInfoResponse.UserInfo userInfo;
    private TextView tvDepositCharge;
    private ConstraintLayout ctlChargeList;
    private ConstraintLayout ctlScoreList;
    private TextView tvMember;

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
        tvShowScore = rootView.findViewById(R.id.tv_show_score);
        tvScore = rootView.findViewById(R.id.tv_score);
        tvDepositCharge = rootView.findViewById(R.id.tv_deposit_charge);
        ctlChargeList = rootView.findViewById(R.id.ctl_charge_list);
        ctlScoreList = rootView.findViewById(R.id.ctl_score_list);
        tvMember = rootView.findViewById(R.id.tv_member);

        ctlChargeList.setVisibility(View.GONE);
        ctlScoreList.setVisibility(View.GONE);

        TextView tvCharge10 = rootView.findViewById(R.id.charge10);
        TextView tvCharge20 = rootView.findViewById(R.id.charge20);
        TextView tvCharge30 = rootView.findViewById(R.id.charge30);
        TextView tvCharge50 = rootView.findViewById(R.id.charge50);
        TextView tvCharge100 = rootView.findViewById(R.id.charge100);
        TextView tvCharge150 = rootView.findViewById(R.id.charge150);
        TextView tvScore50 = rootView.findViewById(R.id.score50);
        TextView tvScore100 = rootView.findViewById(R.id.score100);
        TextView tvScore250 = rootView.findViewById(R.id.score250);

        ConstraintLayout ctlBalanceCharge = rootView.findViewById(R.id.ctl_balance_charge);
        ConstraintLayout ctlDepositCharge = rootView.findViewById(R.id.ctl_deposit_charge);
        ConstraintLayout ctlScoreExchange = rootView.findViewById(R.id.ctl_score_exchange);

        tvShowDeposit.setOnClickListener(this);
        tvDeposit.setOnClickListener(this);
        tvShowBalance.setOnClickListener(this);
        tvBalance.setOnClickListener(this);
        tvShowScore.setOnClickListener(this);
        tvScore.setOnClickListener(this);

        tvCharge10.setOnClickListener(this);
        tvCharge20.setOnClickListener(this);
        tvCharge30.setOnClickListener(this);
        tvCharge50.setOnClickListener(this);
        tvCharge100.setOnClickListener(this);
        tvCharge150.setOnClickListener(this);
        tvScore50.setOnClickListener(this);
        tvScore100.setOnClickListener(this);
        tvScore250.setOnClickListener(this);

        ctlBalanceCharge.setOnClickListener(this);
        ctlDepositCharge.setOnClickListener(this);
        ctlScoreExchange.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findUserByUserId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse userInfoResponse) {
                if (isResponseSuccess(userInfoResponse)) {
                    userInfo = userInfoResponse.getData();
                    tvBalance.setText(String.valueOf(userInfo.getBalance()));
                    tvBalance.setTextColor(userInfo.getBalance() <= 10 ? ContextCompat.getColor(getActivityContext(), R.color.colorAccent) : Color.BLACK);
                    tvDeposit.setText(userInfo.getDeposit() < 0 ? "押金退还中" : String.valueOf(userInfo.getDeposit()));
                    tvDeposit.setTextColor(userInfo.getDeposit() <= 0 ? ContextCompat.getColor(getActivityContext(), R.color.colorAccent) : Color.BLACK);
                    tvDepositCharge.setText(userInfo.getDeposit() < 0 ? "已申请退还押金" :
                            userInfo.getDeposit() == 0 ? "支付押金" : "申请退还押金");
                    tvScore.setText(String.valueOf(userInfo.getScore()));
                    tvScore.setTextColor(userInfo.getScore() >= 500 ? ContextCompat.getColor(getActivityContext(), R.color.colorPrimary) : Color.BLACK);
                    tvMember.setText(userInfo.getScore() >= 500?"你已成为会员":"还差"+(500-userInfo.getScore())+"积分成为会员");
                    tvMember.setTextColor(userInfo.getScore() >= 500 ? ContextCompat.getColor(getActivityContext(), R.color.colorPrimary) : Color.BLACK);
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
            case R.id.tv_show_score:
            case R.id.tv_score:
                getActivityContext().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new ScoreRecordFragment()).addToBackStack(null).commit();
                break;
            case R.id.ctl_balance_charge:
                v.setSelected(ctlChargeList.getVisibility() == View.GONE);
                ctlChargeList.setVisibility(ctlChargeList.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.ctl_score_exchange:
                v.setSelected(ctlScoreList.getVisibility() == View.GONE);
                ctlScoreList.setVisibility(ctlScoreList.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
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
            case R.id.score50:
                checkScore(50);
                break;
            case R.id.score100:
                checkScore(100);
                break;
            case R.id.score250:
                checkScore(250);
                break;
        }
    }

    public void charge(float charge) {
        showAlertDialog("余额充值", "是否充值" + charge + "元到余额中？", new String[]{"充值", "取消"}, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                ApiUtils.getInstance().addBalanceRecord(SpUtils.getLoginId(), charge, new ResponseListener<CommonResponse>() {
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

    public void checkScore(int score) {
        showAlertDialog("积分兑换", "是否使用" + score + "积分兑换余额？", new String[]{"兑换", "取消"}, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                if (userInfo.getScore() >= 500 && userInfo.getScore() - score <= 500) {
                    showAlertDialog("即将失去会员资格", "兑换后将失去会员资格", new String[]{"确定兑换", "取消"}, new AlertDialogListener() {
                        @Override
                        public void onPositiveClick(DialogInterface dialog, int which) {
                            exchange(score);
                        }
                    });
                } else {
                    exchange(score);
                }
            }
        });
    }

    public void exchange(int score) {
        if (userInfo.getScore() - score < 0) {
            showAlertDialog("兑换失败", "积分不足以兑换", new String[]{"确定"}, new AlertDialogListener());
            return;
        }
        ApiUtils.getInstance().addScoreRecord(SpUtils.getLoginId(), score, new ResponseListener<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse commonResponse) {
                if (isResponseSuccess(commonResponse)) {
                    showShort("兑换成功");
                    initData();
                } else {
                    showShort(commonResponse.getMsg());
                }
            }
        });
    }
}
