package com.cpigeon.app.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.home.adpter.HomeMessageAdapter;

/**
 * Created by Zhu TingYu on 2018/1/30.
 */

public class HomeMessageFragment extends BaseMVPFragment<HomeMessagePre> {

    RecyclerView recyclerView;
    HomeMessageAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected HomeMessagePre initPresenter() {
        return new HomeMessagePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("公告通知");

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeMessageAdapter();
        recyclerView.setAdapter(adapter);
        showLoading();
        mPresenter.getHomeMessage(list -> {
            hideLoading();
            adapter.setNewData(list);
        });
    }
}
