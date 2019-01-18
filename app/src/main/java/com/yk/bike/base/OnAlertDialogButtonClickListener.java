package com.yk.bike.base;

import android.content.DialogInterface;

public interface OnAlertDialogButtonClickListener extends DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    void positiveClick();

    void negativeClick();

    void neutralClick();
}
