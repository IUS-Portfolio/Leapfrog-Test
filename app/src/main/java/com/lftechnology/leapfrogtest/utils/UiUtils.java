package com.lftechnology.leapfrogtest.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class UiUtils {

    private UiUtils() {

    }

    /**
     * Displays a toast message
     *
     * @param context Context used to show toast
     * @param message Toast Message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String extractString(TextView textView) {
        return textView.getText().toString().trim();
    }
}
