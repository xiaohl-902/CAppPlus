package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;
import com.cpigeon.app.modular.usercenter.presenter.FeedBackResultListPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IFeedBackResultListView;
import com.cpigeon.app.modular.usercenter.view.adapter.FeedBackResultListAdapter;

public class FeedBackResultListActivity extends BasePageTurnActivity<FeedBackResultListPresenter, FeedBackResultListAdapter, FeedBackResult> implements IFeedBackResultListView {
    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            FeedBackResult feedBackResult = (FeedBackResult) adapter.getData().get(position);
            //if (feedBackResult.getState().equals("已回复")) {
            Intent intent = new Intent(FeedBackResultListActivity.this, FeedBackResultDetialActivity.class);
            intent.putExtra(FeedBackResultDetialActivity.INTENT_KEY_FEEDBACKRESULT, feedBackResult);
            startActivity(intent);
            //}
        }
    };

    @Override
    public FeedBackResultListPresenter initPresenter() {
        return new FeedBackResultListPresenter(this);
    }

    @NonNull
    @Override
    public String getTitleName() {
        return "反馈结果";
    }

    @Override
    public int getDefaultPageSize() {
        return 10;
    }

    @Override
    protected String getEmptyDataTips() {
        return "没有数据";
    }

    @Override
    public FeedBackResultListAdapter getNewAdapterWithNoData() {
        FeedBackResultListAdapter adapter = new FeedBackResultListAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadMoreFeedbackResult();
    }
}
