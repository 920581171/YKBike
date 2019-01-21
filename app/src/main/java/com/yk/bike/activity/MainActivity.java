package com.yk.bike.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.yk.bike.R;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.base.OnAlertDialogButtonClickListener;
import com.yk.bike.callback.OnBaseResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.BikeInfoFragment;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.service.LocationService;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private int REQUEST_CODE_SCAN = 0;

    private LocationService.LocationBinder binder;

    private ServiceConnection connection;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null)
                switch (action) {
                    case Consts.BR_ACTION_EXIT:
                        finish();
                        break;
                    case Consts.BR_ACTION_LOGIN:
                        Log.d(TAG, "onReceive: login");
                        init();
                        break;
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.BR_ACTION_LOGIN);
        intentFilter.addAction(Consts.BR_ACTION_EXIT);
        registerReceiver(br, intentFilter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startQRCode();
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startActivity(new Intent(this, LoginActivity.class));
    }

    public void init() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MainActivity.this.binder = (LocationService.LocationBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(MainActivity.this, LocationService.class), connection, BIND_AUTO_CREATE);

//        replaceFragment(R.id.ll_main,new MapFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        if (connection != null)
            unbindService(connection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            // Handle the camera action
        } else if (id == R.id.nav_admin) {

        } else if (id == R.id.nav_count) {
            replaceFragment(R.id.ll_main,new BikeInfoFragment());
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_info) {
            SharedPreferencesUtils.put(Consts.SP_LOGIN_ID, "");
            SharedPreferencesUtils.put(Consts.SP_LOGIN_NAME, "");
            SharedPreferencesUtils.put(Consts.SP_LOGIN_PASSWORD, "");
            SharedPreferencesUtils.put(Consts.SP_LOGIN_TYPE, "");
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String qrCode = data.getStringExtra(Constant.CODED_CONTENT);
                String content = new String(Base64.decode(qrCode, Base64.DEFAULT));
                if (!content.contains("bike"))
                    showShort("不是正确的二维码");
                else
                    ApiUtils.getInstance().findBikeByBikeId(content, new OnBaseResponseListener<BikeInfoResponse>() {
                        @Override
                        public void onError() {
                            showShort(getResources().getString(R.string.string_network_error));
                        }

                        @Override
                        public void onResponse(BikeInfoResponse bikeInfoResponse) {
                            onQRCodeResult(content, bikeInfoResponse);
                        }
                    });
            }
        }
    }

    public void startQRCode() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorPrimary);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorPrimary);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorPrimary);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        config.setShowAlbum(false);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    public void onQRCodeResult(String content, BikeInfoResponse bikeInfoResponse) {
        String type;
        String title;
        String message;
        String[] buttonText;
        AMapLocation aMapLocation = binder.getAMapLocation();
        if (Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.get(Consts.SP_LOGIN_TYPE, ""))) {
            if (isResponseSuccess(bikeInfoResponse)) {
                if ("0".equals(bikeInfoResponse.getData().getFix())) {
                    title = "自行车已添加";
                    message = "车辆编号：" + content;
                    buttonText = new String[]{"查看", "取消", "需要维修"};
                    type = Consts.CODE_RESULT_TYPE_ADMIN_ADDED;
                } else {
                    title = "自行车正在维修中";
                    message = "车辆编号：" + content;
                    buttonText = new String[]{"维修完毕", "取消"};
                    type = Consts.CODE_RESULT_TYPE_ADMIN_FIX;
                }
            } else {
                title = "添加自行车";
                message = "添加车辆编号：" + content + "到\n" +
                        binder.getAMapLocation().getAddress() + "？";
                buttonText = new String[]{"添加", "取消"};
                type = Consts.CODE_RESULT_TYPE_ADMIN_NEW_ADD;
            }
        } else {
            if (isResponseSuccess(bikeInfoResponse)) {
                if ("0".equals(bikeInfoResponse.getData().getFix())) {
                    title = "扫码成功";
                    message = "车辆可用，编号：" + content;
                    buttonText = new String[]{"立即骑行", "取消", "报修"};
                    type = Consts.CODE_RESULT_TYPE_USER_BIKE;
                } else {
                    title = "扫码成功";
                    message = "车辆正在维修，编号：" + content;
                    buttonText = new String[]{"扫下一辆", "取消"};
                    type = Consts.CODE_RESULT_TYPE_USER_FIX;
                }
            } else {
                title = "扫码失败";
                message = "车辆不存在";
                buttonText = new String[]{"扫下一辆", "取消"};
                type = Consts.CODE_RESULT_TYPE_USER_NO_BIKE;
            }
        }
        showAlertDialog(title, message, buttonText, new OnAlertDialogButtonClickListener() {
            @Override
            public void positiveClick() {
                switch (type) {
                    case Consts.CODE_RESULT_TYPE_ADMIN_ADDED:
                        showShort("查看");
                        break;
                    case Consts.CODE_RESULT_TYPE_ADMIN_NEW_ADD:
                        ApiUtils.getInstance().addBikeInfo(content, aMapLocation.getLatitude(), aMapLocation.getLongitude(),
                                new OnBaseResponseListener<CommonResponse>() {
                                    @Override
                                    public void onError() {
                                        showShort(getResources().getString(R.string.string_network_error));
                                    }

                                    @Override
                                    public void onResponse(CommonResponse commonResponse) {
                                        if (isResponseSuccess(commonResponse))
                                            showShort("添加成功");
                                        else
                                            showShort(commonResponse.getMsg());
                                    }
                                });
                        break;
                    case Consts.CODE_RESULT_TYPE_ADMIN_FIX:
                        BikeInfoResponse.BikeInfo bikeInfo = bikeInfoResponse.getData();
                        bikeInfo.setUserId("")
                                .setLatitude(aMapLocation.getLatitude())
                                .setLongitude(aMapLocation.getLongitude())
                                .setFix("0");
                        ApiUtils.getInstance().updateBikeInfo(bikeInfo, new OnBaseResponseListener<CommonResponse>() {
                            @Override
                            public void onError() {
                                showShort(getResources().getString(R.string.string_network_error));
                            }

                            @Override
                            public void onResponse(CommonResponse commonResponse) {
                                if (isResponseSuccess(commonResponse))
                                    showShort("维修完成");
                                else
                                    showShort(commonResponse.getMsg());
                            }
                        });
                        break;
                    case Consts.CODE_RESULT_TYPE_USER_BIKE:
                        break;
                    case Consts.CODE_RESULT_TYPE_USER_FIX:
                    case Consts.CODE_RESULT_TYPE_USER_NO_BIKE:
                        startQRCode();
                        break;
                }
            }

            @Override
            public void negativeClick() {
                Log.d(TAG, "negativeClick: ");
            }

            @Override
            public void neutralClick() {
                if (Consts.CODE_RESULT_TYPE_ADMIN_ADDED.equals(type) || Consts.CODE_RESULT_TYPE_USER_BIKE.equals(type)) {
                    BikeInfoResponse.BikeInfo bikeInfo = bikeInfoResponse.getData();
                    bikeInfo.setUserId("")
                            .setLatitude(aMapLocation.getLatitude())
                            .setLongitude(aMapLocation.getLongitude())
                            .setFix("1");
                    ApiUtils.getInstance().updateBikeInfo(bikeInfo, new OnBaseResponseListener<CommonResponse>() {
                        @Override
                        public void onError() {
                            showShort(getResources().getString(R.string.string_network_error));
                        }

                        @Override
                        public void onResponse(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse))
                                showShort("报修成功");
                            else
                                showShort(commonResponse.getMsg());
                        }
                    });
                }
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "onDismiss: ");
            }
        });
    }
}

