package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.dao.IUserFollowDao;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public interface IRaceDao extends IBaseDao, IUserFollowDao {
    void showReprotData(int px, String matchType, String ssid, String foot, String name, boolean hascz, int pager,
                        int pagesize, int czIndex, String sKey, IBaseDao.OnCompleteListener<List> onCompleteListener);

    void showPigeonData(String matchType, String ssid, String foot, String name, boolean hascz, int pager,
                        int pagesize, int czIndex, String sKey, IBaseDao.OnCompleteListener<List> onCompleteListener);
}
