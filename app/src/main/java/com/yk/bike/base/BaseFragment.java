package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.bike.R;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.MapFragment;
import com.yk.bike.fragment.SiteLocationFragment;
import com.yk.bike.response.BaseResponse;
import com.yk.bike.utils.SharedPreferencesUtils;

public abstract class BaseFragment<T extends BaseActivity> extends Fragment {

    private static final String TAG = "BaseFragment";

    private T activityContext;

    public abstract int initLayout();

    public abstract void initView(View rootView, Bundle savedInstanceState);

    public abstract void initData();

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(initLayout(), container, false);
        initView(mRootView, savedInstanceState);
        initData();
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        boolean isShowAddSite =  BaseFragment.this instanceof MapFragment && SharedPreferencesUtils.getString(Consts.SP_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN);
        menu.setGroupVisible(R.id.group_add,isShowAddSite);
        menu.setGroupVisible(R.id.group_forward, BaseFragment.this instanceof SiteLocationFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityContext = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityContext = null;
    }

    public T getActivityContext() {
        return activityContext;
    }

    @SuppressLint("ShowToast")
    public void showShort(String text) {
        getActivityContext().showShort(text);
    }

    @SuppressLint("ShowToast")
    public void showLong(String text) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showLong(text);
    }

    public boolean isResponseSuccess(BaseResponse baseResponse) {
        return getActivityContext().isResponseSuccess(baseResponse);
    }

    public void runOnUiThread(Runnable action) {
        getActivityContext().runOnUiThread(action);
    }

    public void sendBroadcast(Intent intent) {
        getActivityContext().sendBroadcast(intent);
    }

    public void sendBroadcast(String[] actions) {
        getActivityContext().sendBroadcast(actions);
    }

    public void sendBroadcast(String action) {
        getActivityContext().sendBroadcast(action);
    }

    public void showAlertDialog(String title, String message, String[] buttonText, OnAlertDialogListener onAlertDialogListener) {
        getActivityContext().showAlertDialog(title, message, buttonText, onAlertDialogListener);
    }

    public void showAlertDialogList(String title, String message, String[] buttonText, OnAlertDialogListener onAlertDialogListener) {
        getActivityContext().showAlertDialogList(title, message, buttonText, onAlertDialogListener);
    }

    public void replaceFragment(int layoutId, Fragment fragment) {
        getActivityContext().replaceFragment(layoutId, fragment);
    }

    public void switchFragment(Fragment newFragment, Fragment currentFragment) {
        getActivityContext().switchFragment(newFragment, currentFragment);
    }

    public View getmRootView() {
        return mRootView;
    }

    public BaseFragment setmRootView(View mRootView) {
        this.mRootView = mRootView;
        return this;
    }
}
