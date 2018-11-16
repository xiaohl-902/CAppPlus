package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.modular.usercenter.model.dao.IUserFollowDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserFollowDaoImpl;
import com.cpigeon.app.modular.usercenter.view.fragment.viewdao.IUserFollowView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import static com.cpigeon.app.utils.CpigeonConfig.getDataDb;

/**
 * Created by chenshuai on 2017/6/28.
 */

public class UserFollowPresenter extends BasePresenter<IUserFollowView, IUserFollowDao> {
    public UserFollowPresenter(IUserFollowView mView) {
        super(mView);
    }

    @Override
    protected IUserFollowDao initDao() {
        return new UserFollowDaoImpl();
    }

    /**
     * 加载用户关注
     */
    public void loadUserFollowNext() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();
        mDao.getUserRaceFollows(mView.getPageIndex(), mView.getPageSize(), mView.getFollowType(), new IBaseDao.OnCompleteListener<List<UserFollow>>() {
            @Override
            public void onSuccess(final List<UserFollow> data) {
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
                            mView.showTips("加载失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }
        });
    }

    /**
     * 移除用户关注
     *
     * @param listViewPosition
     * @param followId
     */
    public void removeUserFollow(final int listViewPosition, int followId) {
        mView.showTips("正在使劲处理...", IView.TipType.LoadingShow);
        mDao.removeUserRaceFollow(followId, "", "", new IBaseDao.OnCompleteListener<Integer>() {
            @Override
            public void onSuccess(final Integer data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips("取消成功", IView.TipType.DialogSuccess);
                        mView.removeItem(listViewPosition);
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips("取消失败", IView.TipType.DialogError);
                    }
                }, 300);
            }
        });
    }

    public void startMatchLiveView(UserFollow userFollow) {
        MatchInfo matchInfo = selectMatchInfosForDbBySSID(userFollow.getRela());
        mView.entryMatchDetialView(matchInfo);
    }


    private MatchInfo selectMatchInfosForDbBySSID(String ssid) {
        DbManager db = x.getDb(getDataDb());
        MatchInfo matchInfo = null;
        try {
            matchInfo = db.selector(MatchInfo.class).where("ssid", "=", ssid).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return matchInfo;
    }

    private MatchInfo selectMatchInfosForDbByOrg(String orgDm) {
        DbManager db = x.getDb(getDataDb());
        MatchInfo matchInfo = null;
        try {
            matchInfo = db.selector(MatchInfo.class).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return matchInfo;
    }
}
