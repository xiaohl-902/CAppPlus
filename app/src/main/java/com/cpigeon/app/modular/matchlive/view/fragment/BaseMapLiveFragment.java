package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;
import com.cpigeon.app.modular.matchlive.presenter.MapLivePre;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.WeatherManager;
import com.cpigeon.app.utils.http.LogUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/3/6.
 */

public abstract class BaseMapLiveFragment extends BaseMVPFragment<MapLivePre> {

    TextView state;
    TextView time;
    TextView end_point;
    TextView end_weather;
    TextView displacement;
    TextView distance;
    AppCompatImageView messageBtn;
    AppCompatImageView closeBtn;

    Animation openAnimation;
    Animation closeAnimation;

    RelativeLayout rl_details;


    TextureMapView textureMapView;
    AMap aMap;
    MapMarkerManager mapMarkerManager;
    WeatherManager weatherManager;

    PolylineOptions polylineOptions;

    protected boolean isFirst = true;


    @Override
    protected MapLivePre initPresenter() {
        return new MapLivePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        weatherManager = new WeatherManager(getContext());
        textureMapView = findViewById(R.id.mapView);
        textureMapView.onCreate(state);
        if (aMap == null) {
            aMap = textureMapView.getMap();
            aMap.getUiSettings().setRotateGesturesEnabled(false);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setTrafficEnabled(true);
            mapMarkerManager = new MapMarkerManager(aMap, getContext());
            polylineOptions = new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
                    .width(18);
        }

        this.state = findViewById(R.id.state);
        time = findViewById(R.id.time);
        end_point = findViewById(R.id.end_point);
        end_weather = findViewById(R.id.end_weather);
        displacement = findViewById(R.id.displacement);
        distance = findViewById(R.id.distance);

        messageBtn = findViewById(R.id.img_message_btn);
        closeBtn = findViewById(R.id.img_btn);
        rl_details = findViewById(R.id.rl_details);

        bindUi(RxUtils.click(closeBtn), o -> {
            rl_details.startAnimation(closeAnimation);
        });

        bindUi(RxUtils.click(messageBtn), o -> {
            rl_details.startAnimation(openAnimation);
        });

        initAnimation();
    }

    protected void setWeatherPoint() {
        ((MapLiveActivity) getActivity()).setPointAndDisplacement(Lists.newArrayList(mPresenter.positions.get(0).getWd(), mPresenter.positions.get(0).getJd(), mPresenter.geCheJianKongRace.getLatitude(),
                mPresenter.geCheJianKongRace.getLongitude()), Double.parseDouble(mPresenter.mapLiveEntity.kongju));
    }


    protected void setTime() {
        time.setText("已监控：" + DateTool.formatTime(mPresenter.time * 1000));
    }

    protected void bindLiveData() {

        if (isFirst) {
            if (mPresenter.mapLiveEntity.sfdjd != 0 && mPresenter.mapLiveEntity.sfdwd != 0) {
                end_point.setText("司放地坐标：" + CommonTool.strToDMs(String.valueOf(mPresenter.mapLiveEntity.sfdjd)) + "E/" + CommonTool.strToDMs(String.valueOf(mPresenter.mapLiveEntity.sfdwd)) + "N");
            } else {
                end_point.setText("司放地坐标：未设置");
            }

            if (mPresenter.mapLiveEntity.sfdjd != 0 && mPresenter.mapLiveEntity.sfdwd != 0
                    && mPresenter.mapLiveEntity.tianqi.equals("未获取到天气")) {
                end_weather.setText("司放地天气：" + "获取中");
                weatherManager.searchCityByLatLng(new LatLng(mPresenter.mapLiveEntity.sfdwd, mPresenter.mapLiveEntity.sfdjd), r -> {
                    weatherManager.requestWeatherByCityName(r.data.getCity(), response -> {
                        if (response.isOk()) {
                            end_weather.setText("司放地天气：" + response.data.getWeather() + "  " + response.data.getTemperature() + "°  "
                                    + response.data.getWindDirection() + "风");
                        } else {
                            end_weather.setText("司放地天气：" + "未获取到天气");
                        }
                    });
                });
            }
            displacement.setText("空距/km：" + mPresenter.mapLiveEntity.kongju);
        }

        if (mPresenter.getLastPoint().getLc() == 0.0) {
            distance.setText("总里程/km：0.0");
        } else {
            distance.setText("总里程/km：" + DateTool.doubleformat(mPresenter.getLastPoint().getLc() / 1000, 3));
        }
    }

    protected void moveMapByPoint(LatLng latLng) {
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 添加轨迹
     */
    protected void addPolylineInPlayGround() {
        polylineOptions.addAll(CommonTool.DouglasPeucker(mPresenter.latLngs,1));
        aMap.addPolyline(polylineOptions);
        LogUtil.print("size :" + mPresenter.latLngs.size());
    }

//    protected void addPolylineInPlayGroundEnd() {
//
//        GPSCorrect gpsCorrect = new GPSCorrect();
//        List<GYTRaceLocation> position = mPresenter.positions;
//        for (int i = 0, len = position.size(); i < len ; i++) {
//            gpsCorrect.filterPos(position.get(i));
//        }
//
//
//
//        polylineOptions.addAll(gpsCorrect.getPoint());
//        aMap.addPolyline(polylineOptions);
//        LogUtil.print("size :" + mPresenter.latLngs.size());
//    }


    private void initAnimation() {
        closeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_in);
        openAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_out);

        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_details.setVisibility(View.GONE);
                messageBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rl_details.setVisibility(View.VISIBLE);
                messageBtn.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
