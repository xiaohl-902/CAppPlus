package com.cpigeon.app.commonstandard.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.customview.CustomEmptyView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/4/15.
 * <p>
 * 可刷新，可翻页的Activity
 */

public abstract class BasePageTurnActivity<Pre extends BasePresenter, Adapter extends BaseQuickAdapter<DataBean, BaseViewHolder>, DataBean>
        extends BaseActivity<Pre>
        implements IPageTurn<DataBean>, IRefresh, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    TextView title;

    boolean canLoadMore = true, isRefreshing = false, isMoreDateLoading = false;
    int pageindex = 1, pagesize = 10;

    @Override
    public boolean isMoreDataLoading() {
        return isMoreDateLoading || pageindex > 0;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_com_toolbar_swiperefreshlayout_recyclerview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle(getTitleName());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(v -> finish());

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pagesize = getDefaultPageSize();
        if (isNetworkConnected()) onRefresh();
        else {
            mSwipeRefreshLayout.setEnabled(true);
            showTips("网络连接已断开", TipType.View);
        }
    }

    @Override
    public void showRefreshLoading() {
        if (isNetworkConnected()) {
            if (mSwipeRefreshLayout == null || mSwipeRefreshLayout.isRefreshing()) return;
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            //onNetworkDisConnected();
            showTips("网络连接已断开", TipType.View);
        }
    }

    @Override
    public void hideRefreshLoading() {
        isRefreshing = false;
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void showEmptyData() {
        showTips(getEmptyDataTips(), TipType.View);
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            if(isNetworkConnected()){
                mCustomEmptyView.isHaveNet();
                mCustomEmptyView.setEmptyImage(R.drawable.ic_empty_img);
                mCustomEmptyView.setEmptyText(tip);
            }else {
                mCustomEmptyView.isNotHaveNet();
                mCustomEmptyView.setEmptyImage(R.drawable.ic_net_error);
                mCustomEmptyView.setButtomOnClickListener(v -> {
                    onRefresh();
                });
            }

            return true;
        }
        switch (tipType) {
            case SnackbarShort:
                final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), tip, Snackbar.LENGTH_SHORT);
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
                final Snackbar snackbar1 = Snackbar.make(getWindow().getDecorView(), tip, Snackbar.LENGTH_LONG);
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
    public int getPageIndex() {
        return pageindex;
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
        if(mRecyclerView != null){
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.setEnableLoadMore(true);
        pageindex = 1;
    }

    @Override
    public void showMoreData(List<DataBean> dataBeen) {
        hideRefreshLoading();
        if (getPageIndex() == 1 && (dataBeen == null || dataBeen.size() == 0)) {
            showEmptyData();
        } else {
            if (mCustomEmptyView != null)
                mCustomEmptyView.setVisibility(View.GONE);
        }
        if (dataBeen != null)
            mAdapter.addData(dataBeen);

        canLoadMore = dataBeen != null && dataBeen.size() == getPageSize();
        Logger.d("canLoadMore=" + canLoadMore);
        if (canLoadMore) {
            pageindex++;
        } else {
            mAdapter.loadMoreEnd(false);
        }
//        mAdapter.setEnableLoadMore(canLoadMore);
        isMoreDateLoading = isRefreshing = false;
    }

    @Override
    public boolean canLoadMoreData() {
        return canLoadMore;
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
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRefresh() {
        iniPageAndAdapter();
        isRefreshing = true;
        loadDataByPresenter();
    }


    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            isMoreDateLoading = true;
            loadDataByPresenter();
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    /**
     * 获取Activity Title
     *
     * @return
     */
    @NonNull
    public abstract String getTitleName();

    /**
     * 获取默认的页面大小
     *
     * @return
     */
    public abstract int getDefaultPageSize();

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
}
