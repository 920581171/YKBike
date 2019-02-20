package com.yk.bike.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.UserInfoResponse;

import java.util.List;

public class DepositAdapter extends BaseAdapter<UserInfoResponse.UserInfo> {

    public DepositAdapter(List<UserInfoResponse.UserInfo> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_deposit_info;
    }

    public DepositAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.setIsRecyclable(false);

        if (list.size() == 0)
            return;

        baseViewHolder.getTextView(R.id.tv_user_id).setText(list.get(i).getUserId());

        baseViewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, baseViewHolder, i);
            }
        });
    }
}
