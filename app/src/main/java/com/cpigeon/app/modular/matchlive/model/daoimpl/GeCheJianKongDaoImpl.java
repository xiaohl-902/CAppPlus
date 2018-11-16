package com.cpigeon.app.modular.matchlive.model.daoimpl;

import android.support.annotation.NonNull;

import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.dao.IGeCheJianKongDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 */

public class GeCheJianKongDaoImpl implements IGeCheJianKongDao {

    @Override
    public void getDefaultGYTRaceInfo(String ssid, String raceName, String raceType, @NonNull OnCompleteListener<GeCheJianKongRace> onCompleteListener) {
        CallAPI.getDefaultGYTRaceInfo(ssid, raceName, raceType, new CallAPI.Callback<GeCheJianKongRace>() {
            @Override
            public void onSuccess(GeCheJianKongRace data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("获取失败");
            }
        });
    }
}
