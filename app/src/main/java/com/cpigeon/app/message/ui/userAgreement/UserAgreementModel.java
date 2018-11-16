package com.cpigeon.app.message.ui.userAgreement;

import com.cpigeon.app.R;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class UserAgreementModel {

    public static Observable<ApiResponse> setUserAgreement(int userId) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_set_user_agreement)
                .addQueryString("u", String.valueOf(userId))
                .request();
    }
}
