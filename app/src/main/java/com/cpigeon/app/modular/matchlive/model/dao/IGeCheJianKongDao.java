package com.cpigeon.app.modular.matchlive.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;

import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 */

public interface IGeCheJianKongDao extends IBaseDao {

    void getDefaultGYTRaceInfo(String ssid,
                               String raceName,
                               String raceType,
                               @NonNull OnCompleteListener<GeCheJianKongRace> onCompleteListener);

}
