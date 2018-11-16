package com.cpigeon.app.utils.http;


import android.support.annotation.StringRes;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.orhanobut.logger.Logger;

import org.xutils.HttpManager;
import org.xutils.common.util.KeyValue;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by chenshuai on 2017/11/7.
 */

public class HttpUtil<T> {

    public static final int TYPE_GET = 0;
    public static final int TYPE_POST = 1;

    static RequestParams requestParams;
    static HttpManager manager;
    public int type = TYPE_POST;
    private Type toJsonType;

    private String headUrl;

    String baseUrl;

    String url;

    String cacheKey;

    boolean isHaveCache = false;


    public static <T> HttpUtil<T> builder() {
        HttpUtil<T> httpUtil = new HttpUtil<>();
        requestParams = new RequestParams();
        requestParams.setConnectTimeout(35000);
        requestParams.setMaxRetryCount(0);
        manager = x.http();
        return httpUtil;
    }

    /**
     * post 添加url 里面的参数
     * get  添加body里面的参数
     *
     * @param name
     * @param value
     * @return
     */
    public HttpUtil<T> addParameter(String name, Object value) {
        requestParams.addParameter(name, value);
        return this;
    }

    public HttpUtil<T> addBody(String name, String value) {
        requestParams.addBodyParameter(name, value);
        return this;
    }


    public HttpUtil<T> addList(Map<String, String> body) {

        Iterator<Map.Entry<String, String>> entries = body.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            requestParams.addBodyParameter(entry.getKey(), entry.getValue());

        }
        return this;
    }

    public HttpUtil<T> addFileBody(String name, String path) {

        if (StringValid.isStringValid(path)) {
            requestParams.addBodyParameter(name, new File(path));
            requestParams.setMultipart(true);
        }

        return this;
    }

    /**
     * 添加url 里面的参数
     *
     * @param name
     * @param value
     * @return
     */
    public HttpUtil<T> addQueryString(String name, String value) {
        requestParams.addQueryStringParameter(name, value);
        return this;
    }


    public HttpUtil<T> url(@StringRes int url) {
        this.url = MyApp.getInstance().getBaseContext().getString(url);
        String stringUrl;
        stringUrl = getBaseUrl()
                + headUrl
                + this.url;
        requestParams.setUri(stringUrl);

        return this;
    }

    public String getBaseUrl() {
        return StringValid.isStringValid(baseUrl) ? baseUrl : CPigeonApiUrl.getInstance().getServer();
    }


    public HttpUtil<T> addHeader(String name, String value) {
        requestParams.addHeader(name, value);
        return this;
    }

    /**
     * 设置可以缓存
     * 放在调用request之前
     *
     * @return
     */

    public HttpUtil<T> setCache() {
        this.isHaveCache = true;

        requestParams.setCacheMaxAge(0);

        cacheKey = getCacheKey(this.url, requestParams);

        return this;
    }

    public HttpUtil<T> setUserId(String name, int value) {
        if (value != 0) {
            requestParams.addQueryStringParameter(name, String.valueOf(value));
        }
        return this;
    }

    /**
     * 设置请求方式
     *
     * @param type
     * @return
     */
    public HttpUtil<T> setType(int type) {
        if (type == TYPE_POST) {
            requestParams.setMethod(HttpMethod.POST);
        } else {
            requestParams.setMethod(HttpMethod.GET);
        }
        this.type = type;
        return this;
    }

    public Observable<T> request() {
        printf();

        CallAPI.addApiSign(requestParams);
        LogUtil.print(requestParams.getUri());
        LogUtil.print(CommonTool.getUserToken(MyApp.getInstance().getBaseContext()));
        Observable<T> observable = RxNet.newRequest(this)
                .map(s -> {
                    try {
                        return GsonUtil.fromJson(s, toJsonType);
                    } catch (Exception e) {
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


    public HttpUtil<T> setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
        return this;
    }

    public HttpUtil<T> setHeadUrl(@StringRes int headUrl) {
        this.headUrl = MyApp.getInstance().getBaseContext().getString(headUrl);
        return this;
    }

    public HttpUtil<T> setToJsonType(Type toJsonType) {
        this.toJsonType = toJsonType;
        return this;
    }

    public HttpUtil<T> setBaseUrl(@StringRes int resId) {
        this.baseUrl = MyApp.getInstance().getBaseContext().getString(resId);
        return this;
    }

    private void printf() {
        List<KeyValue> keyValues = requestParams.getQueryStringParams();
        List<KeyValue> body = requestParams.getBodyParams();
        StringBuffer params = new StringBuffer();
        params.append("{" + "\n");
        params.append("{" + "\n");
        for (KeyValue vale : keyValues) {
            params.append("  url    " + vale.key + ": " + vale.value + "\n");
        }
        for (KeyValue vale : body) {
            params.append("  body   " + vale.key + ": " + vale.value + "\n");
        }
        params.append("}");
        params.toString();
        Log.e("params", params.toString());
    }

    static String getCacheKey(String apiName, RequestParams requestParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(apiName);

        for (KeyValue para : requestParams.getBodyParams()) {
            builder.append(String.format("&post_%s=%s", para.key, para.value.toString()));
        }

        for (KeyValue para : requestParams.getQueryStringParams()) {
            if (!para.key.equals("timestamp") && !para.key.equals("sign") && !para.key.equals("bt")) {
                builder.append(String.format("&get_%s=%s", para.key, para.value.toString()));
            }
        }

        Logger.d(builder.toString());
        return builder.toString();
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public boolean isHaveCache() {
        return isHaveCache;
    }
}
