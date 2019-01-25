package com.yk.bike.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.BikeInfoAdapter;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.base.OnAlertDialogButtonClickListener;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;

public class BikeInfoFragment extends BaseFragment {

    private static final String TAG = "BikeInfoFragment";

    private MainActivity mainActivity;

    private RecyclerView recyclerView;

    @Override
    public int initLayout() {
        return R.layout.fragment_bikeinfo;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    @Override
    public void initData() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        ApiUtils.getInstance().findAllBikeInfo(new OnBaseResponseListener<BikeInfoListResponse>() {
            @Override
            public void onError(String errorMsg) {
                showShort(errorMsg);
            }

            @Override
            public void onSuccess(BikeInfoListResponse bikeInfoListResponse) {
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

                        @Override
                        public void onShowInMapClick(BikeInfoAdapter.ViewHolder holder, BikeInfoResponse.BikeInfo bikeInfo) {
                            MapFragment mapFragment = (MapFragment) mainActivity.getFragment(mainActivity.FRAGMENT_MAP);
                            mapFragment.showBikeLocation(bikeInfo.getLatitude(), bikeInfo.getLongitude());
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            mainActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
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
                                    public void onError(String errorMsg) {
                                        showShort(errorMsg);
                                    }

                                    @Override
                                    public void onSuccess(CommonResponse commonResponse) {
                                        if (isResponseSuccess(commonResponse)) {
                                            showShort("删除成功！");
                                        }
                                    }
                                });
                        }
                    }).show();
    }
}
