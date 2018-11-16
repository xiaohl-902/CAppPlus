package com.cpigeon.app.modular.matchlive.view.adapter;

import com.amap.api.services.weather.LocalWeatherLive;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenshuai on 2017/11/8.
 */

public class AfterWeatherListAdapter extends BaseQuickAdapter<LocalWeatherLive, BaseViewHolder> {

    private Map<String, Integer> icMap;

    public AfterWeatherListAdapter() {
        super(R.layout.item_after_weather_layout, Lists.newArrayList());
        initMap();
    }

    private void initMap() {
        icMap = new HashMap<>();
        /*icMap.put("雷阵雨并伴有冰雹",R.drawable.ic_weahter_hail);
        icMap.put("大雪",R.drawable.ic_weather_heavy_snow);
        icMap.put("大雨",R.drawable.ic_weather_heavy_rain);
        icMap.put("多云",R.drawable.ic_weather_cloudy);
        icMap.put("雷阵雨",R.drawable.ic_weather_thunder_shower);
        icMap.put("霾",R.drawable.ic_weather_somg);
        icMap.put("晴",R.drawable.ic_weather_sunny);
        icMap.put("雾",R.drawable.ic_weather_fog);
        icMap.put("小雪",R.drawable.ic_weather_light_snow);
        icMap.put("小雨",R.drawable.ic_weather_light_rain);
        icMap.put("阴",R.drawable.ic_weather_yin);
        icMap.put("雨夹雪",R.drawable.ic_weather_sleet);
        icMap.put("阵雪",R.drawable.ic_weather_shower);
        icMap.put("阵雨",R.drawable.ic_weather_heavy_rain);
        icMap.put("中雨",R.drawable.ic_weather_moderrate_rain);*/
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

    @Override
    protected void convert(BaseViewHolder holder, LocalWeatherLive weatherLive) {
        holder.setText(R.id.address, weatherLive.getProvince() + weatherLive.getCity());
        holder.setText(R.id.text1, weatherLive.getWeather());
        holder.setText(R.id.text2, mContext.getString(R.string.text_temperature, weatherLive.getTemperature()));
        holder.setText(R.id.text3, mContext.getString(R.string.text_wind_direction, weatherLive.getWindDirection()));
        int integer = icMap.get(weatherLive.getWeather());
        if(integer == 0){
            holder.setImageResource(R.id.icon,icMap.get("阴"));
        }else {
            holder.setImageResource(R.id.icon,icMap.get(weatherLive.getWeather()));
        }
        //holder.setText(R.id.text4, "风力5级");
    }
}
