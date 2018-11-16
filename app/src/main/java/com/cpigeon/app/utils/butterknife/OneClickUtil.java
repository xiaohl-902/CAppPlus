package com.cpigeon.app.utils.butterknife;

import java.util.Calendar;

/**
 * 解决ButterKnife 短时间内重复点击
 * Created by Administrator on 2018/2/7.
 */
public class OneClickUtil {
    private String methodName;
    public static final int MIN_CLICK_DELAY_TIME = 1000*1;
    private long lastClickTime = 0;

    public OneClickUtil(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean check() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }
}