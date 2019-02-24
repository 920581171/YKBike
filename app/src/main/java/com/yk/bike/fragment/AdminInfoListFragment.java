package com.yk.bike.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yk.bike.activity.ChatActivity;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.adapter.AdminInfoAdapter;
import com.yk.bike.adapter.OnItemClickListener;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseRecyclerFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.AdminInfoListResponse;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.utils.ApiUtils;

import java.util.List;

public class AdminInfoListFragment extends BaseRecyclerFragment<MainActivity> {

    private static final String TAG = "AdminInfoListFragment";

    @Override
    public void initData() {
        ApiUtils.getInstance().findAllAdminInfo(new ResponseListener<AdminInfoListResponse>() {
            @Override
            public void onFinish() {
                onDataFinish();
            }

            @Override
            public void onSuccess(AdminInfoListResponse adminInfoListResponse) {
                if (isResponseSuccess(adminInfoListResponse)) {
                    List<AdminInfoResponse.AdminInfo> adminInfos = adminInfoListResponse.getData();
                    AdminInfoAdapter adapter = new AdminInfoAdapter(adminInfos);
                    adapter.setOnItemClickListener(new OnItemClickListener<AdminInfoResponse.AdminInfo>() {
                        @Override
                        public void onClick(View v, RecyclerView.ViewHolder holder, int position) {
                            if ("1".equals(adminInfos.get(position).getAdminOnline()))
                                startActivity(new Intent(getActivityContext(), ChatActivity.class).putExtra(Consts.INTENT_STRING_TO_ID, adminInfos.get(position).getAdminId()));
                            else
                                showAlertDialog("管理员离线", "请联系其他管理员或直接拨打联系电话", new String[]{"确定"}, new AlertDialogListener());
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
                    onDataChange(adapter);
                }
            }
        });
    }
}
