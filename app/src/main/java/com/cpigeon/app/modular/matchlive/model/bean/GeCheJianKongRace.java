package com.cpigeon.app.modular.matchlive.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by chenshuai on 2017/7/11.
 * 鸽车监控比赛信息
 */

public class GeCheJianKongRace implements Serializable{

    /**
     * stateCode : 2
     * createTime : 2017-07-03 09:22:14
     * raceName : 中鸽网测试
     * id : 152
     * mEndTime : 2017-07-03 10:43:02
     * state : 监控结束
     * muid : 5473
     * flyingTime :
     * longitude : 0
     * mTime : 2017-07-03 09:22:19
     * raceImage :
     * latitude : 0
     * flyingArea :
     */

    private int stateCode;
    private String createTime;
    private String raceName;
    private int id;
    private String mEndTime;
    private String state;
    private int muid;
    private String flyingTime;
    private double longitude;
    private String mTime;
    private String raceImage;
    private double latitude;
    private String flyingArea;
    private int position;
    private int parentsPosition;

    public int getParentsPosition() {
        return parentsPosition;
    }

    public void setParentsPosition(int parentsPosition) {
        this.parentsPosition = parentsPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMEndTime() {
        return mEndTime;
    }

    public void setMEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getMuid() {
        return muid;
    }

    public void setMuid(int muid) {
        this.muid = muid;
    }

    public String getFlyingTime() {
        return flyingTime;
    }

    public void setFlyingTime(String flyingTime) {
        this.flyingTime = flyingTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMTime() {
        return mTime;
    }

    public void setMTime(String mTime) {
        this.mTime = mTime;
    }

    public String getRaceImage() {
        return raceImage;
    }

    public void setRaceImage(String raceImage) {
        this.raceImage = raceImage;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getFlyingArea() {
        return flyingArea;
    }

    public void setFlyingArea(String flyingArea) {
        this.flyingArea = flyingArea;
    }

    @Override
    public String toString() {
        return "GeCheJianKongRace{" +
                "stateCode=" + stateCode +
                ", createTime='" + createTime + '\'' +
                ", raceName='" + raceName + '\'' +
                ", id=" + id +
                ", mEndTime='" + mEndTime + '\'' +
                ", state='" + state + '\'' +
                ", muid=" + muid +
                ", flyingTime='" + flyingTime + '\'' +
                ", longitude=" + longitude +
                ", mTime='" + mTime + '\'' +
                ", raceImage='" + raceImage + '\'' +
                ", latitude=" + latitude +
                ", flyingArea='" + flyingArea + '\'' +
                ", position=" + position +
                ", parentsPosition=" + parentsPosition +
                '}';
    }
}

