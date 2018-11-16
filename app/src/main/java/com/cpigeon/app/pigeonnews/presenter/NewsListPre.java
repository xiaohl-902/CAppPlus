package com.cpigeon.app.pigeonnews.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.NewsDetailsEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.pigeonnews.NewsModel;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2018/1/13.
 */

public class NewsListPre extends BasePresenter{

    public int page = 1;

    public NewsListPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void newsList(Consumer<List<NewsEntity>> consumer){
        submitRequestThrowError(NewsModel.newsList(page, 8).map(r -> {
            if(r.status){
                return r.data;
            }else return Lists.newArrayList();
        }),consumer);

    }

}
