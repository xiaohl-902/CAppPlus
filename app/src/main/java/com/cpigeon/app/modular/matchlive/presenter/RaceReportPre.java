package com.cpigeon.app.modular.matchlive.presenter;

import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.MatchModel;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.dao.IGeCheJianKongDao;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReport;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IRaceReportImpl;
import com.cpigeon.app.modular.matchlive.view.activity.viewdao.IRaceReportView;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RaceReportPre extends BasePresenter<IRaceReportView, IRaceReport> {

    IGeCheJianKongDao iGeCheJianKongDao = new IRaceReportImpl();

    public int userId;
    public String matchId;
    public LatLng backLocation;

    public RaceReportPre(IRaceReportView mView) {
        super(mView);
    }


    public void showBulletin() {
        if (isAttached()) {
            mDao.updateBulletin(mView.getLx(), mView.getSsid(), new IBaseDao.OnCompleteListener<List<Bulletin>>() {
                @Override
                public void onSuccess(List<Bulletin> data) {
                    mDao.queryBulletin(mView.getSsid(), new IBaseDao.OnCompleteListener<Bulletin>() {
                        @Override
                        public void onSuccess(final Bulletin data) {
                            postDelayed(new CheckAttachRunnable() {
                                @Override
                                protected void runAttached() {
                                    mView.showBulletin(data);
                                }
                            }, 200);
                        }

                        @Override
                        public void onFail(String msg) {
                            postDelayed(new CheckAttachRunnable() {
                                @Override
                                protected void runAttached() {
                                    mView.showBulletin(null);
                                }
                            }, 200);
                        }
                    });
                }

                @Override
                public void onFail(String msg) {
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            mView.showBulletin(null);
                        }
                    }, 200);
                }
            });
        }
    }


    public void addRaceClickCount() {
        mDao.addRaceClickCount(mView.getLx(), mView.getSsid());
    }

    @Override
    protected IRaceReport initDao() {
        return new IRaceReportImpl();
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
        mDao.addUserRaceFollow(mView.getSsid(), mView.getLx().equals("xh") ? "xiehui" : "gongpeng", mView.getMatchInfo().getMc(), new IBaseDao.OnCompleteListener<UserFollow>() {
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
        iGeCheJianKongDao.getDefaultGYTRaceInfo(mView.getMatchInfo().getSsid(), mView.getMatchInfo().computerBSMC(), "xh".equals(mView.getLx()) ? "2" : "1", new IBaseDao.OnCompleteListener<GeCheJianKongRace>() {
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
    }

    public void getFirstByAssociationLocation(Consumer<Boolean> consumer){
        submitRequestThrowError(MatchModel.getFirstByAssociationLocation(userId, matchId).map(r -> {
            if(r.status){
                backLocation = r.data.get();
                return true;
            }else return false;
        }),consumer);
    }

    public void getFirstByLoftLocation(Consumer<Boolean> consumer){
        submitRequestThrowError(MatchModel.getFirstByLoftLocation(userId, matchId).map(r -> {
            if(r.status){
                backLocation = r.data.get();
                return true;
            }else return false;
        }),consumer);
    }

}
