package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2018/5/21.
 */

public class UserInfoPre extends BasePresenter{

    public int userId;

    public UserInfoPre(Activity activity) {
        super(activity);
        userId = activity.getIntent().getIntExtra(IntentBuilder.KEY_DATA,0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }
}
