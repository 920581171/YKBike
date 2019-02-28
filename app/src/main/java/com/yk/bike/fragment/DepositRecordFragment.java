package com.yk.bike.fragment;

import com.yk.bike.activity.AccountActivity;
import com.yk.bike.adapter.DepositRecordAdapter;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.DepositRecordListResponse;
import com.yk.bike.response.DepositRecordResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SpUtils;

import java.util.List;

public class DepositRecordFragment extends BaseRecyclerFragment<AccountActivity> {
    @Override
    public void initData() {
        ApiUtils.getInstance().findDepositRecordByUserId(SpUtils.getLoginId(), new ResponseListener<DepositRecordListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(DepositRecordListResponse depositRecordListResponse) {
                if (isResponseSuccess(depositRecordListResponse)) {
                    List<DepositRecordResponse.DepositRecord> depositRecords = depositRecordListResponse.getData();
                    DepositRecordAdapter adapter = new DepositRecordAdapter(depositRecords);
                    onDataChange(adapter);
                } else {
                    showShort(depositRecordListResponse.getMsg());
                }
            }
        });
    }
}
