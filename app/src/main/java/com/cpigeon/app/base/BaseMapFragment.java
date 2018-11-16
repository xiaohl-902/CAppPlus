package com.cpigeon.app.base;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;

/**
 * Created by Zhu TingYu on 2018/5/30.
 */

public abstract class BaseMapFragment extends BaseMVPFragment{

   protected MapMarkerManager markerManager;
    protected MapView mapView;
    protected AMap aMap;

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mapView = findViewById(R.id.map);
        mapView.onCreate(state);
        if(aMap == null){
            aMap = mapView.getMap();
        }

        markerManager = new MapMarkerManager(aMap, getActivity());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_map_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}
