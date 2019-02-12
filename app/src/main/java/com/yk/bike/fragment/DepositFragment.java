package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.DepositAdapter;
import com.yk.bike.adapter.OnItemClickListener;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.base.OnAlertDialogListener;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.UserInfoListResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.MainHandler;

import java.util.List;

public class DepositFragment extends BaseFragment<MainActivity> {

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
        ApiUtils.getInstance().findDeposit(new ResponseListener<UserInfoListResponse>() {
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(UserInfoListResponse userInfoListResponse) {
                if (isResponseSuccess(userInfoListResponse)) {
                    List<UserInfoResponse.UserInfo> userInfos = userInfoListResponse.getData();
                    DepositAdapter adapter = new DepositAdapter(userInfos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            showAlertDialogList("处理申请", null, new String[]{"同意申请", "拒绝申请", "取消"}, new AlertDialogListener() {
                                @Override
                                public void onPositiveClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            ApiUtils.getInstance().updateUserInfo(userInfos.get(position).setDeposit(0), new ResponseListener<UserInfoResponse>() {
                                                @Override
                                                public void onSuccess(UserInfoResponse userInfoResponse) {
                                                    if (isResponseSuccess(userInfoResponse)) {
                                                        showShort("同意申请");
                                                    } else {
                                                        showShort(userInfoResponse.getMsg());
                                                    }
                                                }
                                            });
                                            break;
                                        case 1:
                                            ApiUtils.getInstance().updateUserInfo(userInfos.get(position).setDeposit(199), new ResponseListener<UserInfoResponse>() {
                                                @Override
                                                public void onSuccess(UserInfoResponse userInfoResponse) {
                                                    if (isResponseSuccess(userInfoResponse)) {
                                                        showShort("拒绝申请");
                                                    } else {
                                                        showShort(userInfoResponse.getMsg());
                                                    }
                                                }
                                            });
                                            break;
                                    }
                                }
                            });
                        }

                        @Override
                        public void onLongClick(View v, RecyclerView.ViewHolder holder, int position) {

                        }

                        @Override
                        public void onRightButtonClick(View v, RecyclerView.ViewHolder holder, Object o) {

                        }
                    });
                } else {
                    showShort(userInfoListResponse.getMsg());
                }
            }
        });
    }
}
