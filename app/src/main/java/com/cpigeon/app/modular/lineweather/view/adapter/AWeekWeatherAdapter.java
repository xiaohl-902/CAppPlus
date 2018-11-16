package com.cpigeon.app.modular.lineweather.view.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */

public class AWeekWeatherAdapter extends BaseQuickAdapter<LocalDayWeatherForecast, BaseViewHolder> {

    private Map<String, Integer> icMap1;

    public AWeekWeatherAdapter(List<LocalDayWeatherForecast> data) {
        super(R.layout.item_a_week_weather, data);

        initIcMap2();
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalDayWeatherForecast item) {

        if (helper.getPosition() == 0) {
            helper.setText(R.id.tv_time1, "今");
        } else {
            helper.setText(R.id.tv_time1, item.getDate().substring(item.getDate().length() - 2, item.getDate().length()));
        }


        helper.setText(R.id.tv_ewather1, item.getDayWeather());//白天天气
        helper.setText(R.id.tv_ewather2, item.getNightWeather());//晚上天气

        AppCompatImageView img_weather1 = helper.getView(R.id.img_weather1);
        AppCompatImageView img_weather2 = helper.getView(R.id.img_weather2);

        helper.setText(R.id.tv_temperature1, item.getDayTemp() + "℃");//白天温度
        helper.setText(R.id.tv_temperature2, item.getNightTemp() + "℃");//晚上温度

        helper.setText(R.id.tv_wind_direction1, item.getDayWindDirection());//白天方向
        helper.setText(R.id.tv_wind_direction2, item.getNightWindDirection());//晚上风向

        int integer1 = -1;
        try {
            integer1 = icMap1.get(item.getDayWeather());
        } catch (Exception e) {
            integer1 =-1;
        }

        if (integer1 == -1) {
            img_weather1.setImageResource(icMap1.get("未知"));
        } else {
            img_weather1.setImageResource(icMap1.get(item.getDayWeather()));
        }

        int integer2 = -1;
        try {
            integer2 = icMap1.get(item.getNightWeather());
        } catch (Exception e) {
            integer2 = -1;
        }


        if (integer2 == -1) {
            img_weather2.setImageResource(icMap1.get("未知"));
        } else {
            img_weather2.setImageResource(icMap1.get(item.getNightWeather()));
        }
        if (helper.getPosition() == mData.size() - 1) {
            helper.getView(R.id.tv_bo).setVisibility(View.VISIBLE);
        }
    }

    private void initIcMap2() {
        icMap1 = new HashMap<>();
        icMap1.put("阵雨", R.drawable.ic_weather_white_a_shower_b);
        icMap1.put("多云", R.drawable.ic_weather_white_cloudy_b);
        icMap1.put("大雨", R.drawable.ic_weather_white_heavy_rain_b);
        icMap1.put("中雨", R.drawable.ic_weather_white_moderate_rain_b);
        icMap1.put("小雨", R.drawable.ic_weather_white_light_rain_b);

        icMap1.put("小雪", R.drawable.ic_weather_light_snow_b);
        icMap1.put("中雪", R.drawable.ic_weather_light_snow_b);
        icMap1.put("大雪", R.drawable.ic_weather_light_snow_b);

        icMap1.put("雨夹雪", R.drawable.ic_weather_white_sleet_b);
        icMap1.put("霾", R.drawable.ic_weather_white_smog_b);
        icMap1.put("晴", R.drawable.ic_weather_white_sunny_b);
        icMap1.put("雷阵雨", R.drawable.ic_weather_white_thunder_shower_b);
        icMap1.put("阴", R.drawable.ic_weather_white_yin_b);

        icMap1.put("未知", R.drawable.ic_weather_unknown_b);
    }

}
