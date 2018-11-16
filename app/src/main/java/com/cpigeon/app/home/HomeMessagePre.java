package com.cpigeon.app.home;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.HomeMessageEntity;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/30.
 */

public class HomeMessagePre extends BasePresenter {
    public HomeMessagePre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }
    public void getHomeMessage(Consumer<List<HomeMessageEntity>> consumer){
        submitRequestThrowError(HomeModel.homeMessage().map(r -> {
            if(r.isHaveDate()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

}
