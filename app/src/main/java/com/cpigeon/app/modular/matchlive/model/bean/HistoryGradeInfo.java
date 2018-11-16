package com.cpigeon.app.modular.matchlive.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by Zhu TingYu on 2018/5/4.
 */

public  class  HistoryGradeInfo implements Parcelable {
    private String bssj;
    private String xmmc;
    private String bsmc;
    private String bsgm;
    private String foot;
    private String bskj;
    private String speed;
    private String firstspeed;

    public HistoryGradeInfo(){}

    public String getFirstSpeed() {
        return firstspeed;
    }

    public void setFirstSpeed(String firstSpeed) {
        this.firstspeed = firstSpeed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBskj() {
        return bskj;
    }

    public void setBskj(String bskj) {
        this.bskj = bskj;
    }

    public String getBssj() {
        return bssj;
    }

    public void setBssj(String bssj) {
        this.bssj = bssj;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getBsmc() {
        return bsmc;
    }

    public void setBsmc(String bsmc) {
        this.bsmc = bsmc;
    }

    public String getBsgm() {
        return bsgm;
    }

    public void setBsgm(String bsgm) {
        this.bsgm = bsgm;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public static class distanceComparator implements Comparator<HistoryGradeInfo> {
        @Override
        public int compare(HistoryGradeInfo n, HistoryGradeInfo c) {
            float num = Float.valueOf(n.getBskj()) - Float.valueOf(c.getBskj());
            return (int) num;
        }
    }

    public static class rankComparator implements Comparator<HistoryGradeInfo> {
        @Override
        public int compare(HistoryGradeInfo n, HistoryGradeInfo c) {
            int num = Integer.valueOf(n.getBsmc()) - Integer.valueOf(c.getBsmc());
            return num;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bssj);
        dest.writeString(this.xmmc);
        dest.writeString(this.bsmc);
        dest.writeString(this.bsgm);
        dest.writeString(this.foot);
        dest.writeString(this.bskj);
        dest.writeString(this.speed);
    }

    protected HistoryGradeInfo(Parcel in) {
        this.bssj = in.readString();
        this.xmmc = in.readString();
        this.bsmc = in.readString();
        this.bsgm = in.readString();
        this.foot = in.readString();
        this.bskj = in.readString();
        this.speed = in.readString();
    }

    public static final Parcelable.Creator<HistoryGradeInfo> CREATOR = new Parcelable.Creator<HistoryGradeInfo>() {
        @Override
        public HistoryGradeInfo createFromParcel(Parcel source) {
            return new HistoryGradeInfo(source);
        }

        @Override
        public HistoryGradeInfo[] newArray(int size) {
            return new HistoryGradeInfo[size];
        }
    };
}
