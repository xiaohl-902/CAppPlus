package com.cpigeon.app.utils.butterknife;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决ButterKnife 短时间内重复点击
 * Created by Administrator on 2018/2/7.
 */

public class AntiShake {

    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static AntiShake instance = null;


    /*加上synchronized，但是每次调用实例时都会加载**/
/*3.双重锁定:只在第一次初始化的时候加上同步锁*/
    public static AntiShake getInstance() {
        if (instance == null) {
            synchronized (AntiShake.class) {
                if (instance == null) {
                    instance = new AntiShake();
                }
            }
        }
        return instance;
    }

    private List<OneClickUtil> utils = new ArrayList<>();

    public boolean check(Object o) {
        String flag = null;
        if (o == null)
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        else
            flag = o.toString();
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }

    public boolean check() {
        return check(null);
    }
}