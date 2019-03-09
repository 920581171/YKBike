package com.yk.bike.fragment;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.BikeRecordResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.SiteLocationListResponse;
import com.yk.bike.response.SiteLocationResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.service.LocationService;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.BitmapCache;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.utils.SpUtils;
import com.yk.bike.websocket.WebSocketLoaction;
import com.yk.bike.websocket.WebSocketManager;
import com.yk.bike.websocket.WebSocketMessage;
import com.yk.bike.widght.SitePlanView;

import java.text.SimpleDateFormat;
import java.util.List;

public class MapFragment extends BaseFragment<MainActivity> implements AMap.OnInfoWindowClickListener, View.OnClickListener {

    private static final String TAG = "MapFragment";

    private final float ZOOM = 15;

    private MapView mMapView;
    private ImageView sitePlan;
    private AMap mAMap;

    private TextView tvShowBikeTime;
    private TextView tvStopBike;

    private LocationService.OnServiceTimeListener onServiceTimeListener;

    @Override
    public int initLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        ImageView showLocation = rootView.findViewById(R.id.show_location);
        sitePlan = rootView.findViewById(R.id.site_plan);
        ImageView ivEnlarge = rootView.findViewById(R.id.iv_enlarge);
        ImageView ivNarrow = rootView.findViewById(R.id.iv_narrow);
        SitePlanView sitePlanView = rootView.findViewById(R.id.sitePlanView);
        tvShowBikeTime = rootView.findViewById(R.id.tv_show_bike_time);
        tvStopBike = rootView.findViewById(R.id.tv_stop_bike);

        tvShowBikeTime.setVisibility(View.GONE);
        tvStopBike.setVisibility(View.GONE);

        showLocation.setOnClickListener(this);
        sitePlan.setOnClickListener(this);
        ivEnlarge.setOnClickListener(this);
        ivNarrow.setOnClickListener(this);
        tvStopBike.setOnClickListener(this);

        initMap(savedInstanceState);

