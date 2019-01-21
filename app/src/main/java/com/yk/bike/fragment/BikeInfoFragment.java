package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.bike.R;
import com.yk.bike.adapter.BikeInfoAdapter;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.base.OnAlertDialogButtonClickListener;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;

public class BikeInfoFragment extends BaseFragment {

    private View mRootView;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_bikeinfo, container, false);
        init();
        return mRootView;
    }

    public void init() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);

        initRecyclerView();
    }

    private void initRecyclerView() {
        ApiUtils.getInstance().findAllBikeInfo(new OnBaseResponseListener<BikeInfoListResponse>() {
            @Override
            public void onError() {
                showShort(getResources().getString(R.string.string_network_error));
            }

            @Override
            public void onResponse(BikeInfoListResponse bikeInfoListResponse) {
                if (isResponseSuccess(bikeInfoListResponse)) {
                    BikeInfoAdapter adapter = new BikeInfoAdapter(bikeInfoListResponse.getData());
                    adapter.setOnItemClickListener(new BikeInfoAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(BikeInfoAdapter.ViewHolder holder, int position) {

                        }

                        @Override
                        public void onLongClick(BikeInfoAdapter.ViewHolder holder, int position) {
                            showAlertDialog("删除自行车", "是否删除自行车？", new String[]{"删除", "取消"}, new OnAlertDialogButtonClickListener() {
                                @Override
                                public void positiveClick() {
                                    deleteBike(holder);
                                }

                                @Override
                                public void negativeClick() {

                                }

                                @Override
                                public void neutralClick() {

                                }

                                @Override
                                public void onCancel(DialogInterface dialog) {

                                }

                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                }
                            });
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void deleteBike(BikeInfoAdapter.ViewHolder holder) {

        if (getResources().getString(R.string.string_status_using).equals(holder.status.getText().toString())) {
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
                                ApiUtils.getInstance().deleteBikeInfo(holder.bikeId.getText().toString(), new OnBaseResponseListener<CommonResponse>() {
                                    @Override
                                    public void onError() {
                                        showShort(getResources().getString(R.string.string_network_error));
                                    }

                                    @Override
                                    public void onResponse(CommonResponse commonResponse) {
                                        if (isResponseSuccess(commonResponse)) {
                                            showShort("删除成功！");
                                        }
                                    }
                                });
                        }
                    }).show();
    }
}
