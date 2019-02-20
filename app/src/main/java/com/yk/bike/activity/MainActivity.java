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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yk.bike.R;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.fragment.AboutFragment;
import com.yk.bike.fragment.AdminInfoListFragment;
import com.yk.bike.fragment.BikeInfoFragment;
import com.yk.bike.fragment.BikeRecordFragment;
import com.yk.bike.fragment.DepositFragment;
import com.yk.bike.fragment.MapFragment;
import com.yk.bike.fragment.MessageBroadListFragment;
import com.yk.bike.fragment.SiteLocationFragment;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.BikeRecordResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.service.LocationService;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.NullObjectUtils;
import com.yk.bike.utils.SharedPreferencesUtils;
import com.yk.bike.websocket.WebSocketManager;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final int FRAGMENT_MAP = 0;
    public final int FRAGMENT_BIKE_INFO = 1;
    public final int FRAGMENT_ABOUT = 2;
    public final int FRAGMENT_ADMIN_INFO = 3;
    public final int FRAGMENT_BIKE_RECORD = 4;
    public final int FRAGMENT_SITE_LOCATION = 5;
    public final int FRAGMENT_DEPOSIT = 6;
    public final int FRAGMENT_MESSAGE_BROAD = 7;

    private int currentFragmentNum = 0;

    private static final String TAG = "MainActivity";

    private BaseFragment[] fragments;

    private FloatingActionButton fab;

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
                        login();
                        WebSocketManager.getInstance().init(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID));
                        break;
                    case Consts.BR_ACTION_LOGOUT:
                        logout();
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
        intentFilter.addAction(Consts.BR_ACTION_EXIT);
        intentFilter.addAction(Consts.BR_ACTION_LOGIN);
        intentFilter.addAction(Consts.BR_ACTION_LOGOUT);
        registerReceiver(br, intentFilter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startQRCode();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        initFragment();

        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        switchFragment(FRAGMENT_MAP);
    }

    public void initFragment() {
        fragments = new BaseFragment[8];
        fragments[FRAGMENT_MAP] = new MapFragment();
        fragments[FRAGMENT_BIKE_INFO] = new BikeInfoFragment();
        fragments[FRAGMENT_ABOUT] = new AboutFragment();
        fragments[FRAGMENT_ADMIN_INFO] = new AdminInfoListFragment();
        fragments[FRAGMENT_BIKE_RECORD] = new BikeRecordFragment();
        fragments[FRAGMENT_SITE_LOCATION] = new SiteLocationFragment();
        fragments[FRAGMENT_DEPOSIT] = new DepositFragment();
        fragments[FRAGMENT_MESSAGE_BROAD] = new MessageBroadListFragment();

        getSupportFragmentManager().getFragments().clear();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        for (Fragment f : fragments) {
            if (f instanceof MapFragment) {
                fragmentTransaction.add(R.id.ll_fragment, f);
                continue;
            }
            fragmentTransaction.add(R.id.ll_fragment, f).hide(f);
        }
        fragmentTransaction.commit();
    }

    public void refreshFragment() {
        if (fragments == null) {
            initFragment();
            return;
        }

        for (BaseFragment f : fragments)
            f.initData();
    }

    public void login() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        int avatarId;

        if (Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE))) {
            menu.setGroupVisible(R.id.group_user, false);
            menu.setGroupVisible(R.id.group_admin, true);
            avatarId = R.drawable.admin;
        } else {
            menu.setGroupVisible(R.id.group_user, true);
            menu.setGroupVisible(R.id.group_admin, false);
            avatarId = R.drawable.avatar;
        }

        View view = navigationView.getHeaderView(0);
        CircleImageView cvAvatar = view.findViewById(R.id.cv_avatar);
        TextView tvId = view.findViewById(R.id.tv_id);

        Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(avatarId)
                        //禁用内存缓存
                        .skipMemoryCache(true)
                        //硬盘缓存功能
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(UrlConsts.HEADIPORT + UrlConsts.GET_COMMON_DOWNLOAD_AVATAR + "?id=" + SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID))
                .into(cvAvatar);

        tvId.setText(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID));

        refreshFragment();

        initRecord();
    }

    public void initRecord() {
        ApiUtils.getInstance().findBikeRecordIsCycling(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<BikeRecordResponse>() {
            @Override
            public void onSuccess(BikeRecordResponse bikeRecordResponse) {
                if (isResponseSuccess(bikeRecordResponse)) {
                    ((MapFragment) fragments[FRAGMENT_MAP]).startBike(bikeRecordResponse.getData());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        WebSocketManager.getInstance().disconnect();
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

        switch (id) {
            case R.id.action_add:
                if (currentFragmentNum == FRAGMENT_MAP) {
                    ((MapFragment) fragments[FRAGMENT_MAP]).sitePlan();
                } else if (currentFragmentNum == FRAGMENT_MESSAGE_BROAD) {
                    startActivity(new Intent(MainActivity.this, MessageBroadActivity.class));
                }
                break;
            case R.id.action_forward:
                switchFragment(FRAGMENT_MAP);
                break;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_map:
                ((MapFragment) switchFragment(FRAGMENT_MAP)).initBikeLocation();
                break;
            case R.id.nav_record:
                switchFragment(FRAGMENT_BIKE_RECORD).initData();
                break;
            case R.id.nav_admin:
                switchFragment(FRAGMENT_ADMIN_INFO).initData();
                break;
            case R.id.nav_bike_info:
                switchFragment(FRAGMENT_BIKE_INFO).initData();
                break;
            case R.id.nav_site_plan:
                switchFragment(FRAGMENT_SITE_LOCATION).initData();
                break;
            case R.id.nav_deposit:
                switchFragment(FRAGMENT_DEPOSIT).initData();
                break;
            //        else if (id == R.id.nav_count) { }
            case R.id.nav_message_board_sender:
            case R.id.nav_message_board_handler:
                switchFragment(FRAGMENT_MESSAGE_BROAD).initData();
                break;
            case R.id.nav_account:
                startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), Consts.REQUEST_CODE_ACCOUNT);
                break;
            case R.id.nav_info:
                switchFragment(FRAGMENT_ABOUT).initData();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == Consts.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String qrCode = data.getStringExtra(Constant.CODED_CONTENT);
                String content = new String(Base64.decode(qrCode, Base64.DEFAULT));
                if (!content.contains("bike"))
                    showShort("不是正确的二维码");
                else
                    ApiUtils.getInstance().findBikeByBikeId(content, new ResponseListener<BikeInfoResponse>() {
                        @Override
                        public void onError(String errorMsg) {
                            showShort(errorMsg);
                        }

                        @Override
                        public void onSuccess(BikeInfoResponse bikeInfoResponse) {
                            onQRCodeResult(content, bikeInfoResponse);
                        }
                    });
            }
        } else if (requestCode == Consts.REQUEST_CODE_ACCOUNT && resultCode == Consts.RESULT_CODE_LOGOUT) {
            logout();
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
        startActivityForResult(intent, Consts.REQUEST_CODE_SCAN);
    }

    public void onQRCodeResult(String content, BikeInfoResponse bikeInfoResponse) {
        String type;
        String title;
        String message;
        String[] buttonText;
        AMapLocation aMapLocation = binder.getAMapLocation();
        if (Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.get(Consts.SP_STRING_LOGIN_TYPE, ""))) {
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
        showAlertDialog(title, message, buttonText, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                switch (type) {
                    case Consts.CODE_RESULT_TYPE_ADMIN_ADDED:
                        showShort("查看");
                        break;
                    case Consts.CODE_RESULT_TYPE_ADMIN_NEW_ADD:
                        ApiUtils.getInstance().addBikeInfo(content, aMapLocation.getLatitude(), aMapLocation.getLongitude(),
                                new ResponseListener<CommonResponse>() {
                                    @Override
                                    public void onError(String errorMsg) {
                                        showShort(errorMsg);
                                    }

                                    @Override
                                    public void onSuccess(CommonResponse commonResponse) {
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
                        ApiUtils.getInstance().updateBikeInfo(bikeInfo, new ResponseListener<CommonResponse>() {
                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }

                            @Override
                            public void onSuccess(CommonResponse commonResponse) {
                                if (isResponseSuccess(commonResponse))
                                    showShort("维修完成");
                                else
                                    showShort(commonResponse.getMsg());
                            }
                        });
                        break;
                    case Consts.CODE_RESULT_TYPE_USER_BIKE:
                        BikeRecordResponse.BikeRecord bikeRecord = new BikeRecordResponse.BikeRecord()
                                .setBikeId(bikeInfoResponse.getData().getBikeId())
                                .setUserId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID));

                        ApiUtils.getInstance().addBikeRecord(bikeRecord, new ResponseListener<BikeRecordResponse>() {
                            @Override
                            public void onSuccess(BikeRecordResponse bikeRecordResponse) {
                                if (isResponseSuccess(bikeRecordResponse)) {
                                    ((MapFragment) fragments[FRAGMENT_MAP]).startBike(bikeRecordResponse.getData());
                                    showShort("开始骑行");
                                } else {
                                    showShort(bikeRecordResponse.getMsg());
                                }
                            }

                            @Override
                            public void onError(String errorMsg) {
                                showShort(errorMsg);
                            }
                        });
                        break;
                    case Consts.CODE_RESULT_TYPE_USER_FIX:
                    case Consts.CODE_RESULT_TYPE_USER_NO_BIKE:
                        startQRCode();
                        break;
                }
            }

            @Override
            public void onNeutralClick(DialogInterface dialog, int which) {
                if (Consts.CODE_RESULT_TYPE_ADMIN_ADDED.equals(type) || Consts.CODE_RESULT_TYPE_USER_BIKE.equals(type)) {
                    BikeInfoResponse.BikeInfo bikeInfo = bikeInfoResponse.getData();
                    bikeInfo.setUserId("")
                            .setLatitude(aMapLocation.getLatitude())
                            .setLongitude(aMapLocation.getLongitude())
                            .setFix("1");
                    ApiUtils.getInstance().updateBikeInfo(bikeInfo, new ResponseListener<CommonResponse>() {
                        @Override
                        public void onError(String errorMsg) {
                            showShort(errorMsg);
                        }

                        @Override
                        public void onSuccess(CommonResponse commonResponse) {
                            if (isResponseSuccess(commonResponse))
                                showShort("报修成功");
                            else
                                showShort(commonResponse.getMsg());
                        }
                    });
                }
            }
        });
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public MainActivity setFab(FloatingActionButton fab) {
        this.fab = fab;
        return this;
    }

    public void logout() {
        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_ID, "");
        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_NAME, "");
        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_PHONE, "");
        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_PASSWORD, "");
        SharedPreferencesUtils.put(Consts.SP_STRING_LOGIN_TYPE, "");
        SharedPreferencesUtils.put(Consts.SP_STRING_ORDER_ID, "");
        WebSocketManager.getInstance().disconnect();
        switchFragment(FRAGMENT_ABOUT);
        startActivity(new Intent(this, LoginActivity.class));
    }

    public BaseFragment switchFragment(int newFragment) {
        if (newFragment == FRAGMENT_MAP && NullObjectUtils.isEmptyString(SharedPreferencesUtils.getString(Consts.SP_STRING_ORDER_ID)))
            fab.show();
        else
            fab.hide();

        super.switchFragment(fragments[newFragment], fragments[currentFragmentNum]);
        currentFragmentNum = newFragment;
        return fragments[newFragment];
    }

    public BaseFragment getFragment(int position) {
        return position < fragments.length ?
                fragments[position] :
                fragments[fragments.length - 1];
    }

    public AMapLocation getAMapLocation() {
        return binder.getAMapLocation();
    }

    public void addOnServiceTimeListener(LocationService.OnServiceTimeListener
                                                 onServiceTimeListener) {
        binder.addOnServiceTimeListener(onServiceTimeListener);
    }

    public void removeOnServiceTimeListener(LocationService.OnServiceTimeListener
                                                    onServiceTimeListener) {
        binder.removeOnServiceTimeListener(onServiceTimeListener);
    }
}