        sitePlanView.setOnSitePlanClickListener(new SitePlanView.OnSitePlanClickListener() {
            @Override
            public void onCheckClick() {
                Log.d(TAG, "onCheckClick: ");
                if (mAMap != null) {
                    int radius = (int) (sitePlanView.getRadius() * mAMap.getScalePerPixel());
                    LatLng latLng = pointToLatLng(sitePlanView.getCx(), sitePlanView.getCy());
                    showAlertDialog("添加站点",
                            "纬度：" + latLng.latitude + "\n经度：" + latLng.longitude + "\n半径范围：" + radius + "m",
                            new String[]{"添加", "取消"}, new AlertDialogListener() {
                                @Override
                                public void onPositiveClick(DialogInterface dialog, int which) {
                                    ApiUtils.getInstance().addSiteLocation(latLng.latitude, latLng.longitude, radius, new ResponseListener<CommonResponse>() {
                                        @Override
                                        public void onError(String errorMsg) {
                                            showShort(errorMsg);
                                        }

                                        @Override
                                        public void onSuccess(CommonResponse commonResponse) {
                                            if (isResponseSuccess(commonResponse)) {
                                                showShort("添加成功");
                                                setSitePlan(latLng, radius);
                                            }
                                        }
                                    });
                                }
                            });
                }
                sitePlanView.reset();
                sitePlanView.setVisibility(View.GONE);
                initSite();
            }

            @Override
            public void onCancelClick() {
                Log.d(TAG, "onCancelClick: ");
                sitePlanView.reset();
                sitePlanView.setVisibility(View.GONE);
                initBikeLocation();
            }
        });
    }

    @Override
    public void initData() {
        initBikeLocation();
    }

    public void initMap(Bundle savedInstanceState) {
        mMapView = getmRootView().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();

        MyLocationStyle mMyLocationStyle = new MyLocationStyle();
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        mMyLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        if (mAMap != null) {
            mAMap.setMyLocationStyle(mMyLocationStyle);//设置定位蓝点的Style
            //            mAMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
            mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            UiSettings mUiSettings = mAMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setScaleControlsEnabled(true);

            mAMap.setOnMapLoadedListener(() -> {
                AMapLocation aMapLocation = getActivityContext().getAMapLocation();
                animateCamera(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            });

            mAMap.setOnInfoWindowClickListener(this);
        }
    }

    public void initBikeLocation() {
        ApiUtils.getInstance().findAllBikeInfo(new ResponseListener<BikeInfoListResponse>() {
            @Override
            public void onError(String errorMsg) {
                showShort(errorMsg);
            }

            @Override
            public void onSuccess(BikeInfoListResponse bikeInfoListResponse) {
                if (isResponseSuccess(bikeInfoListResponse)) {
                    List<BikeInfoResponse.BikeInfo> list = bikeInfoListResponse.getData();

                    if (mAMap != null) {
                        mAMap.clear();
                        for (BikeInfoResponse.BikeInfo b : list) {
                            String userId = b.getUserId();
                            String bikeId = b.getBikeId();
                            String fix = b.getFix();
                            /*如果不是管理员*/
                            if (!Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE)) &&
                                    ((userId != null && !"".equals(userId)) || fix == null || "1".equals(fix)))
                                continue;
                            LatLng latLng = new LatLng(b.getLatitude(), b.getLongitude());
                            String snippet = "1".equals(b.getFix()) ?
                                    getResources().getString(R.string.string_status_fix) :
                                    userId == null || "".equals(userId) ?
                                            getResources().getString(R.string.string_status_unused) :
                                            getResources().getString(R.string.string_status_using);

                            Bitmap bitmap = "1".equals(b.getFix()) ?
                                    BitmapCache.getInstance().getLocationOff() :
                                    userId == null || "".equals(userId) ?
                                            BitmapCache.getInstance().getLocationOn() :
                                            BitmapCache.getInstance().getLocationUser();

                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .title(getString(R.string.string_show_bike_id) + bikeId)
                                    .snippet(snippet)
                                    .position(latLng)
                                    .icon(bitmapDescriptor);
                            mAMap.addMarker(markerOptions);
                        }
                    }
                }
            }
        });

        sitePlan.setSelected(false);
    }

    public void initSite() {
        ApiUtils.getInstance().findAllSiteLocation(new ResponseListener<SiteLocationListResponse>() {
            @Override
            public void onError(String errorMsg) {
                showShort(errorMsg);
            }

            @Override
            public void onSuccess(SiteLocationListResponse siteLocationListResponse) {
                if (isResponseSuccess(siteLocationListResponse)) {
                    List<SiteLocationResponse.SiteLocation> locations = siteLocationListResponse.getData();

                    if (mAMap != null) {
                        mAMap.clear();
                        for (SiteLocationResponse.SiteLocation s : locations) {
                            LatLng latLng = new LatLng(s.getLatitude(), s.getLongitude());
                            setSitePlan(latLng, s.getRadius());
                        }
                    }
                }
            }
        });
    }

    /**
     * 管理员显示车辆位置
     *
     * @param bikeInfo
     */
    public void showBikeLocation(BikeInfoResponse.BikeInfo bikeInfo) {
        if (mAMap != null) {
            mAMap.clear();

            getActivityContext().switchFragment(getActivityContext().FRAGMENT_MAP);

            animateCamera(bikeInfo.getLatitude(), bikeInfo.getLongitude());

            LatLng latLng = new LatLng(bikeInfo.getLatitude(), bikeInfo.getLongitude());
            String snippet = "1".equals(bikeInfo.getFix()) ?
                    getResources().getString(R.string.string_status_fix) :
                    bikeInfo.getUserId() == null || "".equals(bikeInfo.getBikeId()) ?
                            getResources().getString(R.string.string_status_unused) :
                            getResources().getString(R.string.string_status_using);

            Bitmap bitmap = "1".equals(bikeInfo.getFix()) ?
                    BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_off,
                            ContextCompat.getColor(getActivityContext(), R.color.colorAccent)) :
                    bikeInfo.getUserId() == null || "".equals(bikeInfo.getUserId()) ?
                            BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_on,
                                    ContextCompat.getColor(getActivityContext(), R.color.colorAccent)) :
                            BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_user,
                                    ContextCompat.getColor(getActivityContext(), R.color.colorAccent));

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(getString(R.string.string_show_bike_id) + bikeInfo.getBikeId())
                    .snippet(snippet)
                    .position(latLng)
                    .icon(bitmapDescriptor);
            mAMap.addMarker(markerOptions);
        }
    }

    public Point latLngToPoint(LatLng latLng) {
        if (mAMap != null) {
            Projection projection = mAMap.getProjection();
            return projection.toScreenLocation(latLng);
        }
        return null;
    }

    public LatLng pointToLatLng(int x, int y) {
        if (mAMap != null) {
            Point point = new Point(x, y);
            Projection projection = mAMap.getProjection();
            return projection.fromScreenLocation(point);
        }
        return null;
    }

    public void setSitePlan(LatLng latLng, double radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.fillColor(ContextCompat.getColor(getActivityContext(), R.color.colorAccent_50));
        circleOptions.strokeWidth(0f);
        mAMap.addCircle(circleOptions);
    }

    public void animateCamera(double latitude, double longitude) {
        if (mAMap != null) {
            mAMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM)));
        }
    }

    public void animateCamera(LatLng latLng) {
        mAMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng, ZOOM)));
    }

    public void cameraEnlarge() {
        if (mAMap != null) {
            mAMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    public void cameraNarrow() {
        if (mAMap != null) {
            mAMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    /**
     * 清空地图
     */
    public void clearAMap() {
        if (mAMap != null)
            mAMap.clear();
    }

    /**
     * 获得当前定位位置
     *
     * @return
     */
    public LatLng getLatLng() {
        if (mAMap != null)
            return new LatLng(mAMap.getMyLocation().getLatitude(), mAMap.getMyLocation().getLongitude());
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView != null)
            mMapView.onResume();

        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null)
            mMapView.onPause();

        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    public String getApprovalNumber() {
        if (mAMap != null)
            return mAMap.getMapContentApprovalNumber();
        return null;
    }

    /**
     * 车辆信息点击事件
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN))
            return;
        String bikeId = marker.getTitle().replace(getString(R.string.string_show_bike_id), "");
        String[] s = marker.getSnippet().equals(getString(R.string.string_status_fix)) ?
                new String[]{"更新位置", "重置信息", "删除"} :
                new String[]{"更新位置", "需要维修", "删除"};
        showAlertDialogList("修改信息", null, s, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ApiUtils.getInstance().updateBikeLocation(bikeId, getLatLng().latitude, getLatLng().longitude, new ResponseListener<CommonResponse>() {
                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }

                            @Override
                            public void onSuccess(CommonResponse commonResponse) {
                                if (isResponseSuccess(commonResponse)) {
                                    showShort("更新成功");
                                    initBikeLocation();
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
                                    initBikeLocation();
                                } else {
                                    showShort(commonResponse.getMsg());
                                }
                            }
                        });
                        break;
                    case 2:
                        if (marker.getSnippet().equals(getString(R.string.string_status_using))) {
                            showShort("车辆正在使用中！");
                            break;
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
                                                ApiUtils.getInstance().deleteBikeInfo(bikeId, new ResponseListener<CommonResponse>() {
                                                    @Override
                                                    public void onError(String errorMsg) {
                                                        showShort(errorMsg);
                                                    }

                                                    @Override
                                                    public void onSuccess(CommonResponse commonResponse) {
                                                        if (isResponseSuccess(commonResponse)) {
                                                            showShort("删除成功！");
                                                            initBikeLocation();
                                                        }
                                                    }
                                                });
                                        }
                                    }).show();
                        break;
                }
            }
        });
        Log.d(TAG, "onMarkerClick: " + bikeId);
    }

    /**
     * 站点规划
     */
    public void sitePlan() {
        SitePlanView sitePlanView = getmRootView().findViewById(R.id.sitePlanView);
        sitePlanView.setScalePerPixel(mAMap.getScalePerPixel());
        sitePlanView.setVisibility(View.VISIBLE);
        initSite();
    }

    public void startBike(BikeRecordResponse.BikeRecord bikeRecord) {
        tvShowBikeTime.setVisibility(View.VISIBLE);
        tvStopBike.setVisibility(View.VISIBLE);

        onServiceTimeListener = serviceTime -> {
            String[] subTimes = subTime(serviceTime, bikeRecord.getCreateTime().getTime());
            String time = subTimes[0].equals("") ?
                    subTimes[1] + ":" + subTimes[2] :
                    subTimes[0] + ":" + subTimes[1] + ":" + subTimes[2];
            tvShowBikeTime.setText(time);

            LatLng latLng = getLatLng();

            WebSocketLoaction webSocketLoaction = new WebSocketLoaction()
                    .setUserId(SpUtils.getLoginId())
                    .setLatitude(latLng.latitude)
                    .setLongitude(latLng.longitude);

            WebSocketMessage webSocketMessage = new WebSocketMessage()
                    .setType(Consts.WEBSOCKET_TYPE_LOCATION)
                    .setData(GsonUtils.toJson(webSocketLoaction));

            WebSocketManager.getInstance().sendText(GsonUtils.toJson(webSocketMessage));
        };

        SharedPreferencesUtils.put(Consts.SP_STRING_ORDER_ID, bikeRecord.getOrderId());

        getActivityContext().addOnServiceTimeListener(onServiceTimeListener);
        getActivityContext().getFab().hide();

        initBikeLocation();
    }

    public void stopBike() {
        ApiUtils.getInstance().finishBike(SharedPreferencesUtils.getString(Consts.SP_STRING_ORDER_ID), getLatLng().latitude, getLatLng().longitude, new ResponseListener<BikeRecordResponse>() {
            @Override
            public void onSuccess(BikeRecordResponse bikeRecordResponse) {
                if (isResponseSuccess(bikeRecordResponse)) {
                    BikeRecordResponse.BikeRecord bikeRecord = bikeRecordResponse.getData();
                    String[] subTimes = subTime(bikeRecord.getEndTime().getTime(), bikeRecord.getCreateTime().getTime());
                    String msg = "你骑行了" + (subTimes[0].equals("") ?
                            subTimes[1] + "分" + subTimes[2] + "秒" :
                            subTimes[0] + "小时" + subTimes[1] + "分" + subTimes[2] + "秒") + "\n"
                            + "里程：" + bikeRecord.getMileage() + "米" + "\n"
                            + "计费：" + bikeRecord.getCharge() + "元" + "\n"
                            + "你获得了" + (int)(bikeRecord.getCharge() * 100) + "积分";
                    showAlertDialog("结束骑行", msg, new String[]{"确定"}, new AlertDialogListener() {
                        @Override
                        public void onPositiveClick(DialogInterface dialog, int which) {
                            showShort("骑行结束");
                            getActivityContext().getFab().show();
                            SharedPreferencesUtils.put(Consts.SP_STRING_ORDER_ID, "");
                            tvShowBikeTime.setVisibility(View.GONE);
                            tvStopBike.setVisibility(View.GONE);
                            getActivityContext().removeOnServiceTimeListener(onServiceTimeListener);
                            onServiceTimeListener = null;
                            initBikeLocation();

                            WebSocketLoaction webSocketLoaction = new WebSocketLoaction()
                                    .setUserId(SpUtils.getLoginId());

                            WebSocketMessage webSocketMessage = new WebSocketMessage()
                                    .setType(Consts.WEBSOCKET_TYPE_STOP_LOCATION)
                                    .setData(GsonUtils.toJson(webSocketLoaction));

                            WebSocketManager.getInstance().sendText(GsonUtils.toJson(webSocketMessage));
                        }
                    });
                } else {
                    showShort(bikeRecordResponse.getMsg());
                }
            }
        });
    }

    public void checkStop() {
        ApiUtils.getInstance().findAllSiteLocation(new ResponseListener<SiteLocationListResponse>() {
            @Override
            public void onSuccess(SiteLocationListResponse siteLocationListResponse) {
                if (isResponseSuccess(siteLocationListResponse)) {
                    List<SiteLocationResponse.SiteLocation> list = siteLocationListResponse.getData();
                    for (SiteLocationResponse.SiteLocation siteLocation : list) {
                        LatLng latLng = new LatLng(siteLocation.getLatitude(), siteLocation.getLongitude());
                        float distance = AMapUtils.calculateLineDistance(getLatLng(), latLng);
                        if (distance < siteLocation.getRadius()) {
                            stopBike();
                            return;
                        }
                    }
                    showAlertDialog("无法结束", "不在停车区域内", new String[]{"确定"}, new AlertDialogListener());
                } else {
                    showShort(siteLocationListResponse.getMsg());
                }
            }
        });
    }

    public String[] subTime(long after, long before) {
        String[] subTimes = new String[3];
        long subTime = (after - before) / 1000;
        long h = subTime / 60 / 60;
        long m = subTime / 60 % 60;
        long s = subTime % 60;

        String hh = h <= 0 ? "" : String.valueOf(h);
        String mm = m < 10 ? "0" + m : String.valueOf(m);
        String ss = s < 10 ? "0" + s : String.valueOf(s);

        subTimes[0] = hh;
        subTimes[1] = mm;
        subTimes[2] = ss;

        return subTimes;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_location:
                animateCamera(getLatLng());
                break;
            case R.id.site_plan:
                if (sitePlan.isSelected()) {
                    initBikeLocation();
                    sitePlan.setSelected(false);
                } else {
                    initSite();
                    sitePlan.setSelected(true);
                }
                break;
            case R.id.iv_enlarge:
                cameraEnlarge();
                break;
            case R.id.iv_narrow:
                cameraNarrow();
                break;
            case R.id.tv_stop_bike:
                showAlertDialog("结束骑行", "是否结束骑行？", new String[]{"结束", "取消"}, new AlertDialogListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        ApiUtils.getInstance().findUserByUserId(SpUtils.getLoginId(), new ResponseListener<UserInfoResponse>() {
                            @Override
                            public void onSuccess(UserInfoResponse userInfoResponse) {
                                if (isResponseSuccess(userInfoResponse)) {
                                    if (userInfoResponse.getData().getScore() >= 500) {
                                        stopBike();
                                    } else {
                                        showAlertDialog("提示", "您不是会员，需要在指定地点停车", new String[]{"停车", "取消"}, new AlertDialogListener() {
                                            @Override
                                            public void onPositiveClick(DialogInterface dialog, int which) {
                                                checkStop();
                                            }
                                        });
                                    }
                                } else {
                                    showShort(userInfoResponse.getMsg());
                                }
                            }
                        });
                    }
                });
                break;
        }
    }
}
