package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.SiteLocationResponse;

import java.util.List;

public class SiteLocationAdapter extends BaseAdapter<SiteLocationResponse.SiteLocation> {

    public SiteLocationAdapter(List<SiteLocationResponse.SiteLocation> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_site_location;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size()==0)
            return;

        View itemView = baseViewHolder.itemView;
        TextView tvLatitude = itemView.findViewById(R.id.tv_latitude);
        TextView tvLongitude = itemView.findViewById(R.id.tv_longitude);
        TextView tvRadius = itemView.findViewById(R.id.tv_radius);

        tvLatitude.setText(String.valueOf(list.get(i).getLatitude()));
        tvLongitude.setText(String.valueOf(list.get(i).getLongitude()));
        String radius = String.valueOf(list.get(i).getRadius()) + "米";
        tvRadius.setText(radius);

        itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(v, baseViewHolder, i);
        });

        baseViewHolder.itemView.setOnLongClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onLongClick(v, baseViewHolder, i);
            return true;
        });
    }
}
