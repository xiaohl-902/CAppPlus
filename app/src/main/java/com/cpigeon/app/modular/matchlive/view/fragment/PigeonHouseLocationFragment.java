package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseMapFragment;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.CommonUitls;

/**
 * Created by Zhu TingYu on 2018/5/30.
 */

public class PigeonHouseLocationFragment extends BaseMapFragment {

    public static void start(Activity activity, String lo, String la, String name){

        double lo1 = Double.valueOf(lo);
        double la1 = Double.valueOf(la);


        if(lo1 == 0 || la1 == 0){
            DialogUtils.createErrorDialog(activity, "坐标异常！");
            return;
        }

        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, la)
                .putExtra(IntentBuilder.KEY_DATA_2, lo)
                .putExtra(IntentBuilder.KEY_TITLE, name)
                .startParentActivity(activity, PigeonHouseLocationFragment.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pigeon_house_location_layout;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        LatLng location = new LatLng(CommonTool.Aj2GPSLocation(Double.valueOf(getActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA)))
                ,CommonTool.Aj2GPSLocation(Double.valueOf(getActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA_2))));

        location = CommonUitls.GPS2AmapLocation(getActivity(),location);

        String name = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TITLE);

        setTitle(name + "的鸽舍位置");

        markerManager.addCustomMarker(location,null, R.drawable.ic_line_ewather_sfd);
        markerManager.addMap();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));

    }
}
