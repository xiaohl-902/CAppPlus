package com.cpigeon.app.utils;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.app.utils.databean.ApiResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenshuai on 2017/11/9.
 */

public class WeatherManager {

    Context context;
    GeocodeSearch geocoderSearch;
    WeatherSearch mweathersearch;

    public WeatherManager(Context context) {
        this.context = context;
        geocoderSearch = new GeocodeSearch(context);
        mweathersearch = new WeatherSearch(context);

    }

    public Disposable searchCityByLatLng(LatLng latLng, Consumer<ApiResponse<RegeocodeAddress>> consumer) {

        return Observable.<ApiResponse<RegeocodeAddress>>create(observableEmitter -> {
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    ApiResponse<RegeocodeAddress> response = new ApiResponse<>();
                    response.errorCode = 0;
                    response.data = regeocodeResult.getRegeocodeAddress();
                    observableEmitter.onNext(response);
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
            geocoderSearch.getFromLocationAsyn(query);
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    /*public Observable<ApiResponse<RegeocodeAddress>> setSearchCityCallBack() {

        return Observable.<ApiResponse<RegeocodeAddress>>create(observableEmitter -> {
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    ApiResponse<RegeocodeAddress> response = new ApiResponse<>();
                    response.errorCode = 0;
                    response.data = regeocodeResult.getRegeocodeAddress();
                    observableEmitter.onNext(response);
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        });

    }*/

    public Disposable requestWeatherByCityName(String cityName, Consumer<ApiResponse<LocalWeatherLive>> consumer) {

        return Observable.<ApiResponse<LocalWeatherLive>>create(observableEmitter -> {
            WeatherSearchQuery mquery = new WeatherSearchQuery(cityName, WeatherSearchQuery.WEATHER_TYPE_LIVE);
            mweathersearch.setQuery(mquery);
            mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
                @Override
                public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                    ApiResponse<LocalWeatherLive> response = new ApiResponse<>();
                    if (i == 1000) {
                        if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                            response.data = localWeatherLiveResult.getLiveResult();
                            response.errorCode = 0;
                        } else {
                            response.errorCode = -1;
                            response.msg = "没有查到天气数据";
                        }
                    } else {
                        response.errorCode = -1;
                        response.msg = String.valueOf(i);
                    }
                    observableEmitter.onNext(response);
                }

                @Override
                public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

                }
            });
            mweathersearch.searchWeatherAsyn();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    /*public Observable<ApiResponse<LocalWeatherLive>> setRequstWeatherCallBack() {

        return Observable.<ApiResponse<LocalWeatherLive>>create(observableEmitter -> {
            mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
                @Override
                public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                    ApiResponse<LocalWeatherLive> response = new ApiResponse<>();
                    if (i == 1000) {
                        if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                            response.data = localWeatherLiveResult.getLiveResult();
                            response.errorCode = 0;
                        } else {
                            response.errorCode = -1;
                            response.msg = "没有查到天气数据";
                        }
                    } else {
                        response.errorCode = -1;
                        response.msg = String.valueOf(i);
                    }
                    observableEmitter.onNext(response);
                }

                @Override
                public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

                }
            });

        });

    }*/

}
