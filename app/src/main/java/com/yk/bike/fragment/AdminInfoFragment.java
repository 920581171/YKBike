package com.yk.bike.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.AdminInfoAdapter;
import com.yk.bike.adapter.OnItemClickListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.AdminInfoListResponse;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.utils.ApiUtils;

public class AdminInfoFragment extends BaseFragment {

    private static final String TAG = "AdminInfoFragment";

    MainActivity mainActivity;

    RecyclerView recyclerView;

    @Override
    public int initLayout() {
        return R.layout.fragment_recycler;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findAllAdminInfo(new ResponseListener<AdminInfoListResponse>() {
            @Override
            public void onSuccess(AdminInfoListResponse adminInfoListResponse) {
                if (isResponseSuccess(adminInfoListResponse)){
                    AdminInfoAdapter adapter = new AdminInfoAdapter(adminInfoListResponse.getData());
                    adapter.setOnItemClickListener(new OnItemClickListener<AdminInfoResponse.AdminInfo>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public void onLongClick(View v, RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public void onRightButtonClick(View v, RecyclerView.ViewHolder holder, AdminInfoResponse.AdminInfo adminInfo) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + adminInfo.getAdminPhone());
                            intent.setData(data);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            mainActivity = (MainActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
