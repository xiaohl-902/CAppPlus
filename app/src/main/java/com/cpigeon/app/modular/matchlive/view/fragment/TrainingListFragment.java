package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.TrainingDataEntity;
import com.cpigeon.app.modular.matchlive.view.adapter.TrainingListAdapter;
import com.cpigeon.app.utils.IntentBuilder;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class TrainingListFragment extends BaseMVPFragment {

    RecyclerView recyclerView;
    TrainingListAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        List<HistoryGradeInfo> data = getArguments().getParcelableArrayList(IntentBuilder.KEY_DATA);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingListAdapter();
        adapter.bindToRecyclerView(recyclerView);

        adapter.setNewData(data);

    }
}
