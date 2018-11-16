package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class CircleUserInfoEntity implements Parcelable {

    public int id;

    public String diqu;//地区
    public int sfgz;//是否被登录用户关注
    public String username;//用户名
    public String intro;//简介
    public String nickname;//昵称
    public int msgCount;//信息条数
    public String birth;//生日
    public int fsCount;//粉丝个数
    public String sign;//签名
    public String sex;//性别
    public int gzCount;//关注个数
    public String sjhm;//手机号码
    public String headimgurl;//头像地址

    public boolean isFollow(){
        return sfgz == 1;
    }

    public void setFollow(boolean isFollow){
         sfgz = isFollow ? 1 : 0;
    }

    public CircleUserInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.diqu);
        dest.writeInt(this.sfgz);
        dest.writeString(this.username);
        dest.writeString(this.intro);
        dest.writeString(this.nickname);
        dest.writeInt(this.msgCount);
        dest.writeString(this.birth);
        dest.writeInt(this.fsCount);
        dest.writeString(this.sign);
        dest.writeString(this.sex);
        dest.writeInt(this.gzCount);
        dest.writeString(this.sjhm);
        dest.writeString(this.headimgurl);
    }

    protected CircleUserInfoEntity(Parcel in) {
        this.diqu = in.readString();
        this.sfgz = in.readInt();
        this.username = in.readString();
        this.intro = in.readString();
        this.nickname = in.readString();
        this.msgCount = in.readInt();
        this.birth = in.readString();
        this.fsCount = in.readInt();
        this.sign = in.readString();
        this.sex = in.readString();
        this.gzCount = in.readInt();
        this.sjhm = in.readString();
        this.headimgurl = in.readString();
    }

    public static final Creator<CircleUserInfoEntity> CREATOR = new Creator<CircleUserInfoEntity>() {
        @Override
        public CircleUserInfoEntity createFromParcel(Parcel source) {
            return new CircleUserInfoEntity(source);
        }

        @Override
        public CircleUserInfoEntity[] newArray(int size) {
            return new CircleUserInfoEntity[size];
        }
    };
}
