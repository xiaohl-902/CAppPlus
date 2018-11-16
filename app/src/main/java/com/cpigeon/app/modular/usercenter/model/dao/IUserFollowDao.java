package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;

import java.util.List;

/**
 * Created by chenshuai on 2017/6/28.
 */

public interface IUserFollowDao extends IBaseDao {

    /**
     * 获取关注列表
     *
     * @param pageIndex
     * @param pageSize
     * @param type
     * @param onCompleteListener
     */
    void getUserRaceFollows(int pageIndex, int pageSize, String type, IBaseDao.OnCompleteListener<List<UserFollow>> onCompleteListener);

    /**
     * 获取用户所有的关注列表
     *
     * @param onCompleteListener
     */
    void getAllUserRaceFollows(int cacheTime, IBaseDao.OnCompleteListener<List<UserFollow>> onCompleteListener);

    /**
     * 添加比赛关注
     *
     * @param ssid
     * @param followType
     * @param displayName
     * @param onCompleteListener
     */
    void addUserRaceFollow(String ssid, String followType, String displayName, IBaseDao.OnCompleteListener<UserFollow> onCompleteListener);

    /**
     * 移除关注
     *
     * @param followId
     * @param ssid
     * @param followType
     * @param onCompleteListener
     */
    void removeUserRaceFollow(int followId, String ssid, String followType, IBaseDao.OnCompleteListener<Integer> onCompleteListener);

}
