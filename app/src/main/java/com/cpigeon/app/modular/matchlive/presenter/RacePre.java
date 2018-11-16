package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.dao.IGeCheJianKongDao;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IRaceReportImpl;
import com.cpigeon.app.modular.matchlive.model.daoimpl.RaceDaoImpl;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RacePre extends BasePresenter<IReportData, IRaceDao> {
    IGeCheJianKongDao iGeCheJianKongDao = new IRaceReportImpl();

    public RacePre(IReportData mView) {
        super(mView);
    }

    @Override
    protected IRaceDao initDao() {
        return new RaceDaoImpl();
    }

    public void loadRaceData(final int loadType, int px) {
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();
        mDao.showReprotData(px, mView.getMatchType(), mView.getSsid(), mView.getFoot(), mView.getName(), mView.hascz(), mView.getPageIndex(), mView.getPageSize(), mView.czIndex(), mView.sKey(), new IBaseDao.OnCompleteListener<List>() {
            @Override
            public void onSuccess(final List data) {
                if (isAttached()) {
                    if (loadType == 0) {
                        final List d = isDetached() ? null : "xh".equals(mView.getMatchType()) ? RaceReportAdapter.getXH(data) : RaceReportAdapter.getGP(data);
                        postDelayed(new CheckAttachRunnable() {
                            @Override
                            protected void runAttached() {
                                if (mView.isMoreDataLoading()) {
                                    mView.loadMoreComplete();
                                } else {
                                    mView.hideRefreshLoading();
                                }
                                mView.showMoreData(d);
                            }
                        }, 300);

                    } else {
                        final List d = isDetached() ? null : RaceXunFangAdapter.getGP(data);
                        postDelayed(new CheckAttachRunnable() {
                            @Override
                            protected void runAttached() {
                                if (mView.isMoreDataLoading()) {
                                    mView.loadMoreComplete();
                                } else {
                                    mView.hideRefreshLoading();
                                }
                                mView.showMoreData(d);
                            }
                        }, 300);
                    }

                }
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreFail();
                        } else {
                            mView.hideRefreshLoading();
                            mView.showTips("获取报到记录失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }
        });
    }

    public void addRaceFollow() {
//        Collection collection = new Collection();
//        collection.setCollUserid(CpigeonData.getInstance().getUserId(MyApp.getInstance()));
//        collection.setType(Collection.CollectionType.RACE.getValue());
//        collection.setCollTime(System.currentTimeMillis());
//        collection.setKey(mView.getMatchInfo().getSsid() + "");
//        collection.setContent(mView.getMatchInfo().getMc());
//        addFollow(collection);
        mView.showTips("正在使劲处理...", IView.TipType.LoadingShow);
        mDao.addUserRaceFollow(mView.getSsid(), "race", mView.getMatchInfo().computerBSMC(), new IBaseDao.OnCompleteListener<UserFollow>() {
            @Override
            public void onSuccess(final UserFollow data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        if (data != null) {
                            mView.showTips("关注成功", IView.TipType.DialogSuccess);
                            mView.refreshBoomMnue();
                        } else {
                            mView.showTips("关注失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips("关注失败", IView.TipType.View);
                    }
                }, 300);
            }
        });

    }

    public void addRaceOrgFollow() {
//        Collection collection = new Collection();
//        collection.setCollUserid(CpigeonData.getInstance().getUserId(MyApp.getInstance()));
//        collection.setType(Collection.CollectionType.ORG.getValue());
//        collection.setCollTime(System.currentTimeMillis());
//        collection.setKey(mView.getMatchInfo().getZzid() + "");
//        collection.setContent(mView.getMatchInfo().getMc());
//        addFollow(collection);
        mView.showTips("正在使劲处理...", IView.TipType.LoadingShow);
        mDao.addUserRaceFollow(mView.getSsid(), mView.getMatchInfo().getLx().equals("xh") ? "xiehui" : "gongpeng", mView.getMatchInfo().getMc(), new IBaseDao.OnCompleteListener<UserFollow>() {
            @Override
            public void onSuccess(final UserFollow data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        if (data != null) {
                            mView.showTips("关注成功", IView.TipType.DialogSuccess);
                            mView.refreshBoomMnue();
                        } else {
                            mView.showTips("关注失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips("关注失败", IView.TipType.View);
                    }
                }, 300);
            }
        });
    }

    public void removeFollow(UserFollow userFollow) {

        mView.showTips("正在使劲处理...", IView.TipType.LoadingShow);
        mDao.removeUserRaceFollow(userFollow.getFid(), userFollow.getRela(), userFollow.getFtype(), new IBaseDao.OnCompleteListener<Integer>() {
            @Override
            public void onSuccess(final Integer data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        if (data != null && data > 0) {
                            mView.showTips("取消关注成功", IView.TipType.DialogSuccess);
                            mView.refreshBoomMnue();
                        } else {
                            mView.showTips("取消关注失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips("取消关注失败", IView.TipType.View);
                    }
                }, 300);
            }
        });
    }

    public void getDefaultGCJKInfo() {
        try {
            iGeCheJianKongDao.getDefaultGYTRaceInfo(mView.getMatchInfo().getSsid(), mView.getMatchInfo().computerBSMC(), "xh".equals(mView.getMatchInfo().getLx()) ? "2" : "1", new IBaseDao.OnCompleteListener<GeCheJianKongRace>() {
                @Override
                public void onSuccess(GeCheJianKongRace data) {
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        public void runAttached() {
                            mView.showDefaultGCJKInfo(data);
                        }
                    }, 100);
                }

                @Override
                public void onFail(String msg) {
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        public void runAttached() {
                            mView.showDefaultGCJKInfo(null);
                        }
                    }, 100);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
