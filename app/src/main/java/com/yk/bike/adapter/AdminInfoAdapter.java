package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.response.AdminInfoResponse;

import java.util.List;

public class AdminInfoAdapter extends RecyclerView.Adapter<AdminInfoAdapter.ViewHolder> {

    private List<AdminInfoResponse.AdminInfo> list;

    private OnItemClickListener<AdminInfoResponse.AdminInfo> onItemClickListener;

    public AdminInfoAdapter(List<AdminInfoResponse.AdminInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = list.size() == 0 ?
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_empty_list, viewGroup, false) :
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_admin_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        if (list.size()==0)
            return;

        viewHolder.tvAdminName.setText(list.get(i).getAdminName());
        viewHolder.tvPhone.setText(list.get(i).getAdminPhone());

        viewHolder.itemView.setOnClickListener(v -> {
        });
        viewHolder.itemView.setOnLongClickListener(v -> false);
        viewHolder.ivCall.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onRightButtonClick(v, viewHolder, list.get(i));
        });

        viewHolder.tvCall.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onRightButtonClick(v, viewHolder, list.get(i));
        });
    }

    public List<AdminInfoResponse.AdminInfo> getList() {
        return list;
    }

    public AdminInfoAdapter setList(List<AdminInfoResponse.AdminInfo> list) {
        this.list = list;
        return this;
    }

    public OnItemClickListener<AdminInfoResponse.AdminInfo> getOnItemClickListener() {
        return onItemClickListener;
    }

    public AdminInfoAdapter setOnItemClickListener(OnItemClickListener<AdminInfoResponse.AdminInfo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAdminName;
        public TextView tvPhone;
        public ImageView ivCall;
        public TextView tvCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdminName = itemView.findViewById(R.id.tv_admin_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivCall = itemView.findViewById(R.id.iv_call);
            tvCall = itemView.findViewById(R.id.tv_call);
        }
    }
}
