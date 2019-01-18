package com.yk.bike.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yk.bike.R;
import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.BaseResponse;
import com.yk.bike.response.CommonResponse;

import cn.smssdk.gui.util.Const;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public void showShort(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public void showLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public void showAlertDialog(String title, String message, String[] buttonText, OnAlertDialogButtonClickListener onAlertDialogButtonClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();

        if (buttonText.length > 3)
            buttonText = new String[]{buttonText[0], buttonText[1], buttonText[2]};

        if (onAlertDialogButtonClickListener != null) {
            switch (buttonText.length) {
                case 3:
                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, buttonText[2], (dialog, which) -> {
                        onAlertDialogButtonClickListener.neutralClick();
                    });
                case 2:
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonText[1], (dialog, which) -> {
                        onAlertDialogButtonClickListener.negativeClick();
                    });
                case 1:
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonText[0], (dialog, which) -> {
                        onAlertDialogButtonClickListener.positiveClick();
                    });
                case 0:
                    break;
            }

            alertDialog.setOnDismissListener(onAlertDialogButtonClickListener);
            alertDialog.setOnCancelListener(onAlertDialogButtonClickListener);

        }

        alertDialog.show();
    }

    public boolean isEmptyString(String s) {
        return "".equals(s);
    }

    public boolean isResponseSuccess(BaseResponse baseResponse) {
        return Consts.COMMON_RESPONSE_SUCCESS_MSG.equals(baseResponse.getMsg());
    }

    public void sendBroadcast(String action) {
        sendBroadcast(new Intent().setAction(action));
    }

    public void sendBroadcast(String[] actions) {
        Intent intent = new Intent();
        for (String a : actions) {
            intent.setAction(a);
        }
        sendBroadcast(intent);
    }
}
