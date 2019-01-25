package com.yk.bike.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
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

import java.util.List;

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
        ImageView ivEnlarge = rootView.findViewById(R.id.iv_enlarge);
        ImageView ivNarrow = rootView.findViewById(R.id.iv_narrow);

        showLocation.setOnClickListener(this);
        ivEnlarge.setOnClickListener(this);
        ivNarrow.setOnClickListener(this);

        initMap(savedInstanceState);
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

            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Log.d(TAG, "onCameraChange: " + cameraPosition.zoom);
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, getResources().getDisplayMetrics());
                    Log.d(TAG, "onCameraChangeFinish: " + px);
                    Log.d(TAG, "onCameraChangeFinish: " + mAMap.getScalePerPixel() * px);
                }
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

    public void animateCamera(double latitude, double longitude) {
        if (mAMap != null) {
            mAMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM)));
        }
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

    public String getApprovalNumber(){
        if (mAMap!=null)
            return mAMap.getMapContentApprovalNumber();
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_location:
                AMapLocation aMapLocation = mainActivity.getAMapLocation();
                animateCamera(aMapLocation.getLatitude(), aMapLocation.getLongitude());
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
