package com.yk.bike.base;

import android.content.DialogInterface;

public interface OnAlertDialogListener extends DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    void onPositiveClick(DialogInterface dialog, int which);
    void onNegativeClick(DialogInterface dialog, int which);
    void onNeutralClick(DialogInterface dialog, int which);
}
