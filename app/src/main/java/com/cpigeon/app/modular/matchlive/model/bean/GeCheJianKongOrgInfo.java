package com.cpigeon.app.modular.matchlive.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.modular.matchlive.view.adapter.GeCheJianKongExpandListAdapter;

import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 * 鸽车监控组织信息
 */

public class GeCheJianKongOrgInfo {
    /**
     * races : [{"stateCode":2,"createTime":"2017-07-03 09:22:14","raceName":"中鸽网测试","id":152,"mEndTime":"2017-07-03 10:43:02","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-03 09:22:19","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-03 16:38:39","raceName":"中鸽网测试","id":173,"mEndTime":"2017-07-03 17:34:38","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-03 16:38:46","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-03 19:00:50","raceName":"测试","id":180,"mEndTime":"2017-07-04 10:23:36","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 10:16:17","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-03 19:00:51","raceName":"测试","id":181,"mEndTime":"2017-07-03 19:26:29","state":"监控结束","muid":5473,"flyingTime":"2017-07-03 19:26:29","longitude":0,"mTime":"2017-07-03 19:03:37","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-04 10:25:27","raceName":"中和了","id":184,"mEndTime":"2017-07-04 10:53:42","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 10:25:33","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-04 11:04:38","raceName":"宾利","id":189,"mEndTime":"2017-07-04 12:37:22","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 11:04:42","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-04 12:45:17","raceName":"打字机","id":191,"mEndTime":"2017-07-04 13:05:53","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 12:45:22","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-04 17:34:26","raceName":"你的","id":198,"mEndTime":"2017-07-04 17:36:38","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 17:34:58","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-04 18:19:17","raceName":"我们","id":199,"mEndTime":"2017-07-04 18:55:06","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-04 18:19:22","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-05 08:00:55","raceName":"测试","id":201,"mEndTime":"2017-07-05 08:41:03","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-05 08:01:03","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-05 18:04:19","raceName":"中鸽网测试","id":202,"mEndTime":"2017-07-05 18:06:16","state":"监控结束","muid":5473,"flyingTime":"2017-07-05 18:06:16","longitude":0,"mTime":"2017-07-05 18:04:54","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-05 18:06:43","raceName":"exdd","id":203,"mEndTime":"2017-07-06 09:01:38","state":"监控结束","muid":5473,"flyingTime":"","longitude":1,"mTime":"2017-07-05 18:06:55","raceImage":"","latitude":1,"flyingArea":"ddd"},{"stateCode":2,"createTime":"2017-07-06 10:41:44","raceName":"1111111","id":206,"mEndTime":"2017-07-06 18:11:43","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-06 10:41:50","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-06 18:47:53","raceName":"测试","id":209,"mEndTime":"2017-07-06 19:26:46","state":"监控结束","muid":5473,"flyingTime":"2017-07-06 19:26:46","longitude":0,"mTime":"2017-07-06 18:48:22","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-11 10:05:23","raceName":"中鸽网测试","id":222,"mEndTime":"2017-07-11 10:18:47","state":"监控结束","muid":5473,"flyingTime":"","longitude":103.581,"mTime":"2017-07-11 10:05:31","raceImage":"","latitude":35.5955,"flyingArea":"四川133"},{"stateCode":2,"createTime":"2017-07-11 10:22:13","raceName":"中鸽网测试","id":223,"mEndTime":"2017-07-11 10:23:00","state":"监控结束","muid":5473,"flyingTime":"","longitude":103.581,"mTime":"2017-07-11 10:22:33","raceImage":"","latitude":35.5955,"flyingArea":"四川省民间艺术馆"},{"stateCode":2,"createTime":"2017-07-11 10:23:18","raceName":"中鸽网测试","id":224,"mEndTime":"2017-07-11 11:11:37","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-11 10:23:22","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":2,"createTime":"2017-07-11 11:11:55","raceName":"中鸽网测试","id":228,"mEndTime":"2017-07-11 11:44:10","state":"监控结束","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-11 11:11:59","raceImage":"","latitude":0,"flyingArea":""},{"stateCode":1,"createTime":"2017-07-11 11:44:29","raceName":"中鸽网测试","id":229,"mEndTime":"","state":"监控中","muid":5473,"flyingTime":"","longitude":0,"mTime":"2017-07-11 11:44:34","raceImage":"","latitude":0,"flyingArea":""}]
     * status : 正常
     * orgType : 协会
     * orgName : 12356456
     */

    public static final int STATE_NOT_MONITOR = 0;
    public static final int STATE_MONITORING = 1;
    public static final int STATE_END_OF_MONITOR = 2;

    private String status;
    private String orgType;
    private String orgName;
    public int weikaiqi;
    private List<GeCheJianKongRace> races;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<GeCheJianKongRace> getRaces() {
        return races;
    }

    public void setRaces(List<GeCheJianKongRace> races) {
        this.races = races;
    }

    public boolean isHaveMonitoring() {
        for (GeCheJianKongRace g : races) {
            if (g.getStateCode() == STATE_MONITORING){
                return true;
            }
        }
        return false;
    }

    public int getMonitoringCount() {
        int i = 0;
        for (GeCheJianKongRace g : races) {
            if (g.getStateCode() == STATE_MONITORING){
                i++;
            }
        }
        return i;
    }

    public int getNotMonitorCount() {
        int i = 0;
        for (GeCheJianKongRace g : races) {
            if (g.getStateCode() == STATE_NOT_MONITOR){
                i++;
            }
        }
        return i;
    }

    public int getEndMonitorCount() {
        int i = 0;
        for (GeCheJianKongRace g : races) {
            if (g.getStateCode() == STATE_END_OF_MONITOR){
                i++;
            }
        }
        return i;
    }



}
