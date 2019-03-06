package com.yk.bike.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yk.bike.R;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.utils.SpUtils;

import java.net.URL;

public class StartFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvService;

    @Override
    public int initLayout() {
        return R.layout.fragment_start;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        Button btnLogin = rootView.findViewById(R.id.btn_login);
        Button btnRegister = rootView.findViewById(R.id.btn_register);
        Button btnAdminLogin = rootView.findViewById(R.id.btn_admin_login);
        tvService = rootView.findViewById(R.id.tv_service);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnAdminLogin.setOnClickListener(this);
        tvService.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String s = getString(R.string.string_now_service) + SpUtils.getIpAddress();
        tvService.setText(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (getActivity()!=null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new LoginFragment()).addToBackStack(null).commit();
                break;
            case R.id.btn_register:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new RegisterFragment()).addToBackStack(null).commit();
                break;
            case R.id.btn_admin_login:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new AdminLoginFragment()).addToBackStack(null).commit();
                break;
            case R.id.tv_service:
                showAlertDialogList("选择服务器",null ,UrlConsts.IPS, new AlertDialogListener(){
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        showShort("选择服务器后需要重启APP");
                        SharedPreferencesUtils.put(Consts.SP_STRING_IP_ADDRESS,UrlConsts.IPS[which]);
                        String s = getString(R.string.string_now_service) + UrlConsts.IPS[which];
                        tvService.setText(s);
                    }
                });
                break;
        }
    }
}
