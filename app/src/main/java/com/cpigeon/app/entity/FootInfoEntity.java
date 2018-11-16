package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class FootInfoEntity implements Parcelable {
    public String st;
    public String tid;//:6,
    public String csys;//:903,
    public String mc;//:6,
    public String orgname;//:"四川省遂宁市金翅赛鸽俱乐部",
    public String foot;//:"2011-22-1566641",
    public String bskj;//:"349.735",
    public String speed;//:1084.8326,
    public String xmmc;//:"洋县奖赛暨鸽王第三关"
    public String name;//鸽主姓名

    public FootInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.st);
        dest.writeString(this.tid);
        dest.writeString(this.csys);
        dest.writeString(this.mc);
        dest.writeString(this.orgname);
        dest.writeString(this.foot);
        dest.writeString(this.bskj);
        dest.writeString(this.speed);
        dest.writeString(this.xmmc);
        dest.writeString(this.name);
    }

    protected FootInfoEntity(Parcel in) {
        this.st = in.readString();
        this.tid = in.readString();
        this.csys = in.readString();
        this.mc = in.readString();
        this.orgname = in.readString();
        this.foot = in.readString();
        this.bskj = in.readString();
        this.speed = in.readString();
        this.xmmc = in.readString();
        this.name = in.readString();
    }

    public static final Creator<FootInfoEntity> CREATOR = new Creator<FootInfoEntity>() {
        @Override
        public FootInfoEntity createFromParcel(Parcel source) {
            return new FootInfoEntity(source);
        }

        @Override
        public FootInfoEntity[] newArray(int size) {
            return new FootInfoEntity[size];
        }
    };
}
