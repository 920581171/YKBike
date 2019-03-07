package com.yk.bike.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.activity.MineWeeperActivity;
import com.yk.bike.base.BaseFragment;

public class AboutFragment extends BaseFragment<MainActivity> {

    private ImageView ivLauncher;
    private TextView tvVersion;
    private TextView tvApprovalNumber;

    @Override
    public int initLayout() {
        return R.layout.fragment_about;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        ivLauncher = rootView.findViewById(R.id.iv_launcher);
        tvVersion = rootView.findViewById(R.id.tv_version);
        tvApprovalNumber = rootView.findViewById(R.id.tv_approval_number);
        ivLauncher.setOnLongClickListener(v -> {
            startActivity(new Intent(getActivityContext(), MineWeeperActivity.class));
            return true;
        });
    }

    @Override
    public void initData() {
        String version = "版本号：V" + getString(R.string.string_version_name);
        String number = "审图号：" + ((MapFragment) getActivityContext().getFragment(getActivityContext().FRAGMENT_MAP)).getApprovalNumber();
        tvVersion.setText(version);
        tvApprovalNumber.setText(number);
    }
}
