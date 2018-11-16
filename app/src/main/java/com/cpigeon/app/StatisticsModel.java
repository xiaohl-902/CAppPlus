package com.cpigeon.app;

import com.cpigeon.app.entity.LoginAwardScoreEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/6/7.
 */

public class StatisticsModel {
    public static Observable<ApiResponse<LoginAwardScoreEntity>> addOpenAppStatistics(int userId) {
        return CPAPIHttpUtil.<ApiResponse<LoginAwardScoreEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<LoginAwardScoreEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_add_open_app_statistics)
                .addBody("u", String.valueOf(userId))
                .addBody("t", "中鸽网APP")
                .addBody("ly", "安卓")
                .addBody("sb", android.os.Build.MODEL)
                .request();
    }
}
