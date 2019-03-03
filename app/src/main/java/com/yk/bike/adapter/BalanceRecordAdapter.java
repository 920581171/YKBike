package com.yk.bike.adapter;

import android.support.annotation.NonNull;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.BalanceRecordResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class BalanceRecordAdapter extends BaseAdapter<BalanceRecordResponse.BalanceRecord> {

    public BalanceRecordAdapter(List<BalanceRecordResponse.BalanceRecord> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_record;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0)
            return;

        String status = list.get(i).getIsExchange().equals("1") ?
                "兑换" + list.get(i).getBalance() + "元" :
                list.get(i).getBalance() >= 0 ?
                        "充值" + list.get(i).getBalance() + "元" :
                        "扣除" + Math.abs(list.get(i).getBalance()) + "元";

        baseViewHolder.getTextView(R.id.tv_record_id).setText(list.get(i).getRecordId());
        baseViewHolder.getTextView(R.id.tv_create_time).setText(SimpleDateFormat.getDateTimeInstance().format(list.get(i).getCreateTime().getTime()));
        baseViewHolder.getTextView(R.id.tv_status).setText(status);
    }

}
