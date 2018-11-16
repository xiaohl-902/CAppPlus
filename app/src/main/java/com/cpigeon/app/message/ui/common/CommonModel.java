package com.cpigeon.app.message.ui.common;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.CommonEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.RHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class CommonModel {

    static Observable<ApiResponse<List<CommonEntity>>> getCommons(int userId){
        return RHttpUtil.<ApiResponse<List<CommonEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CommonEntity>>>(){}.getType())
                .url(R.string.api_common_massage_list)
                .request();
    }

    public static Observable<ApiResponse> addCommonMessage(int userId, String content){

        return RHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_add)
                .addBody("dxnr", content)
                .request();

    }

    static Observable<ApiResponse> modifyCommonMessage(int userId, String messageId ,String content){

        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_modify)
                .setType(HttpUtil.TYPE_POST)
                .addQueryString("u", String.valueOf(userId))
                .addBody("id", messageId)
                .addBody("dxnr", content)
                .request();

    }

    static Observable<ApiResponse> deleteCommonMessage(int userId, String messageIds){

        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_delete)
                .setType(HttpUtil.TYPE_POST)
                .addQueryString("u", String.valueOf(userId))
                .addBody("ids", messageIds)
                .request();

    }


}
