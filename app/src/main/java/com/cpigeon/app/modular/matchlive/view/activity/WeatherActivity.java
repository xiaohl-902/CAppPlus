package com.cpigeon.app.modular.matchlive.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.weather.LocalWeatherLive;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.MapMarkerManager;
import com.cpigeon.app.modular.matchlive.view.adapter.AfterWeatherListAdapter;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.WeatherManager;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.utils.http.GsonUtil;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.ShareDialogFragment;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/25.
 */

public class WeatherActivity extends BaseActivity {
    Polyline polyline;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.recyclerview_weather)
    RecyclerView recyclerviewWeather;
    AMap aMap;

    List<LatLng> afterPoints;
    AfterWeatherListAdapter adapter;
    Toolbar toolbar;
    /*RegeocodeAddress[] addressList;
    LocalWeatherLive[] weatherList;*/

    ArrayList<RegeocodeAddress> addressList;
    ArrayList<LocalWeatherLive> weatherList;

    WeatherManager manager;

    Map<String, Integer> icMap;

    int i = 0;

    MapMarkerManager markerManager;

    ImageView mapImage;
    RelativeLayout rootView;

    boolean isShowEnd = true;


    public static final int MARKER_NORMAL = -1;
    public static final int MARKER_START = 0;
    public static final int MARKER_END = 1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        Intent intent = getIntent();


        TextView tvDisplacement = findViewById(R.id.displacement);
        MarqueeTextView title = findViewById(R.id.toolbar_title);
        mapImage = findViewById(R.id.map_image);



        rootView = findViewById(R.id.rootView);


        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            markerManager = new MapMarkerManager(aMap, mContext);
        }
        adapter = new AfterWeatherListAdapter();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.getMenu().add("分享").setOnMenuItemClickListener(item -> {
            showLoading();
            getImageByMap();
            /*View dView = getWindow().getDecorView();
            dView.setDrawingCacheEnabled(true);
            dView.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());

            ShareDialogFragment share = new ShareDialogFragment();
            share.setDescription(getIntent().getStringExtra(IntentBuilder.KEY_TITLE) + "赛线天气");
            share.setShareType(ShareDialogFragment.TYPE_IMAGE_FILE);
            share.setShareContent(bitmap);
            share.setOnShareCallBackListener(() -> {

            });
            share.show(getActivity().getFragmentManager(), "share");*/
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        title.setText(getIntent().getStringExtra(IntentBuilder.KEY_TITLE) + "赛线天气");
        toolbar.setNavigationOnClickListener(v -> finish());

        manager = new WeatherManager(this);

        initIcMap();

        afterPoints = new ArrayList<>();
        isShowEnd = intent.getBooleanExtra("isShowEnd", true);
        double startLa = intent.getDoubleExtra("v1", 0);
        double startLo = intent.getDoubleExtra("v2", 0);
        double stopLa = intent.getDoubleExtra("v3", 0);
        double stopLo = intent.getDoubleExtra("v4", 0);


        if (startLa == 0 || startLo == 0 || stopLa == 0 || stopLo == 0) {
            DialogUtils.createErrorDialog(getActivity(), "加载错误");
        } else {
            double displacement = intent.getDoubleExtra(IntentBuilder.KEY_DATA, 0);

//            if (displacement == 0) {
//                double distanceD = CommonTool.calculateLineDistance(new LatLng(startLa, startLo), new LatLng(stopLa, stopLo)) / 1000;
//                tvDisplacement.setText("参考空距：" + CommonUitls.doubleformat(distanceD, 3) + "km");
//            } else {
//            }
            /**
             *  加点公式，可参考
             */
            tvDisplacement.setText("参考空距：" + CommonUitls.doubleformat(displacement, 3) + "km");

            int distance = (int) displacement;
            int point = (distance / 100) + 1;

            double x = (stopLo - startLo) * (1f / point);
            double y = (stopLa - startLa) * (1f / point);
            Logger.e("斜度:" + x);

            afterPoints.add(new LatLng(startLa, startLo));
            for (int i = 1; i < point; i++) {
                double x1 = startLa + (y * i);//第一个点La
                double x2 = startLo + (x * i);//第一个点Long
                LatLng latLng = new LatLng(x1, x2);
                afterPoints.add(latLng);
            }
            afterPoints.add(new LatLng(stopLa, stopLo));


            initView();
            //addressList = new RegeocodeAddress[afterPoints.size()];
            //weatherList = new LocalWeatherLive[afterPoints.size()];

            addressList = Lists.newArrayList();
            weatherList = Lists.newArrayList();

            showLoading();

            searchCityByPoint();
        }


    }

    private String TAG = "printLog";

    public void getImageByMap() {
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int status) {

                hideLoading();

                if (null == bitmap) {
                    Log.d(TAG, "onMapScreenShot: bitmap为空");
                    return;
                }


                mapImage.setVisibility(View.VISIBLE);
                mapImage.setImageBitmap(bitmap);

//                View dView = getWindow().getDecorView();
//                dView.setDrawingCacheEnabled(true);
//                dView.buildDrawingCache();
//                Bitmap bitmap1 = Bitmap.createBitmap(dView.getDrawingCache());

                bindData(RxUtils.delayed_1(200), o -> {
                    Bitmap bitmap1 = BitmapUtils.getViewBitmap(rootView);
                    ShareDialogFragment share = new ShareDialogFragment();
                    share.setShareContent(bitmap1);
                    mapImage.setVisibility(View.GONE);
                    share.setDescription(getIntent().getStringExtra(IntentBuilder.KEY_TITLE) + "赛线天气");
                    share.setShareType(ShareDialogFragment.TYPE_IMAGE_FILE);
//                share.setShareContent(bitmap);
                    share.setOnShareCallBackListener(new ShareDialogFragment.OnShareCallBackListener() {
                        @Override
                        public void onSuccess() {
                            mapImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFail() {
                            mapImage.setVisibility(View.GONE);
                        }
                    });
                    share.show(getActivity().getFragmentManager(), "share");
                });

//                mapImage.setVisibility(View.GONE);


            }
        });
    }

    private void initIcMap() {
        icMap = new HashMap<>();
        icMap.put("雷阵雨并伴有冰雹", R.drawable.ic_weather_white_hail);
        icMap.put("大雪", R.drawable.ic_weather_white_heavy_snow);
        icMap.put("大雨", R.drawable.ic_weather_white_heavy_rain);
        icMap.put("多云", R.drawable.ic_weather_white_cloudy);
        icMap.put("雷阵雨", R.drawable.ic_weather_white_thunder_shower);
        icMap.put("霾", R.drawable.ic_weather_white_smog);
        icMap.put("晴", R.drawable.ic_weather_white_sunny);
        icMap.put("雾", R.drawable.ic_weather_white_fog);
        icMap.put("小雪", R.drawable.ic_weather_white_light_snow);
        icMap.put("小雨", R.drawable.ic_weather_white_light_rain);
        icMap.put("阴", R.drawable.ic_weather_white_yin);
        icMap.put("雨夹雪", R.drawable.ic_weather_white_sleet);
        icMap.put("阵雪", R.drawable.ic_weather_white_heavy_snow);
        icMap.put("阵雨", R.drawable.ic_weather_white_heavy_rain);
        icMap.put("中雨", R.drawable.ic_weather_white_moderate_rain);
    }


    private void searchCityByPoint() {

        composite.add(manager.searchCityByLatLng(afterPoints.get(i), r -> {
            composite.add(manager.requestWeatherByCityName(r.data.getCity(), response -> {
                if (response.isOk()) {
                    weatherList.add(response.data);
                    if (weatherList.size() == afterPoints.size()) {
                        bindListData();
                        initMap();
                        hideLoading();
                        LogUtil.print("debug" + "hideLoading");
                    } else {
                        searchCityByPoint();
                    }
                } else {
                    hideLoading();
                    DialogUtils.createErrorDialog(getActivity(), "加载异常");
                }
            }));
            i++;
        }));

    }


    private void initView() {
        recyclerviewWeather = findViewById(R.id.recyclerview_weather);
        recyclerviewWeather.setLayoutManager(new LinearLayoutManager(this));
        addItemDecorationLine(recyclerviewWeather);
        recyclerviewWeather.setAdapter(adapter);

    }

    private void bindListData() {
        LogUtil.print("debug" + "bindListData");
        if(weatherList.size() == 2){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenTool.dip2px(80));
            params.setMargins(ScreenTool.dip2px(15), ScreenTool.dip2px(15),ScreenTool.dip2px(15),ScreenTool.dip2px(15));
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            recyclerviewWeather.setLayoutParams(params);
        }
        adapter.setNewData(weatherList);
    }


    private void initMap() {
        LogUtil.print("debug" + "initMap");
        /*aMap.addPolyline(new PolylineOptions()
                .width(10).color(R.color.light_red_1).addAll(afterPoints));*/
        aMap.addPolyline(new PolylineOptions().
                addAll(afterPoints).width(10).color(Color.argb(255, 15, 166, 236)));


        markerManager.addCustomMarker(afterPoints.get(0), null, BitmapUtils.getViewBitmap(getInfoWindow(GsonUtil.toJson(weatherList.get(0)), MARKER_START)));

        for (int i = 1, len = afterPoints.size() - 1; i < len; i++) {
            markerManager.addCustomMarker(afterPoints.get(i), null, BitmapUtils.getViewBitmap(getInfoWindow(GsonUtil.toJson(weatherList.get(i)), MARKER_NORMAL)));
        }

        markerManager.addCustomMarker(afterPoints.get(afterPoints.size() - 1), null,
                BitmapUtils.getViewBitmap(getInfoWindow(GsonUtil.toJson(weatherList.get(afterPoints.size() - 1)), MARKER_END)));

        List<Marker> markerList = markerManager.addMap();


        if (isShowEnd) {
            markerList.get(markerList.size() - 1).showInfoWindow();
        } else {
            markerList.get(0).showInfoWindow();
        }

        RxUtils.delayed(200, aLong -> {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getCameraPosition().zoom - 1));
        });
    }


    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
    }

    View infoWindow = null;
    TextView temp;
    TextView address;
    TextView text1;
    TextView text2;
    ImageView icon;
    ImageView marker;

    public View getInfoWindow(String info, int type) {
        if (infoWindow == null) {
            infoWindow = findViewById(R.id.info);
            temp = infoWindow.findViewById(R.id.temp);
            address = infoWindow.findViewById(R.id.address);
            text1 = infoWindow.findViewById(R.id.text1);
            text2 = infoWindow.findViewById(R.id.text2);
            icon = infoWindow.findViewById(R.id.icon);
            marker = infoWindow.findViewById(R.id.marker);
        }
        render(info, type);
        return infoWindow;
    }

    private void render(String info, int type) {
        LocalWeatherLive weatherLive = GsonUtil.fromJson(info, new TypeToken<LocalWeatherLive>() {
        }.getType());

        temp.setText(mContext.getString(R.string.text_temperature, weatherLive.getTemperature()));
        address.setText(weatherLive.getProvince() + weatherLive.getCity());
        text1.setText(weatherLive.getWeather());
        text2.setText(mContext.getString(R.string.text_wind_direction, weatherLive.getWindDirection()));
        int integer = icMap.get(weatherLive.getWeather());

        if (type == MARKER_START) {
            marker.setImageResource(R.mipmap.ic_move_start);
        } else if (type == MARKER_END) {
            marker.setImageResource(R.mipmap.ic_move_end);
        } else {
            marker.setImageResource(R.mipmap.ic_map_location);
        }

        if (integer == 0) {
            icon.setImageResource(icMap.get("阴"));
        } else {
            icon.setImageResource(icMap.get(weatherLive.getWeather()));
        }

        /*holder.setText(R.id.temp, mContext.getString(R.string.text_temperature, weatherLive.getTemperature()));
        holder.setText(R.id.address, weatherLive.getProvince() + weatherLive.getCity());
        holder.setText(R.id.text1, weatherLive.getWeather());
        holder.setText(R.id.text2, mContext.getString(R.string.text_wind_direction, weatherLive.getWindDirection()));
        if (integer == 0) {
            holder.setImageResource(R.id.icon, icMap.get("阴"));
        } else {
            holder.setImageResource(R.id.icon, icMap.get(weatherLive.getWeather()));
        }*/
    }
}
