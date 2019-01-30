package com.yk.bike.base;

import android.content.DialogInterface;

public interface OnAlertDialogListener extends DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    void positiveClick(DialogInterface dialog, int which);
    void negativeClick(DialogInterface dialog, int which);
    void neutralClick(DialogInterface dialog, int which);
}
