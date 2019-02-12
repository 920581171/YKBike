package com.yk.bike.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.response.BikeRecordResponse;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BikeRecordAdapter extends RecyclerView.Adapter<BikeRecordAdapter.ViewHolder> {

    List<BikeRecordResponse.BikeRecord> list;

    private OnItemClickListener<BikeRecordResponse.BikeRecord> onItemClickListener;

    public BikeRecordAdapter(List<BikeRecordResponse.BikeRecord> list) {
        this.list = list;
    }

    public List<BikeRecordResponse.BikeRecord> getList() {
        return list;
    }

    public BikeRecordAdapter setList(List<BikeRecordResponse.BikeRecord> list) {
        this.list = list;
        return this;
    }

    public OnItemClickListener<BikeRecordResponse.BikeRecord> getOnItemClickListener() {
        return onItemClickListener;
    }

    public BikeRecordAdapter setOnItemClickListener(OnItemClickListener<BikeRecordResponse.BikeRecord> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = list.size() == 0 ?
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_empty_list, viewGroup, false) :
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_bike_record, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        if (list.size()==0)
            return;

        Context context = viewHolder.itemView.getContext();

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

        viewHolder.tvOrderId.setText(list.get(i).getOrderId());
        viewHolder.tvBikeId.setText(list.get(i).getBikeId());
        viewHolder.tvMileage.setText(mileageText);
        viewHolder.tvCharge.setText(charge);
        viewHolder.tvCreateTime.setText(createTime);
        viewHolder.tvDuration.setText(durationText);

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(v, viewHolder, i);
        });

        viewHolder.itemView.setOnLongClickListener(v -> false);
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderId;
        public TextView tvBikeId;
        public TextView tvMileage;
        public TextView tvCharge;
        public TextView tvDuration;
        public TextView tvCreateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvBikeId = itemView.findViewById(R.id.tv_bike_id);
            tvMileage = itemView.findViewById(R.id.tv_mileage);
            tvCharge = itemView.findViewById(R.id.tv_charge);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvCreateTime = itemView.findViewById(R.id.tv_create_time);
        }
    }
}
