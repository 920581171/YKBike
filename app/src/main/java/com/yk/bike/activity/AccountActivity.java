package com.yk.bike.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.MenuItem;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yk.bike.R;
import com.yk.bike.base.AlertDialogListener;
import com.yk.bike.base.BaseActivity;
import com.yk.bike.base.BaseFragment;
import com.yk.bike.callback.ResponseListener;
import com.yk.bike.constant.Consts;
import com.yk.bike.fragment.AdminInfoFragment;
import com.yk.bike.fragment.UserInfoFragment;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.utils.ApiUtils;
import com.yk.bike.utils.FileUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.Objects;


public class AccountActivity extends BaseActivity {

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        replaceFragment(R.id.ll_fragment,
                SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE).equals(Consts.LOGIN_TYPE_ADMIN) ?
                        new AdminInfoFragment() :
                        new UserInfoFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSelectAvatar() {
        showAlertDialogList("修改头像", null, new String[]{"拍照", "从相册中选"}, new AlertDialogListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File out = new File(FileUtils.getDefaultCache(), "CameraImage.jpg");
                        Uri uri = Build.VERSION.SDK_INT >= 24 ?
                                FileProvider.getUriForFile(AccountActivity.this, "com.yk.bike", out) :
                                Uri.fromFile(out);
                        // 获取拍照后未压缩的原图片，并保存在uri路径中
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, Consts.REQUEST_CODE_FROM_CAMERA);
                        break;
                    case 1:
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, Consts.REQUEST_CODE_FROM_ALBUM);
                        break;
                }
            }
        });
    }

    @Override
    public void replaceFragment(int layoutId, BaseFragment fragment) {
        currentFragment = fragment;
        super.replaceFragment(layoutId, fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //从相册选择
            case Consts.REQUEST_CODE_FROM_ALBUM: {
                if (resultCode == RESULT_CANCELED) { //取消操作
                    Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
                    break;
                }
                Uri imageUri = data.getData();
                Uri destinationUri = Uri.fromFile(new File(FileUtils.getDefaultCache(), "CropImage.jpg"));

                UCrop.of(imageUri, destinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(512, 512)
                        .start(this, UCrop.REQUEST_CROP);
                break;
            }

            //拍照
            case Consts.REQUEST_CODE_FROM_CAMERA: {
                if (resultCode == RESULT_CANCELED) { //取消操作
                    Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
                    break;
                }

                Uri imageUri = Uri.fromFile(new File(FileUtils.getDefaultCache(), "CameraImage.jpg"));
                Uri destinationUri = Uri.fromFile(new File(FileUtils.getDefaultCache(), "CropImage.jpeg"));

                UCrop.of(imageUri, destinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(512, 512)
                        .start(this, UCrop.REQUEST_CROP);
                break;
            }

            //裁剪成功
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_CANCELED) { //取消操作
                    Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
                    break;
                }
                Uri cropUri = UCrop.getOutput(data);
                ApiUtils.getInstance().uploadAvatar(new File(Objects.requireNonNull(cropUri).getPath()), SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID), new ResponseListener<CommonResponse>() {
                    @Override
                    public void onSuccess(CommonResponse commonResponse) {
                        if (isResponseSuccess(commonResponse)) {
                            showShort("上传成功");
                            if (currentFragment != null)
                                currentFragment.initData();
                        } else {
                            showShort(commonResponse.getMsg());
                        }
                    }
                });
                break;

            //裁剪失败
            case UCrop.RESULT_ERROR:
                Toast.makeText(this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
