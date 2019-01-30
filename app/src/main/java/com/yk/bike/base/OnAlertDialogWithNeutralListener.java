package com.yk.bike.base;

import android.content.DialogInterface;

public interface OnAlertDialogWithNeutralListener extends OnAlertDialogWithNegativeListener {
    void neutralClick(DialogInterface dialog, int which);
}
