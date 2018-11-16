package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;
import com.cpigeon.app.modular.matchlive.model.dao.IGYTRaceLocation;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GYTRaceLocationImpl implements IGYTRaceLocation {

    @Override
    public void loadRaceLocation(String rid, String lid, String hw, final IBaseDao.OnCompleteListener<List<GYTRaceLocation>> listOnCompleteListener) {
        CallAPI.getGYTRaceLocation(rid, lid, hw, new CallAPI.Callback<List<GYTRaceLocation>>() {
            @Override
            public void onSuccess(List<GYTRaceLocation> data) {
                listOnCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listOnCompleteListener.onFail("获取失败");
            }
        });
    }
}
