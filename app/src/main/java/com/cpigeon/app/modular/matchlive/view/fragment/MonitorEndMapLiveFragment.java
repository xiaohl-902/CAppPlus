package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;
import com.cpigeon.app.modular.matchlive.view.fragment.BaseMapLiveFragment;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.PointsUtil;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.ImageView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/3/6.
 */

public class MonitorEndMapLiveFragment extends BaseMapLiveFragment {

    TextView end_point_name;
    TextView play_btn;
    double careSpeed = -1;
    boolean isMove = false;

    private SmoothMoveMarker smoothMarker;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_map_lookback_new_end_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        end_point_name = findViewById(R.id.end_point_name);
        play_btn = findViewById(R.id.play_btn);
        showLoading();
        mPresenter.getPosition(aBoolean -> {
            if(mPresenter.getLastPoint() != null){
                mPresenter.time = mPresenter.mapLiveEntity.jksc;
                addPolylineInPlayGround();
                setTime();
                bindLiveData();
                if(StringValid.isStringValid(mPresenter.mapLiveEntity.sfd)){
                    end_point_name.setText("司放地：" + mPresenter.mapLiveEntity.sfd);
                }else end_point_name.setText("司放地：未设置");
                addMarker();
                initCare();
                initCardPosition();
                setWeatherPoint();
                play_btn.setOnClickListener(v -> {
                    LogUtil.print("is move" + isMove);
                    if(isMove){
                        smoothMarker.stopMove();
                        play_btn.setText("开始回放");
                        isMove = false;
                    }else {
                        if(careSpeed == 0){
                            initCardPosition();
                        }
                        if(rl_details.getVisibility() == View.VISIBLE){
                            rl_details.startAnimation(closeAnimation);
                        }
                        smoothMarker.startSmoothMove();
                        play_btn.setText("停止回放");
                        isMove = true;
                    }
                });
                hideLoading();
            }else error("暂时没有数据");
        });


    }

    /*private void moveCare() {
        smoothMarker.startSmoothMove();
        play_btn.setText("停止回放");
        play_btn.setOnClickListener(null);
        play_btn.setOnClickListener(v -> {
            stopCare();
        });
    }

    private void stopCare() {
        smoothMarker.stopMove();
        play_btn.setText("开始回放");
        play_btn.setOnClickListener(null);
        play_btn.setOnClickListener(v -> {
            moveCare();
        });
    }*/

    private void initCardPosition() {
        LatLng drivePoint = mPresenter.latLngs.get(0);
        Pair<Integer, LatLng> pair = PointsUtil.calShortestDistancePoint(mPresenter.latLngs, drivePoint);
        mPresenter.latLngs.set(pair.first, drivePoint);
        List<LatLng> subList = mPresenter.latLngs.subList(pair.first, mPresenter.latLngs.size());

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        smoothMarker.setTotalDuration(40);
        smoothMarker.setRotate(-MapMarkerManager.getAngle(mPresenter.latLngs, true));
    }

    private void initCare() {
        smoothMarker = new SmoothMoveMarker(aMap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.car));
        smoothMarker.setMoveListener(v -> {
            careSpeed = v;
            if (v == 0) {
                isMove = false;
                play_btn.setText("开始回放");
                LogUtil.print("is move" + isMove);
                try {
                    smoothMarker.stopMove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*play_btn.setOnClickListener(null);
                play_btn.setOnClickListener(v1 -> {
                    moveCare();
                });*/
            } else {
                LatLng position = smoothMarker.getPosition();
                aMap.animateCamera(CameraUpdateFactory.changeLatLng(position));
            }
        });
    }

    private void addMarker() {
        //描绘起点
        mapMarkerManager.addCustomMarker(mPresenter.startLatLng.latitude, mPresenter.startLatLng.longitude
                , R.mipmap.ic_move_start);
        //描绘终点
        mapMarkerManager.addCustomMarker(mPresenter.getLastPoint().getWd(), mPresenter.getLastPoint().getJd()
                , R.mipmap.ic_move_end);
        mapMarkerManager.addMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(smoothMarker != null){
            smoothMarker.destroy();
        }
    }
}
