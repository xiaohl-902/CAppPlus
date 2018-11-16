package com.cpigeon.app.message.ui.home;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class UserGXTModel {
    public static Observable<ApiResponse<UserGXTEntity>> getUserInfo(int userId){
        return PigeonHttpUtil.<ApiResponse<UserGXTEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UserGXTEntity>>(){}.getType())
                .url(R.string.api_user_info)
                .setType(HttpUtil.TYPE_POST)
                .addQueryString("u",String.valueOf(userId))
                .request();
    }
}
