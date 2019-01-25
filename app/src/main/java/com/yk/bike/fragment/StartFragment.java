package com.yk.bike.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;

public class StartFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public int initLayout() {
        return R.layout.fragment_start;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        Button btnLogin = rootView.findViewById(R.id.btn_login);
        Button btnRegister = rootView.findViewById(R.id.btn_register);
        Button btnAdminLogin = rootView.findViewById(R.id.btn_admin_login);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnAdminLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (getActivity()!=null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new LoginFragment()).commit();
                break;
            case R.id.btn_register:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new RegisterFragment()).commit();
                break;
            case R.id.btn_admin_login:
                if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_login,new AdminLoginFragment()).commit();
                break;
        }
    }
}
