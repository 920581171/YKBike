package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public void showShort(String text){
        if (mToast==null){
            mToast = new Toast(this);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public void showLong(String text){
        if (mToast==null){
            mToast = new Toast(this);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
}
