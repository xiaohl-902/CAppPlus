package com.cpigeon.app.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chenshuai on 2017/4/12.
 */

public class ToastUtil {
    private static Toast toast = null;

    public static void showToast(Context context, int retId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), retId, duration);
        } else {
            toast.setText(retId);
            toast.setDuration(duration);
        }
        toast.show();
    }


    public static void showToast(Context context, String hint, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), hint, duration);
        } else {
            toast.setText(hint);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void showShortToast(Context context, String hint) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), hint, Toast.LENGTH_SHORT);
        } else {
            toast.setText(hint);
        }
        toast.show();
    }

    public static void showLongToast(Context context, String hint) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), hint, Toast.LENGTH_LONG);
        } else {
            toast.setText(hint);
        }
        toast.show();
    }


}
