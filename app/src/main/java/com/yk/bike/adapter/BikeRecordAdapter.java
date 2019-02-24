package com.yk.bike.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.BikeRecordResponse;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BikeRecordAdapter extends BaseAdapter<BikeRecordResponse.BikeRecord> {

    public BikeRecordAdapter(List<BikeRecordResponse.BikeRecord> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_bike_record;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size() == 0)
            return;

        View itemView = baseViewHolder.itemView;
        TextView tvOrderId = itemView.findViewById(R.id.tv_order_id);
        TextView tvBikeId = itemView.findViewById(R.id.tv_bike_id);
        TextView tvMileage = itemView.findViewById(R.id.tv_mileage);
        TextView tvCharge = itemView.findViewById(R.id.tv_charge);
        TextView tvDuration = itemView.findViewById(R.id.tv_duration);
        TextView tvCreateTime = itemView.findViewById(R.id.tv_create_time);

        Context context = itemView.getContext();

        String charge = String.valueOf(list.get(i).getCharge()) + context.getResources().getString(R.string.string_charge);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String mileageText = decimalFormat.format(list.get(i).getMileage() / 1000f) + context.getResources().getString(R.string.string_mileage_unit);

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String createTime = dateFormat.format(list.get(i).getCreateTime().getTime());

        long duration = list.get(i).getEndTime().getTime() - list.get(i).getCreateTime().getTime();
        String durationText = duration / 1000 / 60 + context.getResources().getString(R.string.string_duration);

        if (list.get(i).getOrderStatus().equals("0")) {
            charge = mileageText = durationText = "骑行中";
        }

        tvOrderId.setText(list.get(i).getOrderId());
        tvBikeId.setText(list.get(i).getBikeId());
        tvMileage.setText(mileageText);
        tvCharge.setText(charge);
        tvCreateTime.setText(createTime);
        tvDuration.setText(durationText);

        itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(v, baseViewHolder, i);
        });

        itemView.setOnLongClickListener(v -> false);
    }
}
