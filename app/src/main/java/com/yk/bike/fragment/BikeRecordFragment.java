package com.yk.bike.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.adapter.BikeRecordAdapter;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.BikeRecordListResponse;
import com.yk.bike.response.BikeRecordResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MainHandler;

import java.util.List;

public class BikeRecordFragment extends BaseFragment {

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
        ApiUtils.getInstance().findAllBikeRecord(new ResponseListener<BikeRecordListResponse>() {
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(BikeRecordListResponse bikeRecordListResponse) {
                if (isResponseSuccess(bikeRecordListResponse)) {
                    BikeRecordAdapter adapter = new BikeRecordAdapter(bikeRecordListResponse.getData());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
