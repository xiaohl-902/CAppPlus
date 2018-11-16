package com.cpigeon.app.modular.footsearch.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.FootInfoEntity;
import com.cpigeon.app.modular.footsearch.ui.adapter.FootSearchResultAdapter;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/22.
 */

public class FootSearchResultFragment extends BaseMVPFragment{

    RecyclerView recyclerView;
    FootSearchResultAdapter adapter;

    List<FootInfoEntity> data;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {

        data = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);

        setTitle("足环查询结果");

        toolbar.getMenu().clear();
        toolbar.getMenu().add("帮助")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), FootSearchHelpFragment.class);
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FootSearchResultAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNewData(data);

    }
}
