package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cpigeon.app.R;
import com.cpigeon.app.base.AmapManager;
import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.circle.presenter.CircleNearbyPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleNearbyEntity;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;
import com.cpigeon.app.utils.BitmapUtil;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.view.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zhu TingYu on 2018/5/28.
 */

public class CircleNearbyFragment extends BaseMVPFragment<CircleNearbyPre>{

    MapMarkerManager markerManager;
    MapView mapView;
    AMap aMap;
    AmapManager amapManager;
    CircleImageView head;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_map_layout;
    }

    @Override
    protected CircleNearbyPre initPresenter() {
        return new CircleNearbyPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("附近鸽友");
        mapView = findViewById(R.id.map);
        mapView.onCreate(state);
        if(aMap == null){
            aMap = mapView.getMap();
        }
        amapManager = new AmapManager(aMap);

        markerManager = new MapMarkerManager(aMap, getActivity());
        showLoading();
        new LocationManager(getActivity()).setLocationListener(aMapLocation -> {
            mPresenter.province = aMapLocation.getProvince();
            mPresenter.getNearby(circleNearbyEntities -> {
                hideLoading();
                if(circleNearbyEntities.isEmpty()){
                    error("附近没有鸽友");
                }else {
                    initView();
                }
            });
        }).star();
    }

    private void initView() {
        head = findViewById(R.id.head);
        for(CircleNearbyEntity entity :  mPresenter.data){
           markerManager.addCustomMarker(new LatLng(entity.la, entity.lo),entity.uid, R.mipmap.ic_circle_nearby_head).infoWindowEnable(false);
        }
        markerManager.addMap();
        markerManager.addLisenter(marker -> {
            FriendsCircleHomeFragment.start(getActivity(), Integer.valueOf(marker.getSnippet()),BaseCircleMessageFragment.TYPE_FOLLOW);
            return true;
        });
        RxUtils.delayed(200, aLong -> {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getCameraPosition().zoom - 0.5f));
        });

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
