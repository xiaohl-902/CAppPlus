package com.cpigeon.app.circle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.adpter.ShieldFriendAdapter;
import com.cpigeon.app.circle.presenter.HideFriendPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShieldFriendFragment extends BaseMVPFragment<HideFriendPre>{

    public static final String TYPE_SHIELD = "TYPE_SHIELD";
    public static final String TYPE_BLACKLIST = "TYPE_BLACKLIST";

    String type;

    ShieldFriendAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_layout;
    }

    @Override
    protected HideFriendPre initPresenter() {
        return new HideFriendPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        type = getArguments().getString(IntentBuilder.KEY_TYPE);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShieldFriendAdapter(type, mPresenter);
        recyclerView.setAdapter(adapter);

        if(type.equals(TYPE_SHIELD)){
            mPresenter.getHideList(list -> {
                adapter.setNewData(list);
            });

            setRefreshListener(() -> {
                mPresenter.page = 1;
                mPresenter.getHideList(data -> {
                    adapter.setNewData(data);
                    hideLoading();
                });
            });

            adapter.setOnLoadMoreListener(() -> {
                mPresenter.page++;
                mPresenter.getHideList(data -> {
                    if(data.isEmpty()){
                        adapter.setLoadMore(true);
                    }else {
                        adapter.setLoadMore(false);
                        adapter.addData(data);
                    }
                });
            },recyclerView);
        }else {
            adapter.setOnLoadMoreListener(() -> {
                mPresenter.page++;
                mPresenter.getBlackList(data -> {
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
                mPresenter.getBlackList(data -> {
                    adapter.setNewData(data);
                    hideLoading();
                });
            });

            mPresenter.getBlackList(list -> {
                adapter.setNewData(list);
            });
        }

    }
}
