package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.adpter.CircleNewMessageAdapter;
import com.cpigeon.app.circle.presenter.CircleNewMessagePre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;

/**
 * Created by Zhu TingYu on 2018/5/31.
 */

public class CircleNewMessageFragment extends BaseMVPFragment<CircleNewMessagePre>{

    RecyclerView recyclerView;
    CircleNewMessageAdapter adapter;

    @Override
    protected CircleNewMessagePre initPresenter() {
        return new CircleNewMessagePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("消息");
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CircleNewMessageAdapter();

        recyclerView.setAdapter(adapter);
        adapter.setNewData(mPresenter.getCircleEntities());
    }
}
