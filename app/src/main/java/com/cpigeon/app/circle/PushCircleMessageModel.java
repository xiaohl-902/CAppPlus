package com.cpigeon.app.circle;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.utils.http.RetrofitHelper;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Zhu TingYu on 2018/1/20.
 */

public class PushCircleMessageModel {

    public static Observable<ApiResponse<String>> pushMessage(int userId, String message, String location,String lo, double la, double lon, int showType
            , String  messageType, String video, List<String> images) {

        List<String> imgKeys = Lists.newArrayList("pic1"
                ,"pic2"
                ,"pic3"
                ,"pic4"
                ,"pic5"
                ,"pic6"
                ,"pic7"
                ,"pic8"
                ,"pic9");

        Map<String, String> map = new HashMap<>();
        map.put("u", String.valueOf(userId));
        map.put("msg", message);
        map.put("loabs",location);
        map.put("lo", lo);
        map.put("st", String.valueOf(showType));
        map.put("mmt", messageType);
        map.put("phoneType", android.os.Build.MODEL);
        map.put("lat", String.valueOf(la));
        map.put("long", String.valueOf(lon));

        File videoF = null;

        if(StringValid.isStringValid(video)){
            videoF = new File(video);
        }


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("u",getString(String.valueOf(userId)))
                .addFormDataPart("msg",getString(message))
                .addFormDataPart("lo",getString(lo))
                .addFormDataPart("lat",getString(String.valueOf(la)))
                .addFormDataPart("long",getString(String.valueOf(lon)))
                .addFormDataPart("loabs",getString(location))
                .addFormDataPart("st",getString(String.valueOf(showType)))
                .addFormDataPart("mmt",getString(messageType))
                .addFormDataPart("phoneType",getString(android.os.Build.MODEL));

        if (videoF != null) {
            builder.addPart(MultipartBody.Part.createFormData("video",videoF.getName(),
                    RequestBody.create(MediaType.parse("video/*"), videoF)));
        }

        if(images != null && !images.isEmpty()){
            for (int i = 0, len = images.size(); i < len; i++) {
                File imgF = new File(images.get(i));
                builder.addPart(MultipartBody.Part.createFormData(imgKeys.get(i),imgF.getName(),
                        RequestBody.create(MediaType.parse("image/*"), imgF)));
            }
        }

        LogUtil.print("{\n");
        for (String key : map.keySet()) {
            LogUtil.print(key + ": " + map.get(key)+"\n");
        }

        if(images != null && !images.isEmpty()){
            for (int i = 0, len = images.size(); i < len; i++) {
                LogUtil.print(imgKeys.get(i) + ": " + images.get(i)+"\n");
            }
        }

        LogUtil.print("url: " + CPigeonApiUrl.getInstance().getServer()
                        + "/CPAPI/V1/"+ "PushCircleMessage");
        LogUtil.print("sign: " + CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map));


        RequestBody requestBody = builder.build();
        RequestParams requestParams = new RequestParams();
        requestParams.setMethod(HttpMethod.POST);
        for (String key : map.keySet()) {
            requestParams.addBodyParameter(key, map.get(key));
        }
         CallAPI.addApiSign(requestParams);

        return RetrofitHelper.getApi().pushCircleMessage(
                CommonTool.getUserToken(MyApp.getInstance().getBaseContext()),
                //CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                String.valueOf(System.currentTimeMillis() / 1000),
                CallAPI.addApiSign(requestParams),
                requestBody);
    }

    private static String getString(String s){
        return StringValid.isStringValid(s) ? s:"";
    }

}
