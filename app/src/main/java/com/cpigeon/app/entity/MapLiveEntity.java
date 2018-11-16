package com.cpigeon.app.entity;

import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/3/6.
 */

public class MapLiveEntity {
    public double sfdwd;//司放地纬度
    public String kongju;//空距
    public String tianqi;
    public String sfd;//司放地
    public double sfdjd;//司放地经度
    public long jksc;//long类型，监控时长，单位：秒
    public List<GYTRaceLocation> localinfolist;
}
