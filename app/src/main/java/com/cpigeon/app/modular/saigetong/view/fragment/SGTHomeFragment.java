package com.cpigeon.app.modular.saigetong.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.app.modular.saigetong.view.adapter.SGTUserListAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTHomeFragment extends BaseMVPFragment<SGTPresenter> {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    //    @BindView(R.id.empty_layout)
//    CustomEmptyView mCustomEmptyView;
//    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefreshing = false;

    private SGTUserListAdapter mAdapter;//适配器

    @Override
    protected SGTPresenter initPresenter() {
        return new SGTPresenter(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("参赛鸽影像");
//        toolbar.getMenu().clear();
//        toolbar.getMenu().add("").setIcon(R.mipmap.sgt_sousuo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                ToastUtil.showLongToast(getContext(), "搜索");
//                return false;
//            }
//        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mAdapter = new SGTUserListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecorationLine(mRecyclerView);
        mAdapter.bindToRecyclerView(mRecyclerView);
        showLoading();
        mPresenter.getSGTHomeData(data -> {
            hideLoading();
            mAdapter.setNewData(data);
        });

        //下拉刷新
        setRefreshListener(() -> {
            mPresenter.pi = 1;
            mPresenter.getSGTHomeData(data -> {
                mAdapter.setNewData(data);
                hideLoading();
            });
        });

        //设置加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.pi++;
            mPresenter.getSGTHomeData(data -> {
                if (data.isEmpty()) {
                    mAdapter.setLoadMore(true);
                } else {
                    mAdapter.setLoadMore(false);
                    mAdapter.addData(data);
                }
            });
        }, mRecyclerView);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_layout;
    }


}
