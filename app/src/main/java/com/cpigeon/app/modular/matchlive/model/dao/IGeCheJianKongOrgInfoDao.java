package com.cpigeon.app.modular.matchlive.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;

import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 */

public interface IGeCheJianKongOrgInfoDao extends IBaseDao {

    void getGYTRaceListGroupByOrg(String searchkey,
                                  String orgType,
                                  int pageIndex, int pageSize, @NonNull IBaseDao.OnCompleteListener<List<GeCheJianKongOrgInfo>> onCompleteListener);
}
