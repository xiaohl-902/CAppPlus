package com.cpigeon.app.pigeonnews;

import android.support.annotation.Nullable;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.NewsCommentEntity;
import com.cpigeon.app.entity.NewsDetailsEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.entity.NewsMessageEntity;
import com.cpigeon.app.entity.SnsEntity;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class NewsModel {
    public static Observable<ApiResponse<NewsDetailsEntity>> newsDetails(String newsId){
        return CPAPIHttpUtil.<ApiResponse<NewsDetailsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<NewsDetailsEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_news_details)
                .addBody("nid",newsId)
                .request();
    }

    public static Observable<ApiResponse<List<NewsMessageEntity>>> newsMessage(){
        return CPAPIHttpUtil.<ApiResponse<List<NewsMessageEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsMessageEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_news_GetDiZhenCiBao)
                .request();
    }

    public static Observable<ApiResponse<List<NewsCommentEntity>>> getNewsComments(String newsId, int page){
        return CPAPIHttpUtil.<ApiResponse<List<NewsCommentEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsCommentEntity>>>(){}.getType())
                .url(R.string.api_news_comments)
                .setType(HttpUtil.TYPE_POST)
                .addBody("nid", newsId)
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(6))
                .request();
    }

    public static Observable<ApiResponse<NewsCommentEntity>> addNewsComments (String newsId, String content){
        return CPAPIHttpUtil.<ApiResponse<NewsCommentEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<NewsCommentEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_add_news_comment)
                .addBody("nid", newsId)
                .addBody("c", content)
                .request();
    }

    public static Observable<ApiResponse> addReplyForNews (String replyId,String commentId, String content){
        return CPAPIHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_add_reply_for_news)
                .addBody("id", replyId)
                .addBody("cid", commentId)
                .addBody("c", content)
                .request();
    }

    public static Observable<ApiResponse<SnsEntity>> newsThumb (String newsId){
        return CPAPIHttpUtil.<ApiResponse<SnsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<SnsEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_thumb_news)
                .addBody("nid", newsId)
                .request();
    }

    public static Observable<ApiResponse<SnsEntity>> newsCommentThumb (String newsCommentId){
        return CPAPIHttpUtil.<ApiResponse<SnsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<SnsEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_thumb_news_comment)
                .addBody("cid", newsCommentId)
                .request();
    }

    public static Observable<ApiResponse<List<NewsEntity>>> newsList(int page, int count, @Nullable String type){
        return CPAPIHttpUtil.<ApiResponse<List<NewsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_news_list)
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .addBody("t", type)
                .request();
    }

    public static Observable<ApiResponse<List<NewsEntity>>> newsList(int page, int count){
        return newsList(page, count,null);
    }

    public static Observable<ApiResponse<List<NewsEntity>>> newsRelated(String newsId){
        return CPAPIHttpUtil.<ApiResponse<List<NewsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<NewsEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_news_related)
                .addBody("nid", newsId)
                .request();
    }
}
