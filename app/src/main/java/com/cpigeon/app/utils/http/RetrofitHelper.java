package com.cpigeon.app.utils.http;



import com.cpigeon.app.utils.CPigeonApiUrl;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的帮助类
 * Created by Administrator on 2017/5/25.
 */

public class RetrofitHelper {

    private static OkHttpClient mOkHttpClient;

    /**
     * 静态类初始化
     */
    static {
        initOkHttpClient();
    }

    /**
     * 获取Api
     *
     * @return
     */

    public static ApiService getApi() {

        return createApi(ApiService.class, CPigeonApiUrl.getInstance().getServer()
                + "/CPAPI/V1/");
    }

    /**
     * 初始化Okhttp
     */
    private static void initOkHttpClient() {

        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    /*Cache cache = new Cache(new File(MyApplication.getInstance()
                            .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);*/

                    mOkHttpClient = new OkHttpClient.Builder()
//                            .cache(cache)//开启，数据缓存，不能及时获取新的数据
                            /*.addInterceptor(interceptor)
                            .addNetworkInterceptor(new CacheInterceptor())
                            .addNetworkInterceptor(new StethoInterceptor())*/
                            .retryOnConnectionFailure(true)
                            .connectTimeout(5, TimeUnit.SECONDS)//连接超时
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }


    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }


    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    /*private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // 有网络时 设置缓存超时时间10秒钟
            int maxAge = 10;
            // 无网络时，设置超时为1个小时
            int maxStale = 60 * 60;
            Request request = chain.request();
            if (NetStateUtils.isNetworkConnected(MyApp.getInstance())) {
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
            Response response = chain.proceed(request);
            if (NetStateUtils.isNetworkConnected(MyApp.getInstance())) {
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
    }*/
}
