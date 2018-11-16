package com.cpigeon.app.circle;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class LocationManager {

    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;

    AMapLocationListener locationListener;

    public LocationManager(Context context) {

        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听


        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);


        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);


        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);


        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        //mLocationOption.setLocationCacheEnable(true);


//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void star() {
        mLocationClient.setLocationListener(locationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    public LocationManager setLocationListener(AMapLocationListener locationListener) {
        this.locationListener = locationListener;
        return this;
    }

    public AMapLocationListener getAMapLocationListener() {
        return this.locationListener;
    }

    public AMapLocationClient getAMapLocationClient() {
        return this.mLocationClient;
    }
}
