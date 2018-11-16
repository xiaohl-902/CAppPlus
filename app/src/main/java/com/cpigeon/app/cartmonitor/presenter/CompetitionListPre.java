package com.cpigeon.app.cartmonitor.presenter;

import com.cpigeon.app.cartmonitor.model.CompetitionListDao;
import com.cpigeon.app.cartmonitor.model.CompetitionListDaoImpl;
import com.cpigeon.app.cartmonitor.ui.CompetitionListView;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;

/**
 * Created by chenshuai on 2017/11/7.
 */

public class CompetitionListPre extends BasePresenter<CompetitionListView, CompetitionListDao> {

    public CompetitionListPre(CompetitionListView mView) {
        super(mView);
    }

    @Override
    protected CompetitionListDao initDao() {
        return new CompetitionListDaoImpl();
    }
}
