package com.cpigeon.app.message.ui.order.ui.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.message.ui.order.ui.OrderModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class VoiceListPre extends BasePresenter {
    int userId;
    public VoiceListPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getVoiceList(Consumer<List<VoiceEntity>> consumer){
        submitRequestThrowError(OrderModel.getVoiceList(userId).map(r -> {
            if(r.status){
                return r.data;
            }else return Lists.newArrayList();
        }),consumer);
    }

}
