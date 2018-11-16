package com.cpigeon.app.home;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.entity.DynamicEntity;
import com.cpigeon.app.entity.HomeAdEntity;
import com.cpigeon.app.entity.HomeMessageEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.http.RHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/1/8.
 */

public class HomeModel {
    static Observable<List<HomeAd>> getAd() {
        return Observable.<List<HomeAd>>create(observableEmitter -> {
            String adJsonStr = (String) SharedPreferencesTool.Get(MyApp.getInstance(), "ad", "");
            try {
                JSONArray array = new JSONArray(adJsonStr);
                if (array.length() > 0) {
                    List<HomeAd> adList = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date _begin, _end;
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            _begin = sdf.parse(array.getJSONObject(i).getString("start"));
                            _end = sdf.parse(array.getJSONObject(i).getString("end"));
                            if (array.getJSONObject(i).getInt("type") == 2 &&
                                    array.getJSONObject(i).getBoolean("enable") == true &&
                                    _begin.getTime() < new Date().getTime() && _end.getTime() > new Date().getTime()) {
                                final String imageUrl = array.getJSONObject(i).getString("adImageUrl");
                                final String adurl = array.getJSONObject(i).getString("adUrl");
                                Logger.i("第" + (i + 1) + "个下方广告;URL=" + imageUrl);
                                adList.add(new HomeAd(imageUrl, adurl));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    observableEmitter.onNext(adList);
                }
            } catch (JSONException e) {
                observableEmitter.onError(e);
            }

        });
    }

    static Observable<ApiResponse<List<NewsEntity>>> homeSpeedNews() {
        return CPAPIHttpUtil.<ApiResponse<List<NewsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_home_speed_news)
                .request();
    }

    static Observable<ApiResponse<List<NewsEntity>>> homeNewsList(int top) {
        return RHttpUtil.<ApiResponse<List<NewsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsEntity>>>() {
                }.getType())
                .url(R.string.api_home_news)
                .addBody("top", String.valueOf(top))
                .request();
    }

    static Observable<ApiResponse<List<DynamicEntity>>> homeDynamicList(int top) {
        return CPAPIHttpUtil.<ApiResponse<List<DynamicEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<DynamicEntity>>>() {
                }.getType())
                .url(R.string.api_home_circle_dynamic)
                .setType(HttpUtil.TYPE_POST)
                .addBody("top", String.valueOf(top))
                .request();
    }

    static Observable<ApiResponse<List<HomeMessageEntity>>> homeMessage() {
        return HttpUtil.<ApiResponse<List<HomeMessageEntity>>>builder()
                .setHeadUrl(R.string.api_head_url)
                .addHeader("auth", CommonTool.getUserToken(MyApp.getInstance().getBaseContext()))
                .setUserId("uid", CpigeonData.getInstance().getUserId(MyApp.getInstance().getBaseContext()))
                .setToJsonType(new TypeToken<ApiResponse<List<HomeMessageEntity>>>() {
                }.getType())
                .url(R.string.api_home_message)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }

}
