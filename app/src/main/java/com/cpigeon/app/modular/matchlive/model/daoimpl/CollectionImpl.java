package com.cpigeon.app.modular.matchlive.model.daoimpl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.Collection;
import com.cpigeon.app.modular.matchlive.model.dao.ICollection;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenshuai on 2017/5/9.
 */

public class CollectionImpl implements ICollection {
    @Override
    public void removeColleaction(Collection coll, @NonNull IBaseDao.OnCompleteListener<Boolean> onCompleteListener) {
        if (coll == null) {
            onCompleteListener.onFail(null);
        } else {
            DbManager db = x.getDb(CpigeonConfig.getDataDb());
            boolean result = true;
            try {
                db.delete(coll);
                db.close();
            } catch (DbException e) {
                result = false;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
            onCompleteListener.onSuccess(result);
        }
    }

    @Override
    public void saveColleaction(Collection coll, @NonNull IBaseDao.OnCompleteListener<Collection> onCompleteListener) {
        if (coll == null) {
            onCompleteListener.onFail(null);
        } else {
            if (coll.getCollUserid() != CpigeonData.getInstance().getUserId(MyApp.getInstance()))
                coll.setCollUserid(CpigeonData.getInstance().getUserId(MyApp.getInstance()));
            DbManager db = x.getDb(CpigeonConfig.getDataDb());
            try {
                db.saveOrUpdate(coll);
            } catch (DbException e) {
                e.printStackTrace();
            } finally {
            }
            onCompleteListener.onSuccess(coll);
        }
    }

    @Override
    public void getCollections(@NonNull IBaseDao.OnCompleteListener<List<Collection>> onCompleteListener) {
        getCollections(CpigeonData.getInstance().getUserId(MyApp.getInstance()), null, null, onCompleteListener);
    }

    @Override
    public void getCollections(int userid, String type, String key, @NonNull IBaseDao.OnCompleteListener<List<Collection>> onCompleteListener) {
        DbManager db = x.getDb(CpigeonConfig.getDataDb());
        List<Collection> all = null;
        try {
            Selector<Collection> selector = db.selector(Collection.class);
            selector.where("colluserid", "=", userid);
            if (!TextUtils.isEmpty(type))
                selector.where("type", "=", type);
            if (!TextUtils.isEmpty(key))
                selector.where("key", "=", key);
            all = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        } finally {
        }
        onCompleteListener.onSuccess(all);
    }

}
