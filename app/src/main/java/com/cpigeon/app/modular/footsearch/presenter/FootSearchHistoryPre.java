package com.cpigeon.app.modular.footsearch.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.FootInfoEntity;
import com.cpigeon.app.entity.FootSearchEntity;
import com.cpigeon.app.modular.footsearch.FootSearchModel;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class FootSearchHistoryPre extends BasePresenter {

    public int page = 1;
    public String id;

    public FootSearchHistoryPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getHistory(Consumer<List<FootSearchEntity>> consumer){
        submitRequestThrowError(FootSearchModel.searchHistory(page, 10).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void getHistoryDetails(Consumer<List<FootInfoEntity>> consumer){
        submitRequestThrowError(FootSearchModel.searchHistoryDetails(id).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void cleanHistory(Consumer<String> consumer){
        submitRequestThrowError(FootSearchModel.cleanHistory().map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }
}
