package com.cpigeon.app.modular.matchlive.model;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.CircleUserInfoEntity;
import com.cpigeon.app.entity.MapLiveEntity;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/3/5.
 */

public class MapLiveModel {

    public static Observable<ApiResponse<MapLiveEntity>> getPosition(String rid, String lid){
        return PigeonHttpUtil.<ApiResponse<MapLiveEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<MapLiveEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_GET)
                .url(R.string.api_map_live_point)
                .addQueryString("rid", rid)
                .addQueryString("lid", lid)
                .addQueryString("hw", "y")
                .request();
    }
}
