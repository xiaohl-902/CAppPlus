package com.cpigeon.app.circle;

import android.support.annotation.Nullable;


import com.cpigeon.app.R;
import com.cpigeon.app.entity.CircleFriendEntity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.entity.CircleNearbyEntity;
import com.cpigeon.app.entity.CircleUserInfoEntity;
import com.cpigeon.app.entity.HideMessageEntity;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class CircleModel {

    public static Observable<ApiResponse<CircleUserInfoEntity>> getCircleInfo(int userId, int type){
        return PigeonHttpUtil.<ApiResponse<CircleUserInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<CircleUserInfoEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_circle_user_info)
                .addBody("u", String.valueOf(userId))
                .addBody("t", String.valueOf(type))
                .request();
    }


    public static Observable<ApiResponse<List<CircleMessageEntity>>> circleMessage(int userId, String type, @Nullable int messageId, int page, int count){
        return PigeonHttpUtil.<ApiResponse<List<CircleMessageEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CircleMessageEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_message_list)
                .addBody("u", String.valueOf(userId))
                .addBody("lt", type)
                .addBody("mid", String.valueOf(messageId))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse> circleFollow(int userId, int followId, int isFollow){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_user_follow)
                .addBody("u", String.valueOf(userId))
                .addBody("auid", String.valueOf(followId))
                .addBody("isa", String.valueOf(isFollow))
                .request();
    }

    public static Observable<ApiResponse> circleMessageThumbUp(int userId, int messageId, int isThumb){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_thumb_message)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .addBody("isp", String.valueOf(isThumb))
                .request();
    }

    public static Observable<ApiResponse<CircleMessageEntity.CommentListBean>> addCircleMessageComment(int userId, int messageId, String content, int previousId, int commentId){
        return PigeonHttpUtil.<ApiResponse<CircleMessageEntity.CommentListBean>>build()
                .setToJsonType(new TypeToken<ApiResponse<CircleMessageEntity.CommentListBean>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_add_message_comment)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .addBody("c", content)
                .addBody("topid", String.valueOf(previousId))
                .addBody("cid", String.valueOf(commentId))
                .request();
    }

    public static Observable<ApiResponse> deleteCircleMessageComment(int userId, int commentId){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_delete_message_comment)
                .addBody("u", String.valueOf(userId))
                .addBody("cid", String.valueOf(commentId))
                .request();
    }

    public static Observable<ApiResponse> hideCircleMessage(int userId, int messageId, int isHide){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_hide_circle_message)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .addBody("iss", String.valueOf(isHide))
                .request();
    }

    public static Observable<ApiResponse> hideCircleUser(int userId, int hidedUserId, int isHide){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_hide_circle_user)
                .addBody("u", String.valueOf(userId))
                .addBody("suid", String.valueOf(hidedUserId))
                .addBody("iss", String.valueOf(isHide))
                .request();
    }

    public static Observable<ApiResponse> addUserToBlackList(int userId, int blackUserId, int isHide){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_add_user_black_list)
                .addBody("u", String.valueOf(userId))
                .addBody("duid", String.valueOf(blackUserId))
                .addBody("isd", String.valueOf(isHide))
                .request();
    }

    public static Observable<ApiResponse<List<CircleFriendEntity>>> fansAndFollowList(int userId, String type, int page, int count){
        return PigeonHttpUtil.<ApiResponse<List<CircleFriendEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CircleFriendEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_fans_and_follow)
                .addBody("u", String.valueOf(userId))
                .addBody("t", type)
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse<List<CircleFriendEntity>>> hideFriendList(int userId, int page, int count){
        return PigeonHttpUtil.<ApiResponse<List<CircleFriendEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CircleFriendEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_hide_user_list)
                .addBody("u", String.valueOf(userId))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse<List<CircleFriendEntity>>> blackList(int userId, int page, int count){
        return PigeonHttpUtil.<ApiResponse<List<CircleFriendEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CircleFriendEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_user_black_list)
                .addBody("u", String.valueOf(userId))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse<List<HideMessageEntity>>> hideMessageList(int userId, int page, int count){
        return PigeonHttpUtil.<ApiResponse<List<HideMessageEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<HideMessageEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_hide_message_list)
                .addBody("u", String.valueOf(userId))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .request();
    }

    public static Observable<ApiResponse> forwardMessage(int userId, int messageId, String content){
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_hide_message_list)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .addBody("c", content)
                .request();
    }

    public static Observable<ApiResponse<String>> addMessageReport(String content, int messageId){
        return CPAPIHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_pigeon_report_message)
                .addBody("c", content)
                .addBody("t", "circle_msg")
                .addBody("rm", String.valueOf(messageId))
                .request();
    }

    public static Observable<ApiResponse<String>> setMessageShowType(int userId, int messageId, int showType){
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_message_show_type)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .addBody("st", String.valueOf(showType))
                .request();
    }

    public static Observable<ApiResponse<String>> deleteMessage(int userId, int messageId){
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_message_delete)
                .addBody("u", String.valueOf(userId))
                .addBody("mid", String.valueOf(messageId))
                .request();
    }


    public static Observable<ApiResponse<List<CircleNearbyEntity>>> circleNearby(int userId, String province, String city){
        return PigeonHttpUtil.<ApiResponse<List<CircleNearbyEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CircleNearbyEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_circle_nearby)
                .addBody("u", String.valueOf(userId))
                .addBody("p", province)
                .addBody("c", city)
                .request();
    }

}
