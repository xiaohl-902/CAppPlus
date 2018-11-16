package com.cpigeon.app.pigeonnews.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.NewsCommentEntity;
import com.cpigeon.app.entity.SnsEntity;
import com.cpigeon.app.pigeonnews.NewsModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/11.
 */

public class NewsCommentsPre extends BasePresenter {

    public String newsId;
    public int page = 1;
    public String commentId;
    public String content;
    public String replyId;

    public NewsCommentsPre(Activity activity) {
        super(activity);
        newsId = activity.getIntent().getStringExtra(IntentBuilder.KEY_DATA);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getNewsComments(Consumer<List<NewsCommentEntity>> consumer) {
        submitRequestThrowError(NewsModel.getNewsComments(newsId,page).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void replyComment(Consumer<String> consumer){
        submitRequestThrowError(NewsModel.addReplyForNews(commentId,replyId,content).map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void addNewsComment(Consumer<NewsCommentEntity> consumer){
        submitRequestThrowError(NewsModel.addNewsComments(newsId, content).map(r -> {
            if(r.status){
                NewsCommentEntity replyEntity = r.data;
                replyEntity.nicheng = CpigeonData.getInstance().getUserInfo().getNickname();
                return replyEntity;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void thumbNewsComment(Consumer<SnsEntity> consumer){
        submitRequestThrowError(NewsModel.newsCommentThumb(commentId).map(r -> {
            if(r.status){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }
}
