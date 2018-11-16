package com.cpigeon.app.modular.usercenter.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.message.ui.home.UserGXTModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/12/14.
 */

public class UserCenterPre extends BasePresenter {
    int userId;
    public UserCenterPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }


}
