package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by Zhu TingYu on 2018/3/5.
 */

public class MonitoringMapLiveFragment extends BaseMapLiveFragment {


    TextView speed;

    TextView c_point;
    TextView c_weather;

    Disposable disposable;
    Disposable disposable2;

    boolean isLoadDataFrist = true;


    @Override
    protected boolean isCanDettach() {
        return false;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_map_lookback_new_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        speed = findViewById(R.id.speed);
        c_point = findViewById(R.id.c_point);
        c_weather = findViewById(R.id.c_weather);
        initData();

    }

    private void initData() {
        mPresenter.lid = mPresenter.getLastPoint() == null ? "" : String.valueOf(mPresenter.getLastPoint().getLid());
        if(isFirst){
            showLoading();
        }
        mPresenter.getPosition(isHaveData -> {
            if(isHaveData){
                c_point.setText("当前坐标：" +  CommonTool.GPSformatOfEveryMinute(mPresenter.getLastPoint().getJd()) + "E/" + CommonTool.GPSformatOfEveryMinute(mPresenter.getLastPoint().getWd())+"N");
                c_weather.setText("当前天气：" + mPresenter.getLastPoint().getTq().getMc() +"  "+mPresenter.getLastPoint().getTq().getWd() + "°  "
                        +mPresenter.getLastPoint().getTq().getFx()+"风");
                bindLiveData();
                speed.setText("车速 km/h：" + mPresenter.getLastPoint().getSd());
                mapMarkerManager.removeEndMarkers();
                addPolylineInPlayGround();
                addMarker();
                if(isFirst){
                    mPresenter.time = mPresenter.mapLiveEntity.jksc;
                    setTime();

                    disposable = RxUtils.rollPoling(1000,1000, aLong -> {
                        mPresenter.time++;
                        setTime();
                        if(isLoadDataFrist){
                            isLoadDataFrist = false;
                            disposable2 = RxUtils.rollPoling(5000, 5000, aLong1 -> {
                                initData();
                            });
                        }
                    });

                    setWeatherPoint();

                    isFirst = false;
                }
            }else {
                if(isFirst){
                    ToastUtil.showShortToast(getContext(), "暂时没有数据");
                }
            }
            hideLoading();

        });
    }

    private void moveLastPoint() {
        mapMarkerManager.removeEndMarkers();
    }

    private void addMarker() {

        mapMarkerManager.clean();
        //描绘起点
/*        mapMarkerManager.addCustomMarker(mPresenter.startLatLng.latitude,mPresenter.startLatLng.longitude
                ,R.mipmap.ic_move_start);*/
        //描绘终点
        mapMarkerManager.addCustomMarker(mPresenter.getLastPoint().getWd(),mPresenter.getLastPoint().getJd()
                ,R.drawable.ic_blue_point);
        mapMarkerManager.addMap().get(0).setAnchor(0.5f,0.5f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }

        if(disposable2 != null){
            disposable2.dispose();
        }
    }
}
