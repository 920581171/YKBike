package com.yk.bike.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yk.bike.utils.MainHandler;

public class AdminInfoListFragment extends BaseFragment {

    private static final String TAG = "AdminInfoListFragment";

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public int initLayout() {
        return R.layout.fragment_recycler;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            MainHandler.getInstance().postDelayed(this::initData, 500);
        });
    }

    @Override
    public void initData() {
        ApiUtils.getInstance().findAllAdminInfo(new ResponseListener<AdminInfoListResponse>() {
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(AdminInfoListResponse adminInfoListResponse) {
                if (isResponseSuccess(adminInfoListResponse)) {
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
}
