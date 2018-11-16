package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.adpter.ShieldDynamicAdapter;
import com.cpigeon.app.circle.presenter.HideMessageListPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShieldDynamicFragment extends BaseMVPFragment <HideMessageListPre>{

    RecyclerView recyclerView;
    ShieldDynamicAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_layout;
    }

    @Override
    protected HideMessageListPre initPresenter() {
        return new HideMessageListPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ShieldDynamicAdapter(mPresenter);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.getHideMessage(data -> {
                if(data.isEmpty()){
                    adapter.setLoadMore(true);
                }else {
                    adapter.setLoadMore(false);
                    adapter.addData(data);
                }
            });
        },recyclerView);

        setRefreshListener(() -> {
            mPresenter.page = 1;
            mPresenter.getHideMessage(data -> {
                adapter.setNewData(data);
                hideLoading();
            });
        });

        mPresenter.getHideMessage(list -> {
            adapter.setNewData(list);
        });
    }
}
