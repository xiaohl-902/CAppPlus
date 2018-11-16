package com.cpigeon.app.modular.matchlive.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.Collection;

import java.util.List;

/**
 * Created by chenshuai on 2017/5/9.
 */

public interface ICollection {

    /**
     * 移除收藏
     *
     * @param coll
     * @param onCompleteListener
     */
    void removeColleaction(Collection coll, @NonNull IBaseDao.OnCompleteListener<Boolean> onCompleteListener);

    /**
     * 保存收藏
     *
     * @param coll
     * @param onCompleteListener
     */
    void saveColleaction(Collection coll, @NonNull IBaseDao.OnCompleteListener<Collection> onCompleteListener);

    /**
     * 获取当前用户的所有收藏
     *
     * @param onCompleteListener
     */
    void getCollections(@NonNull IBaseDao.OnCompleteListener<List<Collection>> onCompleteListener);

    /**
     * 获取用户的收藏
     *
     * @param userid
     * @param type
     * @param key
     * @param onCompleteListener
     */
    void getCollections(int userid, String type, String key, @NonNull IBaseDao.OnCompleteListener<List<Collection>> onCompleteListener);
}
