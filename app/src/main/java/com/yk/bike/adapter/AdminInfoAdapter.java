package com.yk.bike.adapter;

import android.support.annotation.NonNull;

import com.yk.bike.R;
import com.yk.bike.base.BaseAdapter;
import com.yk.bike.base.BaseViewHolder;
import com.yk.bike.response.AdminInfoResponse;

import java.util.List;

public class AdminInfoAdapter extends BaseAdapter<AdminInfoResponse.AdminInfo> {

    public AdminInfoAdapter(List<AdminInfoResponse.AdminInfo> list) {
        super(list);
    }

    @Override
    public int initLayout() {
        return R.layout.item_recycler_admin_info;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        if (list.size()==0)
            return;

        baseViewHolder.getTextView(R.id.tv_admin_name).setText(list.get(i).getAdminName());
        baseViewHolder.getTextView(R.id.tv_phone).setText(list.get(i).getAdminPhone());

        baseViewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener!=null){
                onItemClickListener.onClick(v,baseViewHolder,i);
            }
        });
        baseViewHolder.itemView.setOnLongClickListener(v -> false);
        baseViewHolder.getImageView(R.id.iv_call).setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onRightButtonClick(v, baseViewHolder, list.get(i));
        });

        baseViewHolder.getTextView(R.id.tv_call).setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onRightButtonClick(v, baseViewHolder, list.get(i));
        });
    }
}
