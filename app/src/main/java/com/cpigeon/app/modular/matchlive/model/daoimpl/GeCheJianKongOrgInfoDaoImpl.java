package com.cpigeon.app.modular.matchlive.model.daoimpl;

import android.support.annotation.NonNull;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IGeCheJianKongOrgInfoDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenshuai on 2017/7/11.
 */

public class GeCheJianKongOrgInfoDaoImpl implements IGeCheJianKongOrgInfoDao {
    @Override
    public void getGYTRaceListGroupByOrg(String searchkey, String orgType, int pageIndex, int pageSize, @NonNull final OnCompleteListener<List<GeCheJianKongOrgInfo>> onCompleteListener) {
        /*CallAPI.getGYTRaceListGroupByOrg(searchkey, orgType, pageIndex, pageSize, new CallAPI.Callback<List<GeCheJianKongOrgInfo>>() {
            @Override
            public void onSuccess(List<GeCheJianKongOrgInfo> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("获取失败");
            }
        });*/
        MatchModel.getApiPigeonList(searchkey, orgType, pageIndex, pageSize).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(listApiResponse -> {
            onCompleteListener.onSuccess(listApiResponse.data);
        }, throwable -> {onCompleteListener.onFail("获取失败");});

    }
}
