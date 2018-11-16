package com.cpigeon.app.modular.matchlive.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class TrainingDataEntity  {

    private List<RPImages> piclist;
    private List<HistoryGradeInfo> racelist;
    private String tcsyts;

    public String getTcsyts() {
        return tcsyts;
    }

    public void setTcsyts(String tcsyts) {
        this.tcsyts = tcsyts;
    }


    public List<RPImages> getPiclist() {
        return piclist;
    }

    public void setPiclist(List<RPImages> piclist) {
        this.piclist = piclist;
    }

    public List<HistoryGradeInfo> getRacelist() {
        return racelist;
    }

    public void setRacelist(List<HistoryGradeInfo> racelist) {
        this.racelist = racelist;
    }

    public static class fristSpeedComparator implements Comparator<HistoryGradeInfo> {
        @Override
        public int compare(HistoryGradeInfo n, HistoryGradeInfo c) {
            float num = Float.valueOf(n.getFirstSpeed()) - Float.valueOf(c.getFirstSpeed());
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
}
