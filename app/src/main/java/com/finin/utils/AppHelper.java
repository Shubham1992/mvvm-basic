package com.finin.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.finin.R;

public class AppHelper {

    public static void setStatusBarColor(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(context.getColor(R.color.colorPrimaryDark));
            } else
                window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static DisplayMetrics getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics;

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
