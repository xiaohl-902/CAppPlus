package com.cpigeon.app.message.ui.sendmessage;

import com.cpigeon.app.R;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/28.
 */

public class SendMessageModel {
    public static Observable<ApiResponse> sendMessage(int userId, String groupIds, String content, String number) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_send_message)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzids", groupIds)
                .addBody("dxnr", content)
                .addBody("sjhm", number)
                .request();
    }
}
