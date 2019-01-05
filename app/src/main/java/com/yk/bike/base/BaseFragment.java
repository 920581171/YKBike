package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {
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
