package com.cpigeon.app.pigeonnews.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.NewsMessageEntity;
import com.cpigeon.app.pigeonnews.NewsModel;

import java.util.function.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/10.
 */

public class PigeonNewsPre extends BasePresenter {

    private static final int TYPE_EARTH_QUAKE = 1;
    private static final int TYPE_EARTH_QUAKE2 = 1;


    public PigeonNewsPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    /*public void getNewsMessage(Consumer<Boolean> consumer){
        submitRequestThrowError(NewsModel.newsMessage().map(r -> {

        }));
    }*/

}
