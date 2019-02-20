package com.yk.bike.fragment;

import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.BikeRecordAdapter;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.BikeRecordListResponse;
import com.yk.bike.utils.ApiUtils;

public class BikeRecordFragment extends BaseRecyclerFragment<MainActivity> {
    @Override
    public void initData() {
        ApiUtils.getInstance().findAllBikeRecord(new ResponseListener<BikeRecordListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(BikeRecordListResponse bikeRecordListResponse) {
                if (isResponseSuccess(bikeRecordListResponse)) {
                    onDataChange(new BikeRecordAdapter(bikeRecordListResponse.getData()));
                }
            }
        });
    }
}
