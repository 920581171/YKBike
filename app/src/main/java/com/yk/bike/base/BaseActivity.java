package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.CommonResponse;

import cn.smssdk.gui.util.Const;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public void showShort(String text){
        if (mToast==null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public void showLong(String text){
        if (mToast==null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_LONG);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public boolean isEmptyString(String s){
        return "".equals(s);
    }

    public boolean isResponseSuccess(CommonResponse commonResponse){
        return Consts.COMMON_RESPONSE_SUCCESS_MSG.equals(commonResponse.getMsg());
    }
}
