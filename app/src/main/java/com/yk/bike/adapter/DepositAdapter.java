package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.response.UserInfoResponse;

import java.util.List;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {

    private List<UserInfoResponse.UserInfo> list;

    private OnItemClickListener onItemClickListener;

    public List<UserInfoResponse.UserInfo> getList() {
        return list;
    }

    public DepositAdapter setList(List<UserInfoResponse.UserInfo> list) {
        this.list = list;
        return this;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public DepositAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public DepositAdapter(List<UserInfoResponse.UserInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = list.size() == 0 ?
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_empty_list, viewGroup, false) :
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_deposit_info, viewGroup, false);
        return new DepositAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        if (list.size()==0)
            return;

        viewHolder.tvUserId.setText(list.get(i).getUserId());

        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, viewHolder, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUserId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserId = itemView.findViewById(R.id.tv_user_id);
        }
    }
}
