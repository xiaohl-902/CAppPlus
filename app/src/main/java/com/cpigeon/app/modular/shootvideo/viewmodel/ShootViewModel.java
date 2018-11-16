package com.cpigeon.app.modular.shootvideo.viewmodel;


import android.util.Log;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.lineweather.model.bean.UllageToolEntity;
import com.cpigeon.app.modular.lineweather.view.viewdeo.ILineWeatherView;
import com.cpigeon.app.modular.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.app.utils.StringUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.cpigeon.app.utils.http.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class ShootViewModel extends BasePresenter {

    //我的鸽币
    public ShootInfoEntity mShootInfoEntity = new ShootInfoEntity.Builder().build();

    protected Map<String, Object> postParams = new HashMap<>();//存放参数
    protected long timestamp;//时间搓

    public ShootViewModel(IView mView) {
        super(mView);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取用户头像和鸽舍名称
    public void getShootData(Consumer<ShootInfoEntity> consumer) {
        submitRequestThrowError(ILineWeatherView.getShoot().map(r -> {
            if (r.isOk()) {
                return r.data;
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    //设置用户头像和鸽舍名称
    public void setShootData(Consumer<Object> consumer) {

        if (StringUtil.isStringValid(mShootInfoEntity.getImgurl())) {
            if (mShootInfoEntity.getImgurl().contains("http:")) {
                mShootInfoEntity.setImgurl("");
            }
        }

        submitRequestThrowError(ILineWeatherView.setShoot(mShootInfoEntity.getSszz(), mShootInfoEntity.getImgurl()).map(r -> {
            if (r.isOk()) {
                return r.msg;
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }
}
