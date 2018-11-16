package com.cpigeon.app.modular.footsearch;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.FootInfoEntity;
import com.cpigeon.app.entity.FootSearchEntity;
import com.cpigeon.app.entity.FootSearchServiceInfoEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.RHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class FootSearchModel {
    public static Observable<ApiResponse<List<FootInfoEntity>>> searchFoot (String keyWord, String year, int userId){
        return CPAPIHttpUtil.<ApiResponse<List<FootInfoEntity>>>build()
                .setType(HttpUtil.TYPE_POST)
                .setToJsonType(new TypeToken<ApiResponse<List<FootInfoEntity>>>(){}.getType())
                .setBaseUrl(R.string.api_base_url_89)
                .url(R.string.api_get_search_foot)
                .addBody("u", String.valueOf(userId))
                .addBody("s", keyWord)
                .addBody("y", year)
                .addBody("t",String.valueOf(1))
                .request();
    }
    public static Observable<ApiResponse<List<FootSearchEntity>>> searchHistory (int page, int count){
        return CPAPIHttpUtil.<ApiResponse<List<FootSearchEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<FootSearchEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_search_foot_history)
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse<List<FootInfoEntity>>> searchHistoryDetails (String id){
        return CPAPIHttpUtil.<ApiResponse<List<FootInfoEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<FootInfoEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_search_foot_details)
                .addBody("id", String.valueOf(id))
                .request();
    }

    public static Observable<ApiResponse> cleanHistory (){
        return CPAPIHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_search_foot_clean_histroy)
                .request();
    }

    public static Observable<ApiResponse<FootSearchServiceInfoEntity>> getUserServiceInfo (){
        return CPAPIHttpUtil.<ApiResponse<FootSearchServiceInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<FootSearchServiceInfoEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_user_service_info)
                .addBody("t","android")
                .addBody("n", "足环查询服务")
                .request();
    }
}
