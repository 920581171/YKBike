package com.yk.bike.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.bike.R;
import com.yk.bike.adapter.OnItemClickListener;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> list;

    protected OnItemClickListener<T> onItemClickListener;

    public BaseAdapter(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public BaseAdapter<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public BaseAdapter<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public abstract int initLayout();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = list.size() == 0 ?
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_empty_list, viewGroup, false) :
                LayoutInflater.from(viewGroup.getContext()).inflate(initLayout(), viewGroup, false);
        return new BaseViewHolder(view);
    }

    public abstract void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i);

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }
}
