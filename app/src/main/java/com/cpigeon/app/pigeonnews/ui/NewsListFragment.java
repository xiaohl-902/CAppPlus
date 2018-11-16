package com.cpigeon.app.pigeonnews.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.HomeNewsEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.home.adpter.PigeonNewsAdapter;
import com.cpigeon.app.pigeonnews.presenter.NewsListPre;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

/**
 * Created by Zhu TingYu on 2018/1/6.
 */

public class NewsListFragment extends BaseMVPFragment<NewsListPre> {

    RecyclerView recyclerView;
    PigeonNewsAdapter adapter;

    @Override
    protected NewsListPre initPresenter() {
        return new NewsListPre(getActivity());
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
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setPadding(0, 0, 0, 0);
        adapter = new PigeonNewsAdapter();
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            IntentBuilder.Builder(getActivity(), NewsDetailsActivity.class).startActivity();
        });
        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.newsList(data -> {
                if(data.isEmpty()){
                    adapter.loadMoreEnd();
                    adapter.setLoadMore(true);
                }else {
                    adapter.addData(HomeNewsEntity.get(data, HomeNewsEntity.TYPE_ALL));
                    adapter.setLoadMore(false);

                }
            });
        }, recyclerView);
        adapter.setOnNotNetClickListener(v -> {
            bindData();
        });
        recyclerView.setAdapter(adapter);

        setRefreshListener(() -> {
            bindData();
        });

        bindData();
    }

    private void bindData() {
        showLoading();
        mPresenter.newsList(data -> {
            hideLoading();
            adapter.setNewData(HomeNewsEntity.get(data, HomeNewsEntity.TYPE_ALL));
        });
    }


}
