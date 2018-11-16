package com.cpigeon.app.message.ui.history;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.MessageEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class MessageHistoryModel {

    public static Observable<ApiResponse<List<MessageEntity>>> MessageHistory(int userId, int date) {
        return PigeonHttpUtil.<ApiResponse<List<MessageEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MessageEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_message_history_list)
                .addQueryString("u", String.valueOf(userId))
                .addBody("y", String.valueOf(date))
                .request();
    }

}
