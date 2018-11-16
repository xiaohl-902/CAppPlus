package com.cpigeon.app.commonstandard.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IPageTurn;
import com.cpigeon.app.commonstandard.view.activity.IRefresh;
import com.cpigeon.app.utils.customview.CustomEmptyView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/4/15.
 * 可翻页，可刷新的Fragment
 */

public abstract class BasePageTurnFragment<Pre extends BasePresenter, Adapter extends BaseQuickAdapter<DataBean, BaseViewHolder>, DataBean> extends BaseMVPFragment<Pre> implements IPageTurn<DataBean>, IRefresh, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    boolean canLoadMore = true, isRefreshing = false, isMoreDateLoading = false;
    int pageindex = 1, pagesize = 10;

    protected Adapter mAdapter;

    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pagesize = getDefaultPageSize();
        iniPageAndAdapter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_matchlive_sub;
    }

    @Override
    public void finishCreateView(Bundle state) {
        if (isNetworkConnected()) {
            loadData();
            showRefreshLoading();
            loadDataByPresenter();
        } else {
            try {
                loadData();
                loadDataByPresenter();
                if (mAdapter.getData().isEmpty()) {
                    mSwipeRefreshLayout.setEnabled(true);
                    showTips("网络连接已断开", TipType.View);
                }
            } catch (Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.GONE);
                showTips("网络连接已断开", TipType.View);
            }
        }
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        iniPageAndAdapter();
        loadDataByPresenter();
    }

    @Override
    public boolean isMoreDataLoading() {
        return isMoreDateLoading || pageindex > 0;
    }

    @Override
    public boolean isRefreshing() {
        return this.isRefreshing;
    }

    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setEnabled(false);
            }
            isMoreDateLoading = true;
            loadDataByPresenter();
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    @Override
    public void loadMoreComplete() {
        isMoreDateLoading = false;
        mAdapter.loadMoreComplete();//完成加载
        mAdapter.setEnableLoadMore(true);
        finishTask();
    }

    @Override
    public void loadMoreFail() {
        isMoreDateLoading = false;
        mAdapter.loadMoreFail();
        mAdapter.setEnableLoadMore(true);
        finishTask();
    }

    protected void finishTask() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mCustomEmptyView != null) {
            mCustomEmptyView.setVisibility(View.GONE);
        }
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if (mSwipeRefreshLayout != null && mCustomEmptyView != null
                    && mRecyclerView != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mCustomEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                if (isNetworkConnected()) {
                    mCustomEmptyView.isHaveNet();
                    mCustomEmptyView.setEmptyImage(R.drawable.ic_empty_img);
                    mCustomEmptyView.setEmptyText(tip);
                } else {
                    mCustomEmptyView.isNotHaveNet();
                    mCustomEmptyView.setEmptyImage(R.drawable.ic_net_error);
                    mCustomEmptyView.setButtomOnClickListener(v -> {
                        loadData();
                        showRefreshLoading();
                        loadDataByPresenter();
                    });
                }
            }
            return true;
        }
        switch (tipType) {
            case SnackbarShort:
                final Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), tip, Snackbar.LENGTH_SHORT);
                snackbar.setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        // 跳转到系统的网络设置界面
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
                return true;
            case SnackbarLong:
                final Snackbar snackbar1 = Snackbar.make(getActivity().getWindow().getDecorView(), tip, Snackbar.LENGTH_LONG);
                snackbar1.setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar1.dismiss();
                        // 跳转到系统的网络设置界面
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
                return true;
        }
        return super.showTips(tip, tipType);
    }

    @Override
    public void showRefreshLoading() {
        if (mSwipeRefreshLayout == null || mSwipeRefreshLayout.isRefreshing()) return;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        try {
            isRefreshing = false;
            if (mSwipeRefreshLayout != null) {
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showEmptyData() {
        showTips(getEmptyDataTips(), TipType.View);
    }


    @Override
    public int getPageIndex() {
        return pageindex;
    }


    public void setPageIndex(int pageindex) {
        this.pageindex = pageindex;
    }


    @Override
    public int getPageSize() {
        return pagesize;
    }

    @Override
    public void iniPageAndAdapter() {
        mAdapter = getNewAdapterWithNoData();
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        pageindex = 1;
    }

    /**
     * 获取默认的页面大小
     *
     * @return
     */
    protected abstract int getDefaultPageSize();

    /**
     * 获取空数据的提示文字
     *
     * @return
     */
    protected abstract String getEmptyDataTips();

    /**
     * 获取adapter
     * <p>return new Adapter(null);<p/>
     *
     * @return
     */
    public abstract Adapter getNewAdapterWithNoData();

    /**
     * 通过Presenter 加载数据
     */
    protected abstract void loadDataByPresenter();

    @Override
    public void showMoreData(List<DataBean> dataBeen) {
        try {
            hideRefreshLoading();
            if (getPageIndex() == 1 && (dataBeen == null || dataBeen.size() == 0)) {
                showEmptyData();
            } else {
                if (mCustomEmptyView != null) {
                    mCustomEmptyView.setVisibility(View.GONE);
                }
            }
            if (dataBeen != null)
                mAdapter.notifyDataSetChanged();
            mAdapter.addData(dataBeen);
            mAdapter.notifyDataSetChanged();
            canLoadMore = dataBeen != null && dataBeen.size() == getPageSize();
            Logger.d("canLoadMore=" + canLoadMore + ";Size=" + (dataBeen == null ? 0 : dataBeen.size()));
            if (canLoadMore) {
                pageindex++;
            } else {
                mAdapter.loadMoreEnd(false);
            }
//        mAdapter.setEnableLoadMore(canLoadMore);
            isMoreDateLoading = isRefreshing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canLoadMoreData() {
        return canLoadMore;
    }
}
