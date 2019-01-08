package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {
    @SuppressLint("ShowToast")
    public void showShort(String text){
        if (getActivity()!=null)
        ((BaseActivity)getActivity()).showShort(text);
    }

    @SuppressLint("ShowToast")
    public void showLong(String text){
        if (getActivity()!=null)
            ((BaseActivity)getActivity()).showLong(text);
    }
}
