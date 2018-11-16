package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;
import com.cpigeon.app.modular.usercenter.model.dao.IFeedBackDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.FeedBackDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IFeedBackResultListView;

import java.util.List;

/**
 * Created by chenshuai on 2017/6/22.
 */

public class FeedBackResultListPresenter extends BasePresenter<IFeedBackResultListView, IFeedBackDao> {
    public FeedBackResultListPresenter(IFeedBackResultListView mView) {
        super(mView);
    }

    @Override
    protected IFeedBackDao initDao() {
        return new FeedBackDaoImpl();
    }

    public void loadMoreFeedbackResult() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1)
            mView.showRefreshLoading();
        mDao.getFeedbacks(mView.getPageIndex(), mView.getPageSize(), new IBaseDao.OnCompleteListener<List<FeedBackResult>>() {
            @Override
            public void onSuccess(final List<FeedBackResult> data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreComplete();
                        } else {
                            mView.hideRefreshLoading();
                        }
                        mView.showMoreData(data);
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreFail();
                        } else {
                            mView.hideRefreshLoading();
                            mView.showTips("反馈记录加载失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }
        });
    }
}
