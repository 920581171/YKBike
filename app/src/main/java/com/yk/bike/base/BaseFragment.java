package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.bike.response.BaseResponse;
import com.yk.bike.response.CommonResponse;

public abstract class BaseFragment extends Fragment {

    public abstract int initLayout();

    public abstract void initView(View rootView,Bundle savedInstanceState);

    public abstract void initData();

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(initLayout(),container,false);
        initView(mRootView,savedInstanceState);
        initData();
        return mRootView;
    }

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

    public void replaceFragment(int layoutId, Fragment fragment) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).replaceFragment(layoutId, fragment);
    }

    public void switchFragment(Fragment newFragment,Fragment currentFragment){
        if (getActivity()!=null){
            ((BaseActivity) getActivity()).switchFragment(newFragment,currentFragment);
        }
    }

    public View getmRootView() {
        return mRootView;
    }

    public BaseFragment setmRootView(View mRootView) {
        this.mRootView = mRootView;
        return this;
    }
}
