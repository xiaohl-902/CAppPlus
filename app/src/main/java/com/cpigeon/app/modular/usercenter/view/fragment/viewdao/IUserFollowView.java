package com.cpigeon.app.modular.usercenter.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IPageTurnView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;

/**
 * Created by chenshuai on 2017/6/28.
 */

public interface IUserFollowView extends IPageTurnView<UserFollow> {
    String getFollowType();

    void removeItem(int position);

    void entryMatchDetialView(MatchInfo matchInfo);
}
