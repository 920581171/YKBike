package com.yk.bike.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.base.BaseFragment;

public class AboutFragment extends BaseFragment {

    private ImageView ivLauncher;
    private TextView tvVersion;
    private TextView tvApprovalNumber;
    private MainActivity mainActivity;

    @Override
    public int initLayout() {
        return R.layout.fragment_about;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        ivLauncher = rootView.findViewById(R.id.iv_launcher);
        tvVersion = rootView.findViewById(R.id.tv_version);
        tvApprovalNumber = rootView.findViewById(R.id.tv_approval_number);
    }

    @Override
    public void initData() {
        String version = "版本号：V" + getString(R.string.string_version_name);
        String number = "审图号：" + ((MapFragment) mainActivity.getFragment(mainActivity.FRAGMENT_MAP)).getApprovalNumber();
        tvVersion.setText(version);
        tvApprovalNumber.setText(number);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
