package com.yk.bike.base;

import android.content.DialogInterface;

public interface OnAlertDialogWithNegativeListener extends OnAlertDialogPositiveListener {
    void negativeClick(DialogInterface dialog, int which);
}
