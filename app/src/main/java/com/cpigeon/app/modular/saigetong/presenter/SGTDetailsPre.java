package com.cpigeon.app.modular.saigetong.presenter;

import android.app.Activity;
import android.util.Log;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.SGTDetailsInfoEntity;
import com.cpigeon.app.modular.saigetong.model.bead.SGTImgEntity;
import com.cpigeon.app.modular.saigetong.model.daoimpl.ISGTImpl;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/23.
 */

public class SGTDetailsPre extends BasePresenter {

    public String foodId;
    String guid;
    public String id;

    public SGTDetailsPre(Activity activity) {
        super(activity);
        foodId = activity.getIntent().getStringExtra(IntentBuilder.KEY_TITLE);
        guid = activity.getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        id = activity.getIntent().getStringExtra(IntentBuilder.KEY_DATA);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取足环信息（含照片）
    public void getFootInfoData(Consumer<SGTDetailsInfoEntity> consumer) {
        submitRequestThrowError(ISGTImpl.getFootInfoData(id, guid).map(r -> {
            if(r.status){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }
}
