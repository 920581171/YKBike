package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.response.BikeInfoResponse;

import java.util.List;

public class BikeInfoAdapter extends RecyclerView.Adapter<BikeInfoAdapter.ViewHolder> {

    private List<BikeInfoResponse.BikeInfo> list;

    private OnItemClickListener<BikeInfoResponse.BikeInfo> onItemClickListener;

    public BikeInfoAdapter(List<BikeInfoResponse.BikeInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_bike_info, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        viewHolder.bikeId.setText(list.get(i).getBikeId());
        String mileageText = list.get(i).getMileage() + viewHolder.itemView.getContext().getResources().getString(R.string.string_mileage_unit);
        String userId = list.get(i).getUserId();
        viewHolder.mileage.setText(mileageText);

        if ("1".equals(list.get(i).getFix())) {
            viewHolder.status.setText(viewHolder.itemView.getContext().getResources().getString(R.string.string_status_fix));
        } else {
            if (userId == null || "".equals(userId)) {
                viewHolder.status.setText(viewHolder.itemView.getContext().getResources().getString(R.string.string_status_unused));
            } else {
                viewHolder.status.setText(viewHolder.itemView.getContext().getResources().getString(R.string.string_status_using));
            }
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v,viewHolder, i);
            }
        });

        viewHolder.itemView.setOnLongClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onLongClick(v,viewHolder, i);
            }
            return true;
        });

        viewHolder.ivShowInMap.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRightButtonClick(v,viewHolder, list.get(i));
            }
        });

        viewHolder.tvShowInMap.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRightButtonClick(v,viewHolder, list.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<BikeInfoResponse.BikeInfo> getList() {
        return list;
    }

    public BikeInfoAdapter setList(List<BikeInfoResponse.BikeInfo> list) {
        this.list = list;
        return this;
    }

    public OnItemClickListener<BikeInfoResponse.BikeInfo> getOnItemClickListener() {
        return onItemClickListener;
    }

    public BikeInfoAdapter setOnItemClickListener(OnItemClickListener<BikeInfoResponse.BikeInfo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bikeId;
        public TextView status;
        public TextView mileage;

        public ImageView ivShowInMap;
        public TextView tvShowInMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bikeId = itemView.findViewById(R.id.tv_bike_id);
            status = itemView.findViewById(R.id.tv_status);
            mileage = itemView.findViewById(R.id.tv_mileage);
            ivShowInMap = itemView.findViewById(R.id.iv_show_in_map);
            tvShowInMap = itemView.findViewById(R.id.tv_show_in_map);
        }
    }
}
