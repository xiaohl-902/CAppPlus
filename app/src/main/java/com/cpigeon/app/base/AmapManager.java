package com.cpigeon.app.base;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;

/**
 * 高德地图操作管理工具
 * Created by Zhu TingYu on 2018/3/30.
 */

public class AmapManager {
    AMap aMap;
    public AmapManager(AMap aMap){
        this.aMap = aMap;
    }

    public void moveByLatLng(double la, double lo){
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(la, lo)));
    }

    public void moveByLatLng(LatLng latLng){
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

}
