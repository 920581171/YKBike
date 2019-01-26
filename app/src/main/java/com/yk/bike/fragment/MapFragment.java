package com.yk.bike.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.yk.bike.R;
import com.yk.bike.activity.MainActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.widght.SitePlanView;

import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class MapFragment extends BaseFragment implements View.OnClickListener {

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
                    setSitePlan(sitePlanView.getCx(), sitePlanView.getCy(), radius);
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

                    Drawable vectorDrawable = mainActivity.getResources().getDrawable(R.drawable.ic_location_on, null);
                    Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                            vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    vectorDrawable.draw(canvas);

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
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .title(getString(R.string.string_show_bike_id) + bikeId)
                                    .position(latLng)
                                    .icon(bitmapDescriptor);
                            mAMap.addMarker(markerOptions);
                        }
                    }
                }
            }
        });
    }

    public void showBikeLocation(double latitude, double longitude) {
        if (mAMap != null) {
            mAMap.clear();

            mainActivity.switchFragment(mainActivity.FRAGMENT_MAP);

            animateCamera(latitude, longitude);

            Drawable vectorDrawable = mainActivity.getResources().getDrawable(R.drawable.ic_location_on, null);
            vectorDrawable.mutate().setTint(getResources().getColor(R.color.colorAccent, null));
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(bitmapDescriptor);
            mAMap.addMarker(markerOptions);
        }
    }

    public void setZOOM() {
        Projection projection = mAMap.getProjection();
        Point point = projection.toScreenLocation(getLatLng());
        Log.d(TAG, "setZOOM: " + point.x + "----" + point.y);
    }

    public void setSitePlan(int x, int y, double radius) {
        if (mAMap != null) {
            Point point = new Point(x, y);
            Projection projection = mAMap.getProjection();
            LatLng latLng = projection.fromScreenLocation(point);
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.fillColor(ContextCompat.getColor(mainActivity,R.color.colorAccent_50));
            circleOptions.strokeWidth(0f);
            mAMap.addCircle(circleOptions);
        }
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
