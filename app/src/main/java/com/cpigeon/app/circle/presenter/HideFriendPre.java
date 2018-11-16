package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.CircleFriendEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class HideFriendPre extends HideMessagePre {
    public int page = 1;
    int userId;

    public HideFriendPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getHideList(Consumer<List<CircleFriendEntity>> consumer){
        submitRequestThrowError(CircleModel.hideFriendList(userId, page, 10).map(r -> {
            if(r.isOk()){
                if (r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void getBlackList(Consumer<List<CircleFriendEntity>> consumer){
        submitRequestThrowError(CircleModel.blackList(userId, page, 10).map(r -> {
            if(r.isOk()){
                if (r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

}
