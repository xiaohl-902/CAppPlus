package com.cpigeon.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Window;

import com.cpigeon.app.R;


/**
 * 自定义dialog 可以设置圆角
 * Created by Administrator on 2018/3/29.
 */

public class CustomAlertDialog2 extends Dialog {

    public CustomAlertDialog2(@NonNull Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public CustomAlertDialog2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public CustomAlertDialog2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = displayWidth;    //宽度设置为屏幕的0.55
        p.height = displayHeight;    //高度设置为屏幕的0.28
        getWindow().setAttributes(p);     //设置生效

        this.getWindow().setBackgroundDrawableResource(R.drawable.dialog_corner_bg2);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
