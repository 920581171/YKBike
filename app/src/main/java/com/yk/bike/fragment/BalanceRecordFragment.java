package com.yk.bike.fragment;

import com.yk.bike.activity.AccountActivity;
import com.yk.bike.adapter.BalanceRecordAdapter;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.BalanceRecordListResponse;
import com.yk.bike.response.BalanceRecordResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SpUtils;

import java.util.List;

public class BalanceRecordFragment extends BaseRecyclerFragment<AccountActivity> {
    @Override
    public void initData() {
        ApiUtils.getInstance().findBalanceRecordByUserId(SpUtils.getLoginId(), new ResponseListener<BalanceRecordListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(BalanceRecordListResponse balanceRecordListResponse) {
                if (isResponseSuccess(balanceRecordListResponse)) {
                    List<BalanceRecordResponse.BalanceRecord> balanceRecords = balanceRecordListResponse.getData();
                    BalanceRecordAdapter adapter = new BalanceRecordAdapter(balanceRecords);
                    onDataChange(adapter);
                } else {
                    showShort(balanceRecordListResponse.getMsg());
                }
            }
        });
    }
}
