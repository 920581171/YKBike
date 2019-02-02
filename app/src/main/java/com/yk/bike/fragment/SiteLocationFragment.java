package com.yk.bike.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.OnItemClickListener;
import com.yk.bike.adapter.SiteLocationAdapter;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.SiteLocationListResponse;
import com.yk.bike.response.SiteLocationResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MainHandler;

import java.util.List;

public class SiteLocationFragment extends BaseFragment<MainActivity> {

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
        ApiUtils.getInstance().findAllSiteLocation(new ResponseListener<SiteLocationListResponse>() {
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(SiteLocationListResponse siteLocationListResponse) {
                if (isResponseSuccess(siteLocationListResponse)) {
                    List<SiteLocationResponse.SiteLocation> siteLocations = siteLocationListResponse.getData();
                    SiteLocationAdapter adapter = new SiteLocationAdapter(siteLocations);
                    adapter.setOnItemClickListener(new OnItemClickListener<SiteLocationResponse.SiteLocation>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            siteSetting(siteLocations.get(position));
                        }

                        @Override
                        public void onLongClick(View v, RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public void onRightButtonClick(View v, RecyclerView.ViewHolder holder, SiteLocationResponse.SiteLocation siteLocation) {

                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void siteSetting(SiteLocationResponse.SiteLocation siteLocation) {
        showAlertDialog("删除站点", "是否删除该站点？", new String[]{"删除", "取消"}, new AlertDialogListener() {
            @Override
            public void positiveClick(DialogInterface dialog, int which) {
                Snackbar.make(getActivity().findViewById(R.id.fab), "站点即将删除", Snackbar.LENGTH_LONG)
                        .setAction("撤销", v -> {
                            showShort("撤销删除");
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                                    ApiUtils.getInstance().deleteSiteLocation(siteLocation.getSiteId(), new ResponseListener<CommonResponse>() {
                                        @Override
                                        public void onError(String errorMsg) {
                                            showShort(errorMsg);
                                        }

                                        @Override
                                        public void onSuccess(CommonResponse commonResponse) {
                                            if (isResponseSuccess(commonResponse)) {
                                                showShort("删除成功！");
                                                initData();
                                            }
                                        }
                                    });
                            }
                        }).show();
            }
        });
    }
}
