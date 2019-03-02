package com.yk.bike.fragment;

import com.yk.bike.activity.AccountActivity;
import com.yk.bike.adapter.ScoreRecordAdapter;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.ScoreRecordListResponse;
import com.yk.bike.response.ScoreRecordResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SpUtils;

import java.util.List;

public class ScoreRecordFragment extends BaseRecyclerFragment<AccountActivity> {
    @Override
    public void initData() {
        ApiUtils.getInstance().findScoreRecordByUserId(SpUtils.getLoginId(), new ResponseListener<ScoreRecordListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(ScoreRecordListResponse scoreRecordListResponse) {
                if (isResponseSuccess(scoreRecordListResponse)) {
                    List<ScoreRecordResponse.ScoreRecord> scoreRecords = scoreRecordListResponse.getData();
                    ScoreRecordAdapter adapter = new ScoreRecordAdapter(scoreRecords);
                    onDataChange(adapter);
                } else {
                    showShort(scoreRecordListResponse.getMsg());
                }
            }
        });
    }
}
