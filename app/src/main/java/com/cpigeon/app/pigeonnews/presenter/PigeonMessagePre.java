package com.cpigeon.app.pigeonnews.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.NewsMessageEntity;
import com.cpigeon.app.pigeonnews.NewsModel;
import com.cpigeon.app.pigeonnews.ui.PigeonMessageFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/13.
 */

public class PigeonMessagePre extends BasePresenter {

    public int type;
    List<NewsMessageEntity> data;

    public PigeonMessagePre(Activity activity) {
        super(activity);

    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getMessage(Consumer<NewsMessageEntity> consumer){
        submitRequestThrowError(NewsModel.newsMessage().map(r -> {
            if(r.status){
                NewsMessageEntity rentity = null;
                for (NewsMessageEntity entity : r.data) {
                    if(entity.type == type){
                        rentity = entity;
                    }
                }
                return rentity;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

}
