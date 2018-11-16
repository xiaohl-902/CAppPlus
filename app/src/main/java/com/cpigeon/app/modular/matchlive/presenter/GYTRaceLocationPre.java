package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;
import com.cpigeon.app.modular.matchlive.model.dao.IGYTRaceLocation;
import com.cpigeon.app.modular.matchlive.model.daoimpl.GYTRaceLocationImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IMapLiveView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GYTRaceLocationPre extends BasePresenter<IMapLiveView, IGYTRaceLocation> {

    public boolean isFirst = true;

    public GYTRaceLocationPre(IMapLiveView mView) {
        super(mView);
    }

    @Override
    protected IGYTRaceLocation initDao() {
        return new GYTRaceLocationImpl();
    }

    public void loadGYTRaceLocation() {
        if (isAttached()) {
            if (isFirst){
                mView.showTips("正在拼命加载...", IView.TipType.LoadingShow);
            }
            mDao.loadRaceLocation(mView.getRid(), mView.getLid(), mView.hw(), new IBaseDao.OnCompleteListener<List<GYTRaceLocation>>() {
                @Override
                public void onSuccess(final List<GYTRaceLocation> data) {
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            mView.showTips("",IView.TipType.LoadingHide);
                            mView.showMapData(data);
                        }
                    }, 300);
                }

                @Override
                public void onFail(String msg) {
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            mView.showTips("获取报到记录失败", IView.TipType.DialogError);
                            mView.showTips("",IView.TipType.LoadingHide);
                        }
                    }, 300);
                }
            });
        }
    }
}
