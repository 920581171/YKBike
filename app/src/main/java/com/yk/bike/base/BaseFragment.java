package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.yk.bike.response.CommonResponse;

public abstract class BaseFragment extends Fragment {
    @SuppressLint("ShowToast")
    public void showShort(String text) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showShort(text);
    }

    @SuppressLint("ShowToast")
    public void showLong(String text) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showLong(text);
    }

    public boolean isEmptyString(String s) {
        if (getActivity() != null)
            return ((BaseActivity) getActivity()).isEmptyString(s);
        return false;
    }

    public boolean isResponseSuccess(CommonResponse commonResponse) {
        if (getActivity() != null)
            return ((BaseActivity) getActivity()).isResponseSuccess(commonResponse);
        return false;
    }

    public void runOnUiThread(Runnable action) {
        if (getActivity() != null)
            getActivity().runOnUiThread(action);
    }

    public void sendBroadcast(Intent intent){
        if (getActivity()!=null)
            getActivity().sendBroadcast(intent);
    }
}
