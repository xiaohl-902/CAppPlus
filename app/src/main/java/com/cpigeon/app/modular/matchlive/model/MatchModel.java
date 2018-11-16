package com.cpigeon.app.modular.matchlive.model;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.LocationEntity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.TrainingDataEntity;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.http.RetrofitHelper;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.cpigeon.app.utils.CpigeonConfig.getDataDb;

/**
 * Created by Zhu TingYu on 2018/3/12.
 */

public class MatchModel {
    public static Observable<ApiResponse<LocationEntity>> getFirstByAssociationLocation(int userId, String matchId){
        return PigeonHttpUtil.<ApiResponse<LocationEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<LocationEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_match_first_location)
                .addBody("u", String.valueOf(userId))
                .addBody("id", String.valueOf(matchId))
                .request();
    }

    public static Observable<ApiResponse<LocationEntity>> getFirstByLoftLocation(int userId, String matchId){
        return PigeonHttpUtil.<ApiResponse<LocationEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<LocationEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_match_first_location)
                .addBody("u", String.valueOf(userId))
                .addBody("sid", String.valueOf(matchId))
                .addBody("t", "gp")
                .request();
    }

    public static Observable<ApiResponse<HistoryGradeEntity>> getHistoryGrade(int userId, String foodId, String cardId, String type){
        return PigeonHttpUtil.<ApiResponse<HistoryGradeEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<HistoryGradeEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_history_grade)
                .addBody("u", String.valueOf(userId))
                .addBody("f", foodId)
                .addBody("p", cardId)
                .addBody("t", type)
                .setCache()
                .request();
    }

    public static Observable<ApiResponse<TrainingDataEntity>> getTrainingData(int userId, String foodId, String matchId){
        return PigeonHttpUtil.<ApiResponse<TrainingDataEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<TrainingDataEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_training_data)
                .addBody("u", String.valueOf(userId))
                .addBody("f", foodId)
                .addBody("ssid", matchId)
                .setCache()
                .request();
    }
    public static Observable<ApiResponse<List<MatchInfo>>> getMatchList(CallAPI.DATATYPE.MATCH matchType, int liveDay){
        return PigeonHttpUtil.<ApiResponse<List<MatchInfo>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MatchInfo>>>(){}.getType())
                .setType(HttpUtil.TYPE_GET)
                .url(R.string.api_match_list)
                .addQueryString("t", String.valueOf(matchType.getValue()))
                .addQueryString("bt", String.valueOf(System.currentTimeMillis() / 1000 - 60 * 60 * 24 * liveDay))
                .addQueryString("c", "-1")
                .addQueryString("ht", "1")
                .setCache()
                .request().map(listApiResponse -> {
                    if(listApiResponse.status){
                        if(listApiResponse.data != null && !listApiResponse.data.isEmpty()){
                            StringBuilder builder = new StringBuilder();
                            builder.append("DELETE FROM matchInfo WHERE dt='jg' OR( st>'");
                            builder.append(DateTool.dateToStr(new Date(System.currentTimeMillis() - 1000 * 24 * 60 * 60 * (matchType == CallAPI.DATATYPE.MATCH.GP ? CpigeonConfig.LIVE_DAYS_GP : matchType == CallAPI.DATATYPE.MATCH.XH ? CpigeonConfig.LIVE_DAYS_XH : 0))) + "' AND ssid NOT IN (");
                            for (MatchInfo info : listApiResponse.data) {
                                info.setSsid(EncryptionTool.encryptAES(info.getSsid()));
                                builder.append("'" + info.getSsid() + "',");
                            }
                            builder.deleteCharAt(builder.length() - 1);
                            builder.append(")");
                            //当前类型
                            builder.append((matchType == CallAPI.DATATYPE.MATCH.XH ? "AND lx='xh'" : matchType == CallAPI.DATATYPE.MATCH.GP ? " AND lx='gp'" : ""));
                            builder.append(")");

                            DbManager db = x.getDb(getDataDb());
                            if (listApiResponse.data.size() != 0) {
                                try {
                                    db.execNonQuery(builder.toString());//除此之外的数据全部删除
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }

                            try {
                                db.saveOrUpdate(listApiResponse.data);
                                Logger.i(String.format("list.size=%s\n%s",
                                        listApiResponse.data.size(),
                                        builder.toString()));
                                db.selector(MatchInfo.class).where("lx", "=", listApiResponse.data.get(listApiResponse.data.size() - 1).getLx()).and("st", ">", DateTool.getDayBeginTimeStr(new Date(System.currentTimeMillis() - 1000 * 24 * 60 * 60 * 3))).count();

                            } catch (DbException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                   return listApiResponse;
                });
    }


    public static Observable<ApiResponse<String>> uploadShareImage(int userId, String imageUrl){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("u",String.valueOf(userId));

        File imgF = new File(imageUrl);
        builder.addPart(MultipartBody.Part.createFormData("pic",imgF.getName(),
                RequestBody.create(MediaType.parse("image/*"), imgF)));

        RequestBody requestBody = builder.build();
        RequestParams requestParams = new RequestParams();
        requestParams.setMethod(HttpMethod.POST);
            requestParams.addBodyParameter("", String.valueOf(userId));
        CallAPI.addApiSign(requestParams);

        return RetrofitHelper.getApi().uploadShareGradle(
                CommonTool.getUserToken(MyApp.getInstance().getBaseContext()),
                String.valueOf(System.currentTimeMillis() / 1000),
                CallAPI.addApiSign(requestParams),
                requestBody);

    }
}
