package com.cpigeon.app.modular.matchlive.model.bean;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Zhu TingYu on 2018/3/12.
 */

public class LocationEntity {
   double la;//纬度，GPS坐标
   double lo;//经度，GPS坐标

    public LatLng get(){
        return new LatLng(la, lo);
    }
}
