package com.yk.bike.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public interface OnItemClickListener<T> {
    void onClick(View v, ViewHolder holder, int position);

    void onLongClick(View v, ViewHolder holder, int position);

    void onRightButtonClick(View v, ViewHolder holder, T t);
}
