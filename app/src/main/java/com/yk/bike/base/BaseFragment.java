package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.yk.bike.response.BaseResponse;
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

    public boolean isResponseSuccess(BaseResponse baseResponse) {
        if (getActivity() != null)
            return ((BaseActivity) getActivity()).isResponseSuccess(baseResponse);
        return false;
    }

    public void runOnUiThread(Runnable action) {
        if (getActivity() != null)
            getActivity().runOnUiThread(action);
    }

    public void sendBroadcast(Intent intent) {
        if (getActivity() != null)
            getActivity().sendBroadcast(intent);
    }

    public void sendBroadcast(String[] actions) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).sendBroadcast(actions);
    }

    public void sendBroadcast(String action) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).sendBroadcast(action);
    }

    public void showAlertDialog(String title, String message, String[] buttonText, OnAlertDialogButtonClickListener onAlertDialogButtonClickListener) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showAlertDialog(title, message, buttonText, onAlertDialogButtonClickListener);
    }
}
