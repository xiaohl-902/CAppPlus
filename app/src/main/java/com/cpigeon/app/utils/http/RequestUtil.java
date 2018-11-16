package com.cpigeon.app.utils.http;


import android.support.annotation.StringRes;

import com.alibaba.fastjson.JSON;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zhu TingYu on 2017/12/15.
 */

public class RequestUtil<T> {

    private static final int DEFAULT_TIMEOUT = 5;

    private Map<String, String> bodyParameter = new HashMap<>();
    private Map<String, String> headParameter = new HashMap<>();

    private Type toJsonType;

    private String url;
    private String baseUrl;
    private String headUrl;

    private Retrofit retrofit;

    private OkHttpClient okHttpClient;
    private RequestInterface requestInterface;

    private String sign;

    private int uid;

    static OkHttpClient.Builder builder;

    boolean isCache = false;

    public static <T> RequestUtil<T> builder() {
        RequestUtil<T> requestUtil = new RequestUtil<>();
        builder = new OkHttpClient.Builder();
        return requestUtil;
    }

    public RequestUtil<T> addBody(String key, String value) {
        bodyParameter.put(key, value);
        return this;
    }

    public RequestUtil<T> addHead(String key, String value) {
        headParameter.put(key, value);
        return this;
    }

    public RequestUtil<T> headUrl(String headUrl) {
        this.headUrl = headUrl;
        return this;
    }

    public RequestUtil<T> url(String url) {
        this.url = url;
        return this;
    }
    public RequestUtil<T> url(@StringRes int url) {
        this.url = MyApp.getInstance().getBaseContext().getString(url);
        return this;
    }


    public RequestUtil<T> setToJsonType(Type toJsonType) {
        this.toJsonType = toJsonType;
        return this;
    }

    public RequestUtil<T> setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;

    }

    public RequestUtil<T> setUserId(int userId) {
        this.uid = userId;
        return this;

    }

    public RequestUtil<T> setCacheFile(int userId) {
        File cacheFile = new File(MyApp.getInstance().getBaseContext().getCacheDir(), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        builder.cache(cache);
        isCache = true;
        return this;

    }

    public Observable<T> request() {

        LogUtil.print(bodyParameter);
        LogUtil.print(getRequestUrl() + url);
        builder.addInterceptor(new BaseInterceptor(headParameter))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (isCache) {
            builder.addNetworkInterceptor(new CacheInterceptor());
        }

        okHttpClient = builder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(getRequestUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        requestInterface = retrofit.create(RequestInterface.class);

        Observable<T> observable = RxRequest.getRxHttp(this)
                .map(s -> {
                    try {
                        return GsonUtil.fromJson(s, toJsonType);
                    }catch (Exception e){
                        return JSON.parseObject(s, toJsonType);
                    }
                });

        observable = observable.map(e -> {
            if (e instanceof ApiResponse) {
                ApiResponse responseJson = (ApiResponse) e;
                LogUtil.print(responseJson.toJsonString());
            }
            return e;
        });
        return observable;

    }


    RequestInterface getRequestInterface() {
        return requestInterface;
    }

    String getRequestUrl() {
        return baseUrl + headUrl;
    }

    String getSign() {
        return CallAPI.getApiSign(bodyParameter);
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getBodyParameter() {
        return bodyParameter;
    }

    public int getUid() {
        return uid;
    }
}

class CacheInterceptor implements Interceptor {

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        // 有网络时 设置缓存超时时间10秒钟
        int maxAge = 10;
        // 无网络时，设置超时为1个小时
        int maxStale = 60 * 60;
        Request request = chain.request();
        if (CommonTool.isNetworkConnected(MyApp.getInstance().getBaseContext())) {
            //有网络时只从网络获取
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            //无网络时只从缓存中读取
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        okhttp3.Response response = chain.proceed(request);
        if (CommonTool.isNetworkConnected(MyApp.getInstance().getBaseContext())) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
