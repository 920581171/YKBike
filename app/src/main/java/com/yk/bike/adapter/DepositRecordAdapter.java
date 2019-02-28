package com.yk.bike.adapter;

import android.support.annotation.NonNull;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.DepositRecordResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class DepositRecordAdapter extends BaseAdapter<DepositRecordResponse.DepositRecord> {

    public DepositRecordAdapter(List<DepositRecordResponse.DepositRecord> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_deposit_record;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0)
            return;

        String status = list.get(i).getDeposit() >= 0 ?
                "押金" + list.get(i).getDeposit() + "元" :
                "退还押金" + Math.abs(list.get(i).getDeposit()) + "元";

        baseViewHolder.getTextView(R.id.tv_record_id).setText(list.get(i).getRecordId());
        baseViewHolder.getTextView(R.id.tv_create_time).setText(SimpleDateFormat.getDateTimeInstance().format(list.get(i).getCreateTime().getTime()));
        baseViewHolder.getTextView(R.id.tv_status).setText(status);
    }

}
