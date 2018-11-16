package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IGeCheJianKongOrgInfoDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.GeCheJianKongOrgInfoDaoImpl;
import com.cpigeon.app.modular.matchlive.view.adapter.GeCheJianKongExpandListAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IGeCheJianKongListView;

import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 */

public class GeCheJianKongPersenter extends BasePresenter<IGeCheJianKongListView, IGeCheJianKongOrgInfoDao> {
    public GeCheJianKongPersenter(IGeCheJianKongListView mView) {
        super(mView);
    }

    @Override
    protected IGeCheJianKongOrgInfoDao initDao() {
        return new GeCheJianKongOrgInfoDaoImpl();
    }

    /**
     * 加载
     */
    public void loadNext() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();

        mDao.getGYTRaceListGroupByOrg(mView.getSearchKey(), mView.getOrgType(), mView.getPageIndex(), mView.getPageSize(), new IBaseDao.OnCompleteListener<List<GeCheJianKongOrgInfo>>() {
            @Override
            public void onSuccess(final List<GeCheJianKongOrgInfo> data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreComplete();
                        } else {
                            mView.hideRefreshLoading();
                        }
                        mView.showMoreData(GeCheJianKongExpandListAdapter.get(data));
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
                            mView.showTips("加载失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }
        });
    }
}
