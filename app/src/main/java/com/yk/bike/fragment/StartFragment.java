package com.yk.bike.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yk.bike.R;
import com.yk.bike.base.BaseFragment;

public class StartFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;

    private Button btnLogin;
    private Button btnRegister;
    private Button btnAdminLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_start, container, false);
        init();
        return mRootView;
    }

    public void init() {
        btnLogin = mRootView.findViewById(R.id.btn_login);
        btnRegister = mRootView.findViewById(R.id.btn_register);
        btnAdminLogin = mRootView.findViewById(R.id.btn_admin_login);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnAdminLogin.setOnClickListener(this);
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
