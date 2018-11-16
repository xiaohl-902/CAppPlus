package com.cpigeon.app.utils.http;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Zhu TingYu on 2017/12/15.
 */

public interface RequestInterface {

    @POST
    Call<ResponseBody> request(
            @Url String url,
            @Query("u") String uid,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

    @Headers("Cache-Control: public, max-age=3600")
    @POST
    Call<ResponseBody> requestHaveCache (
            @Url String url,
            @Query("u") String uid,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

}


