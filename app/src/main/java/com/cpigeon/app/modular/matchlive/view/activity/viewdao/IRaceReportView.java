package com.cpigeon.app.modular.matchlive.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.IRefreshBoomMenu;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.Collection;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface IRaceReportView extends IView, IRefreshBoomMenu {

    MatchInfo getMatchInfo();

    String getSsid();

    String getLx();

    void showBulletin(Bulletin bulletin);

    void showDefaultGCJKInfo(GeCheJianKongRace geCheJianKongRace);
}
