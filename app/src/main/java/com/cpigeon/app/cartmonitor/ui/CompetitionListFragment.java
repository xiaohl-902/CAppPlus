package com.cpigeon.app.cartmonitor.ui;

import android.os.Bundle;

import com.cpigeon.app.R;
import com.cpigeon.app.cartmonitor.presenter.CompetitionListPre;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;

/**
 * Created by chenshuai on 2017/11/7.
 */

public class CompetitionListFragment extends BaseMVPFragment<CompetitionListPre> implements CompetitionListView {

    public static final int TYPE_ASSOCIATION = 0;
    public static final int TYPE_BOB = 1;

    public static final String TYPE = "type";

    @Override
    protected CompetitionListPre initPresenter() {
        return new CompetitionListPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_com_swiperefreshlayout_recyclerview_has_searchedittext;
    }
}
