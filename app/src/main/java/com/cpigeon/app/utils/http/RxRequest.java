package com.cpigeon.app.utils.http;

import com.cpigeon.app.utils.databean.ApiResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Zhu TingYu on 2017/12/15.
 */

public class RxRequest {
    public static Observable<String> getRxHttp(RequestUtil rxRequest){
        return Observable.<String>create(observableEmitter -> {
            Map<String, String> body = rxRequest.getBodyParameter();

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String key : body.keySet()) {
                builder.addFormDataPart(key, body.get(key));
            }

            if(body.isEmpty()){
                builder.addFormDataPart("empty","empty");
            }
            Call<ResponseBody> call;

            if(rxRequest.isCache){
                call = rxRequest.getRequestInterface().request(rxRequest.getUrl(), String.valueOf(rxRequest.getUid()),rxRequest.getSign(), builder.build());

            }else {
                call = rxRequest.getRequestInterface().requestHaveCache(rxRequest.getUrl(), String.valueOf(rxRequest.getUid()),rxRequest.getSign(), builder.build());
            }

            Response<ResponseBody> response = call.execute();

            if(response.code() == 200){
                observableEmitter.onNext(response.body().string());
            }else {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.errorCode = -1;
                apiResponse.msg = "服务器内部异常";
                observableEmitter.onNext(GsonUtil.toJson(apiResponse));
            }

        });
    }
}
