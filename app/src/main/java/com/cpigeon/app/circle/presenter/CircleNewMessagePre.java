package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.JPushCircleEntity;
import com.cpigeon.app.utils.IntentBuilder;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/31.
 */

public class CircleNewMessagePre extends BasePresenter {

    private List<JPushCircleEntity> circleEntities;

    public CircleNewMessagePre(Activity activity) {
        super(activity);
        circleEntities = activity.getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public List<JPushCircleEntity> getCircleEntities() {
        return circleEntities;
    }
}
