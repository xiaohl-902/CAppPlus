package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.CircleNearbyEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/5/28.
 */

public class CircleNearbyPre extends BasePresenter{

    int userId;
    public String province;
    public String city;
    public List<CircleNearbyEntity> data;

    public CircleNearbyPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getNearby(Consumer<List<CircleNearbyEntity>> consumer){
        submitRequestThrowError(CircleModel.circleNearby(userId, province, city).map(r -> {
            if(r.isOk()){
                if(r.status){
                    this.data = r.data;
                    return this.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }
}
