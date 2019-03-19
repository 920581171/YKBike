package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.BikeTypeResponse;
import com.yk.bike.utils.StaticUtils;

import java.util.List;

public class BikeInfoAdapter extends BaseAdapter<BikeInfoResponse.BikeInfo> {

    public BikeInfoAdapter(List<BikeInfoResponse.BikeInfo> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_bike_info;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size()==0)
            return;

        View itemView = baseViewHolder.itemView;
        TextView bikeId = itemView.findViewById(R.id.tv_bike_id);
        TextView status = itemView.findViewById(R.id.tv_status);
        TextView mileage = itemView.findViewById(R.id.tv_mileage);
        ImageView ivShowInMap = itemView.findViewById(R.id.iv_show_in_map);
        TextView tvShowInMap = itemView.findViewById(R.id.tv_show_in_map);
        TextView tvBikeType = itemView.findViewById(R.id.tv_bike_type);

        BikeTypeResponse.BikeType bikeType = StaticUtils.getInstance().getBikeType(list.get(i).getBikeType());

        bikeId.setText(list.get(i).getBikeId());
        String mileageText = list.get(i).getMileage() + itemView.getContext().getResources().getString(R.string.string_mileage_unit);
        String userId = list.get(i).getUserId();
        mileage.setText(mileageText);
        tvBikeType.setText(bikeType.getTypeName());

        if ("1".equals(list.get(i).getFix())) {
            status.setText(itemView.getContext().getResources().getString(R.string.string_status_fix));
        } else {
            if (userId == null || "".equals(userId)) {
                status.setText(itemView.getContext().getResources().getString(R.string.string_status_unused));
            } else {
                status.setText(itemView.getContext().getResources().getString(R.string.string_status_using));
            }
        }

        itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, baseViewHolder, i);
            }
        });

        baseViewHolder.itemView.setOnLongClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onLongClick(v, baseViewHolder, i);
            }
            return true;
        });

        ivShowInMap.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRightButtonClick(v, baseViewHolder, list.get(i));
            }
        });

        tvShowInMap.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRightButtonClick(v, baseViewHolder, list.get(i));
            }
        });
    }
}
