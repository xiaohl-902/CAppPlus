package com.cpigeon.app.message.ui.selectPhoneNumber;

import com.cpigeon.app.R;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class SelectPhoneModel {
    static Observable<ApiResponse> getCommons(int userId, int GroupId, String phones){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_input_number_of_phone)
                .addQueryString("u",String.valueOf(userId))
                .addBody("fzid", String.valueOf(GroupId))
                .addBody("c", phones)
                .request();
    }
}
