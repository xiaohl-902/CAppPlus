package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.commonstandard.view.IRefreshBoomMenu;
import com.cpigeon.app.commonstandard.view.activity.IPageTurn;
import com.cpigeon.app.commonstandard.view.activity.IRefresh;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface IReportData extends IView, IPageTurn<MultiItemEntity>, IRefresh, IRefreshBoomMenu {
    String getMatchType();

    String getSsid();

    String getFoot();

    String getName();

    boolean hascz();

    int czIndex();

    String sKey();

    MatchInfo getMatchInfo();

    void showDefaultGCJKInfo(GeCheJianKongRace geCheJianKongRace);
}
