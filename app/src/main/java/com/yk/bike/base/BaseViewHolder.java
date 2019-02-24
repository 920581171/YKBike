package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views = new SparseArray<>();

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        setIsRecyclable(false);
    }

    public View getView(int viewId){
        View view = views.get(viewId);
        if (view == null){
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return view;
    }

    public TextView getTextView(int viewId){
        return (TextView) getView(viewId);
    }

    public ImageView getImageView(int viewId){
        return (ImageView)getView(viewId);
    }

    public Button getButton(int viewId){
        return (Button)getView(viewId);
    }
}
