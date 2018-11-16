package com.cpigeon.app.modular.saigetong.presenter;

import android.app.Activity;
import android.util.Log;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.saigetong.model.bead.SGTFootSearchEntity;
import com.cpigeon.app.modular.saigetong.model.bead.SGTGzListEntity;
import com.cpigeon.app.modular.saigetong.model.bead.SGTImgEntity;
import com.cpigeon.app.modular.saigetong.model.bead.SGTRpRecordEntity;
import com.cpigeon.app.modular.saigetong.model.bead.SGTUserListEntity;
import com.cpigeon.app.modular.saigetong.model.daoimpl.ISGTImpl;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/1/20.
 */
public class SGTPresenter extends BasePresenter {

    public int pi = 1;
    public int pi2 = 1;
    public int pi3 = -1;

    public String guid;
    public String tID;

    private String keyWord;

    public SGTPresenter(Activity mView) {
        super(mView);
        guid = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA);
        tID = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取赛鸽通用户列表
    public void getSGTHomeData(Consumer<List<SGTUserListEntity>> consumer) {
        submitRequestThrowError(ISGTImpl.getSGTHomeData(pi, 10).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    //获取入棚记录列表
    public void getSGTRpRecoudData(Consumer<SGTRpRecordEntity> consumer) {
        submitRequestThrowError(ISGTImpl.getSGTRpRecoudData(guid, pi2, 10).map(r -> {
            if (r.status) {
                return r.data;
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    private String TAG = "xiaohl";

    //获取赛鸽通用户列表
    public void getSGTGzListData(Consumer<List<SGTGzListEntity>> consumer) {
        Log.d(TAG, "getSGTGzListData: " + tID);
        submitRequestThrowError(ISGTImpl.getSGTGzListData(guid, tID, pi3, 10).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    //公棚赛鸽搜索（搜索足环或鸽主姓名）
    public void getSGTSearchFootListData(Consumer<List<SGTFootSearchEntity>> consumer) {
        submitRequestThrowError(ISGTImpl.getSGTSearchFootListData(guid, keyWord).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }




    public Consumer<String> setKeyWord(){
        return s -> {
            keyWord = s;
        };
    }
}
