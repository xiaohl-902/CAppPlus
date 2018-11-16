package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface IMapLiveView extends IView {
    void showMapData(List<GYTRaceLocation> raceLocations);
    String getRid();
    String getLid();
    String hw();
}
