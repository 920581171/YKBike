package com.yk.bike.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
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
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.SiteLocationListResponse;
import com.yk.bike.response.SiteLocationResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.BitmapCache;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.widght.SitePlanView;

import java.time.chrono.IsoChronology;
import java.util.List;

public class MapFragment extends BaseFragment implements AMap.OnInfoWindowClickListener, View.OnClickListener {

    private static final String TAG = "MapFragment";

    private final float ZOOM = 15;

    private MainActivity mainActivity;

    private MapView mMapView;
    private AMap mAMap;

    @Override
    public int initLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        ImageView showLocation = rootView.findViewById(R.id.show_location);
        ImageView sitePlan = rootView.findViewById(R.id.site_plan);
        ImageView ivEnlarge = rootView.findViewById(R.id.iv_enlarge);
        ImageView ivNarrow = rootView.findViewById(R.id.iv_narrow);
        SitePlanView sitePlanView = rootView.findViewById(R.id.sitePlanView);

        showLocation.setOnClickListener(this);
        sitePlan.setOnClickListener(this);
        ivEnlarge.setOnClickListener(this);
        ivNarrow.setOnClickListener(this);

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
                            new String[]{"添加", "取消"}, (DialogInterface dialog, int which) -> {
                                ApiUtils.getInstance().addSiteLocation(latLng.latitude, latLng.longitude, radius, new OnBaseResponseListener<CommonResponse>() {
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
                            });
                }
                sitePlanView.reset();
                sitePlanView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelClick() {
                Log.d(TAG, "onCancelClick: ");
                sitePlanView.reset();
                sitePlanView.setVisibility(View.GONE);
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
                AMapLocation aMapLocation = mainActivity.getAMapLocation();
                animateCamera(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            });

            mAMap.setOnInfoWindowClickListener(this);
        }
    }

    public void initBikeLocation() {
        ApiUtils.getInstance().findAllBikeInfo(new OnBaseResponseListener<BikeInfoListResponse>() {
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
                            if (!Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.getString(Consts.SP_LOGIN_TYPE)) &&
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
    }

    public void initSite() {
        ApiUtils.getInstance().findAllSiteLocation(new OnBaseResponseListener<SiteLocationListResponse>() {
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

    public void showBikeLocation(BikeInfoResponse.BikeInfo bikeInfo) {
        if (mAMap != null) {
            mAMap.clear();

            mainActivity.switchFragment(mainActivity.FRAGMENT_MAP);

            animateCamera(bikeInfo.getLatitude(), bikeInfo.getLongitude());

            LatLng latLng = new LatLng(bikeInfo.getLatitude(), bikeInfo.getLongitude());
            String snippet = "1".equals(bikeInfo.getFix()) ?
                    getResources().getString(R.string.string_status_fix) :
                    bikeInfo.getUserId() == null || "".equals(bikeInfo.getBikeId()) ?
                            getResources().getString(R.string.string_status_unused) :
                            getResources().getString(R.string.string_status_using);

            Bitmap bitmap = "1".equals(bikeInfo.getFix()) ?
                    BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_off,
                            getResources().getColor(R.color.colorAccent, null)) :
                    bikeInfo.getUserId() == null || "".equals(bikeInfo.getUserId()) ?
                            BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_on,
                                    getResources().getColor(R.color.colorAccent, null)) :
                            BitmapCache.getBitmapByDrawableWithColor(R.drawable.ic_location_user,
                                    getResources().getColor(R.color.colorAccent, null));

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
        circleOptions.fillColor(ContextCompat.getColor(mainActivity, R.color.colorAccent_50));
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

    public void clearAMap() {
        if (mAMap != null)
            mAMap.clear();
    }

    public LatLng getLatLng() {
        if (mAMap != null)
            return new LatLng(mAMap.getMyLocation().getLatitude(), mAMap.getMyLocation().getLongitude());
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            mainActivity = (MainActivity) context;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    public String getApprovalNumber() {
        if (mAMap != null)
            return mAMap.getMapContentApprovalNumber();
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String bikeId = marker.getTitle().replace(getString(R.string.string_show_bike_id), "");
        String[] s = marker.getSnippet().equals(getString(R.string.string_status_fix)) ?
                new String[]{"更新位置", "重置信息", "删除"} :
                new String[]{"更新位置", "需要维修", "删除"};
        showAlertDialogList("修改信息", null, s, (dialog, which) -> {
            switch (which) {
                case 0:
                    ApiUtils.getInstance().updateBikeLocation(bikeId, getLatLng().latitude, getLatLng().longitude, new OnBaseResponseListener<CommonResponse>() {
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
                    ApiUtils.getInstance().updateBikeFix(bikeId, fix, new OnBaseResponseListener<CommonResponse>() {
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
                                            ApiUtils.getInstance().deleteBikeInfo(bikeId, new OnBaseResponseListener<CommonResponse>() {
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
        });
        Log.d(TAG, "onMarkerClick: " + bikeId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_location:
                animateCamera(getLatLng());
                break;
            case R.id.site_plan:
                SitePlanView sitePlanView = getmRootView().findViewById(R.id.sitePlanView);
                sitePlanView.setScalePerPixel(mAMap.getScalePerPixel());
                sitePlanView.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_enlarge:
                cameraEnlarge();
                break;
            case R.id.iv_narrow:
                cameraNarrow();
                break;
        }
    }
}
