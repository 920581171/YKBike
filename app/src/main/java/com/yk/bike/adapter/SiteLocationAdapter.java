package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.response.SiteLocationResponse;

import java.util.List;

public class SiteLocationAdapter extends RecyclerView.Adapter<SiteLocationAdapter.ViewHolder> {

    private List<SiteLocationResponse.SiteLocation> list;

    private OnItemClickListener<SiteLocationResponse.SiteLocation> onItemClickListener;

    public SiteLocationAdapter(List<SiteLocationResponse.SiteLocation> list) {
        this.list = list;
    }

    public List<SiteLocationResponse.SiteLocation> getList() {
        return list;
    }

    public SiteLocationAdapter setList(List<SiteLocationResponse.SiteLocation> list) {
        this.list = list;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = list.size() == 0 ?
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_empty_list, viewGroup, false) :
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_site_location, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        if (list.size()==0)
            return;

        viewHolder.tvLatitude.setText(String.valueOf(list.get(i).getLatitude()));
        viewHolder.tvLongitude.setText(String.valueOf(list.get(i).getLongitude()));
        String radius = String.valueOf(list.get(i).getRadius()) + "米";
        viewHolder.tvRadius.setText(radius);

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(v, viewHolder, i);
        });

        viewHolder.itemView.setOnLongClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onLongClick(v, viewHolder, i);
            return true;
        });
    }

    public OnItemClickListener<SiteLocationResponse.SiteLocation> getOnItemClickListener() {
        return onItemClickListener;
    }

    public SiteLocationAdapter setOnItemClickListener(OnItemClickListener<SiteLocationResponse.SiteLocation> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLatitude;
        public TextView tvLongitude;
        public TextView tvRadius;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLatitude = itemView.findViewById(R.id.tv_latitude);
            tvLongitude = itemView.findViewById(R.id.tv_longitude);
            tvRadius = itemView.findViewById(R.id.tv_radius);
        }
    }
}
