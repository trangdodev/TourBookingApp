package com.project.travelbooking.helper;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class CommonHelper {
    public static void HideKeyboard(InputMethodManager imm) {
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }

    public static String FormatCurrency(double price) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

        return format.format(price);
    }

    public static String FormatDate(Date date){
        Locale locale = new Locale("vi_VN");
        Locale.setDefault(locale);
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
        return df.format(date);
    }
}
