package com.project.travelbooking.helper;


import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelper {
    public static void showSnackBar(View view, String message, int messageType) {
        Snackbar snackbar = Snackbar
                .make(view, "message", Snackbar.LENGTH_SHORT);
        switch (messageType) {
            // Error snack bar
            case 1: {
                snackbar.setBackgroundTint(Color.valueOf(179, 64, 69).toArgb());
                break;
            }
            // Success snack bar
            case 2: {
                snackbar.setBackgroundTint(Color.valueOf(45, 136, 77).toArgb());
                break;
            }
            // Warning snack bar
            case 3: {
                snackbar.setBackgroundTint(Color.valueOf(254, 207, 109).toArgb());
                break;
            }
            // Info snack bar
            default: {
                snackbar.setBackgroundTint(Color.valueOf(64, 145, 215).toArgb());
                break;
            }
        }
        snackbar.show();
    }
}
