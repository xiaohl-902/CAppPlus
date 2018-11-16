package com.cpigeon.app.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.amap.api.services.weather.LocalWeatherLive;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;

/**
 * Created by chenshuai on 2017/11/9.
 */

public class WeatherInfoView extends ViewGroup {

    public WeatherInfoView(Context context) {
        super(context);
        initView(context);
    }


    public WeatherInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WeatherInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    private void initView(Context context) {
        //LayoutInflater.from(context).inflate(R.layout.item_weather_info_window_layout,null,true);
        inflate(context,R.layout.item_weather_info_window_layout,this).invalidate();
    }

    public void bindData(LocalWeatherLive weatherLive){
        BaseViewHolder holder = new BaseViewHolder(this);
        holder.setText(R.id.temp, getContext().getString(R.string.text_temperature,weatherLive.getTemperature()));
        holder.setText(R.id.address, weatherLive.getProvince() + weatherLive.getCity());
        holder.setText(R.id.text1, weatherLive.getWeather());
        holder.setText(R.id.text2, getContext().getString(R.string.text_wind_direction,weatherLive.getWindDirection()));
    }

}
