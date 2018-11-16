package com.cpigeon.app.circle.ui;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.R;
import com.cpigeon.app.base.AmapManager;
import com.cpigeon.app.base.BaseMapFragment;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2018/5/28.
 */

public class CircleMessageLocationFragment extends BaseMapFragment {

    public static void start(Activity activity, LatLng latLng){
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, latLng)
                .startParentActivity(activity, CircleMessageLocationFragment.class);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        LatLng location = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        setTitle("他在这里");
        //mapView = findViewById(R.id.map);
     /*   mapView.onCreate(state);0
        if(aMap == null){
            aMap = mapView.getMap();
        }

        markerManager = new MapMarkerManager(aMap, getActivity());*/
        markerManager.addCustomMarker(location,null,R.mipmap.ic_circle_message_location);
        markerManager.addMap();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));

    }
}
