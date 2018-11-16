package com.cpigeon.app.modular.matchlive.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class RPImages implements Parcelable {
    long sj;
    String imgurl;
    String slrurl;
    String tag;
    String updatefootinfo;
    String yearmonth;
    String day;
    int bupai;

    public int getBupai() {
        return bupai;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getSj() {
        return sj;
    }

    public void setSj(long sj) {
        this.sj = sj;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSlrurl() {
        return slrurl;
    }

    public void setSlrurl(String slrurl) {
        this.slrurl = slrurl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getUpdatefootinfo() {
        return updatefootinfo;
    }

    public void setUpdatefootinfo(String updatefootinfo) {
        this.updatefootinfo = updatefootinfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.sj);
        dest.writeString(this.imgurl);
        dest.writeString(this.slrurl);
        dest.writeString(this.tag);
        dest.writeString(this.updatefootinfo);
        dest.writeString(this.yearmonth);
        dest.writeString(this.day);
        dest.writeInt(this.bupai);
    }

    public RPImages() {
    }

    protected RPImages(Parcel in) {
        this.sj = in.readLong();
        this.imgurl = in.readString();
        this.slrurl = in.readString();
        this.tag = in.readString();
        this.updatefootinfo = in.readString();
        this.yearmonth = in.readString();
        this.day = in.readString();
        this.bupai = in.readInt();
    }

    public static final Parcelable.Creator<RPImages> CREATOR = new Parcelable.Creator<RPImages>() {
        @Override
        public RPImages createFromParcel(Parcel source) {
            return new RPImages(source);
        }

        @Override
        public RPImages[] newArray(int size) {
            return new RPImages[size];
        }
    };

}