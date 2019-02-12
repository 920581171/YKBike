package com.yk.bike.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.BikeInfoAdapter;
import com.yk.bike.adapter.OnItemClickListener;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.base.OnAlertDialogListener;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MainHandler;

import java.util.List;

public class BikeInfoFragment extends BaseFragment<MainActivity> {

    private static final String TAG = "BikeInfoFragment";

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
            MainHandler.getInstance().postDelayed(this::initData,500);
        });
    }

    @Override
    public void initData() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        ApiUtils.getInstance().findAllBikeInfo(new ResponseListener<BikeInfoListResponse>() {
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String errorMsg) {
                showShort(errorMsg);
            }

            @Override
            public void onSuccess(BikeInfoListResponse bikeInfoListResponse) {
                if (isResponseSuccess(bikeInfoListResponse)) {
                    List<BikeInfoResponse.BikeInfo> list = bikeInfoListResponse.getData();
                    BikeInfoAdapter adapter = new BikeInfoAdapter(list);
                    adapter.setOnItemClickListener(new OnItemClickListener<BikeInfoResponse.BikeInfo>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            onItemClick(list.get(position));
                        }

                        @Override
                        public void onLongClick(View v, RecyclerView.ViewHolder holder, int position) {
                            showAlertDialog("删除自行车", "是否删除自行车？",
                                    new String[]{"删除", "取消"},
                                    new AlertDialogListener() {
                                        @Override
                                        public void onPositiveClick(DialogInterface dialog, int which) {
                                            deleteBike(list.get(position));
                                        }
                                    });
                        }

                        @Override
                        public void onRightButtonClick(View v, RecyclerView.ViewHolder holder, BikeInfoResponse.BikeInfo bikeInfo) {
                            MapFragment mapFragment = (MapFragment) getActivityContext().getFragment(getActivityContext().FRAGMENT_MAP);
                            mapFragment.showBikeLocation(bikeInfo);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void deleteBike(BikeInfoResponse.BikeInfo bikeInfo) {

        if (bikeInfo.getUserId() != null && !"".equals(bikeInfo.getUserId())) {
            showShort("车辆正在使用中！");
            return;
        }

        if (getActivity() != null)
            Snackbar.make(getActivity().findViewById(R.id.fab), "车辆即将删除", Snackbar.LENGTH_LONG)
                    .setAction("撤销", v -> {
                        showShort("撤销删除");
                    })
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                                ApiUtils.getInstance().deleteBikeInfo(bikeInfo.getBikeId(), new ResponseListener<CommonResponse>() {
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

    public void onItemClick(BikeInfoResponse.BikeInfo bikeInfo) {
        String bikeId = bikeInfo.getBikeId();
        String[] s = bikeInfo.getFix().equals("1") ?
                new String[]{"更新位置", "重置信息", "删除"} :
                new String[]{"更新位置", "需要维修", "删除"};
        showAlertDialogList("修改信息", null, s, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ApiUtils.getInstance().updateBikeLocation(bikeId, bikeInfo.getLatitude(), bikeInfo.getLongitude(), new ResponseListener<CommonResponse>() {
                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }

                            @Override
                            public void onSuccess(CommonResponse commonResponse) {
                                if (isResponseSuccess(commonResponse)) {
                                    showShort("更新成功");
                                    initData();
                                } else {
                                    showShort(commonResponse.getMsg());
                                }
                            }
                        });
                        break;
                    case 1:
                        String fix = s[1].equals("重置信息") ? "0" : "1";
                        ApiUtils.getInstance().updateBikeFix(bikeId, fix, new ResponseListener<CommonResponse>() {
                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }

                            @Override
                            public void onSuccess(CommonResponse commonResponse) {
                                if (isResponseSuccess(commonResponse)) {
                                    showShort("提交成功");
                                    initData();
                                } else {
                                    showShort(commonResponse.getMsg());
                                }
                            }
                        });
                        break;
                    case 2:
                        deleteBike(bikeInfo);
                        break;
                }
            }
        });
    }
}
