package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface IGYTRaceLocation extends IBaseDao {
    void loadRaceLocation(String rid,
                          String lid,
                          String hw,
                          OnCompleteListener<List<GYTRaceLocation>> listOnCompleteListener);
}
