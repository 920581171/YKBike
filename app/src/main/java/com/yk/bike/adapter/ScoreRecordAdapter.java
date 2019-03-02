package com.yk.bike.adapter;

import android.support.annotation.NonNull;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.BalanceRecordResponse;
import com.yk.bike.response.ScoreRecordResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class ScoreRecordAdapter extends BaseAdapter<ScoreRecordResponse.ScoreRecord> {

    public ScoreRecordAdapter(List<ScoreRecordResponse.ScoreRecord> list) {
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

        String status = list.get(i).getScore() >= 0 ?
                "获得" + list.get(i).getScore() + "积分" :
                "兑换" + Math.abs(list.get(i).getScore()) + "积分";

        baseViewHolder.getTextView(R.id.tv_record_id).setText(list.get(i).getRecordId());
        baseViewHolder.getTextView(R.id.tv_create_time).setText(SimpleDateFormat.getDateTimeInstance().format(list.get(i).getCreateTime().getTime()));
        baseViewHolder.getTextView(R.id.tv_status).setText(status);
    }

}
