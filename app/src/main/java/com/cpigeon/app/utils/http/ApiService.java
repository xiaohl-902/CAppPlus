package com.cpigeon.app.utils.http;

import com.cpigeon.app.utils.databean.ApiResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Zhu TingYu on 2017/12/1.
 */

public interface ApiService {
    /*@Multipart
    @FormUrlEncoded*/
    @POST("GXT_XGQMXX")
    Observable<ApiResponse> modifySign(
            @Header("u") String token,
            @Query("u") String userId,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

    @POST("GXT_XGGRXX")
    Observable<ApiResponse> modifyPersonInfo(
            @Header("u") String token,
            @Query("u") String userId,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

    @POST("GXT_TJGRXX")
    Observable<ApiResponse> uploadPersonInfo(
            @Header("u") String token,
            @Query("u") String userId,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

    @POST("PushCircleMessage")
    Observable<ApiResponse<String>> pushCircleMessage(
            @Header("u") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Body RequestBody requestBody);

    @POST("SharePic")
    Observable<ApiResponse<String>> uploadShareGradle(
            @Header("u") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Body RequestBody requestBody);


}
