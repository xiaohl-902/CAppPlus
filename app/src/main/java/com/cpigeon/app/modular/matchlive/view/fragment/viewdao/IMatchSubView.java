package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IRefresh;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface IMatchSubView  extends IView,IRefresh{
//    void showGPData(List<MatchInfo> matchInfoList);
//    void showXHData(List<MatchInfo> matchInfoList);
    void showData(List<MatchInfo> matchInfoList);
    void setLoadType(int type);

    boolean hasDataList();
}
