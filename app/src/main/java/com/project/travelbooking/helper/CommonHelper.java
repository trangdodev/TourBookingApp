package com.project.travelbooking.helper;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class CommonHelper {
    public static void HideKeyboard(InputMethodManager imm) {
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }
}
