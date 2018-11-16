package com.cpigeon.app.message.ui.order.ui;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.GXTMessagePrice;
import com.cpigeon.app.entity.MessageOrderEntity;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.entity.UserBalanceEntity;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.entity.WeiXinPayEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class OrderModel {
    public static Observable<ApiResponse<OrderInfoEntity>> greatServiceOrder(int userId) {
        return PigeonHttpUtil.<ApiResponse<OrderInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<OrderInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_open_GXT_order)
                .addQueryString("u", String.valueOf(userId))
                .addBody("ly", "中鸽网APP")
                .addBody("uc", "android")
                .request();
    }

    public static Observable<ApiResponse<OrderInfoEntity>> createGXTMessageOrder(int userId, int count, double money) {
        return PigeonHttpUtil.<ApiResponse<OrderInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<OrderInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_create_GXT_message_order)
                .addQueryString("u", String.valueOf(userId))
                .addBody("czts", String.valueOf(count))
                .addBody("money", String.valueOf(money))
                .addBody("ly", "中鸽网APP")
                .addBody("uc", "android")
                .request();
    }

    public static Observable<ApiResponse> payOrderByBalance(int userId, String orderId, String password) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_pay_order_by_balance)
                .addQueryString("u", String.valueOf(userId))
                .addBody("oid", orderId)
                .addBody("p", EncryptionTool.encryptAES(password))
                .request();
    }

    public static Observable<ApiResponse<UserBalanceEntity>> getUserBalance(int userId) {
        return PigeonHttpUtil.<ApiResponse<UserBalanceEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UserBalanceEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_user_balance_info)
                .addQueryString("u", String.valueOf(userId))
                .request();
    }

    public static Observable<ApiResponse<WeiXinPayEntity>> greatWXOrder(int userId, String orderId) {
        return PigeonHttpUtil.<ApiResponse<WeiXinPayEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<WeiXinPayEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_wx_order)
                .addQueryString("u", String.valueOf(userId))
                .addBody("oid", orderId)
                .request();
    }

    public static Observable<ApiResponse<GXTMessagePrice>> getMessagePrice() {
        return PigeonHttpUtil.<ApiResponse<GXTMessagePrice>>build()
                .setToJsonType(new TypeToken<ApiResponse<GXTMessagePrice>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_GXT_message_price)
                .request();
    }


    public static Observable<ApiResponse<List<MessageOrderEntity>>> getMessageOrderHistory(int userId, String startTime, String endTime) {
        return PigeonHttpUtil.<ApiResponse<List<MessageOrderEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MessageOrderEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_GXT_message_order_history)
                .addQueryString("u", String.valueOf(userId))
                .addBody("u", String.valueOf(userId))
                .addBody("t1", startTime)
                .addBody("t2", endTime)
                .request();
    }

    public static Observable<ApiResponse<String>> setVoiceInfo(int userId, String voiceId, String unitName
            , String TFN, String voiceType, String name, String phoneNumber, String province, String city, String county, String address, String email) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_set_voice_info)
                .addBody("u", String.valueOf(userId))
                .addBody("id", voiceId)
                .addBody("dw", unitName)
                .addBody("sh", TFN)
                .addBody("lx", voiceType)
                .addBody("lxr", name)
                .addBody("dh", phoneNumber)
                .addBody("dz", address)
                .addBody("yx", email)
                .addBody("p", province)
                .addBody("c", city)
                .addBody("a", county)
                .request();
    }

    public static Observable<ApiResponse<List<VoiceEntity>>> getVoiceList(int userId) {
        return PigeonHttpUtil.<ApiResponse<List<VoiceEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<VoiceEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_voice_list)
                .addBody("u", String.valueOf(userId))
                .request();
    }

    public static Observable<ApiResponse<String>> deleteVoice(int userId, String voiceId) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_delete_voice)
                .addBody("u", String.valueOf(userId))
                .addBody("id", voiceId)
                .request();
    }

    public static Observable<ApiResponse<String>> bindVoiceOnOrder(int userId, String voiceId, String orderId, String isBind) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_bind_voice_on_order)
                .addBody("u", String.valueOf(userId))
                .addBody("id", voiceId)
                .addBody("oid", orderId)
                .addBody("bind", isBind)
                .request();
    }

    public static Observable<ApiResponse<VoiceEntity>> getVoiceInfo(int userId, String voiceId) {
        return PigeonHttpUtil.<ApiResponse<VoiceEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<VoiceEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_voice_by_id)
                .addBody("u", String.valueOf(userId))
                .addBody("id", voiceId)
                .request();
    }
}
