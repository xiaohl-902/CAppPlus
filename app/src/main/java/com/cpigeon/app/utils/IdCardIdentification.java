package com.cpigeon.app.utils;

import com.cpigeon.app.entity.IdCardPInfoEntity;
import com.cpigeon.app.utils.http.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.youtu.Youtu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class IdCardIdentification {
    public static final int  TYPE_POSITIVE = 0;
    public static final int  TYPE_NOT_POSITIVE = 1;

    Youtu faceYoutu;

    public static final String APP_ID = "10109853";
    public static final String SECRET_ID = "AKIDseEMJ71T5Wm5RaKda5dDpLmFlHjeRp2G";
    public static final String SECRET_KEY = "rGiElM7xpiDcJdKwAxfIuOnkiiAXqHmJ";
    public static final String USER_ID = "2851551317";

    public IdCardIdentification(){
        faceYoutu = new Youtu(APP_ID,SECRET_ID,SECRET_KEY,Youtu.API_YOUTU_END_POINT);
    }

    public void  IdCardOcr(final String path, final int type, Consumer<JSONObject> consumer){
        Observable.<JSONObject>create(observableEmitter -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = faceYoutu.IdCardOcr(path,type);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            observableEmitter.onNext(jsonObject);
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }
}
