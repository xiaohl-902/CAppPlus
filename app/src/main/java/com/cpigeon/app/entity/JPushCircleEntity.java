package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zhu TingYu on 2018/5/30.
 */

public class JPushCircleEntity implements Parcelable {

    /**
     * content-available : 1
     * uid : 26176
     * plnr : 呃呃呃
     * bplnr : 这样的
     * mid : 8928
     * plsj : 2018-05-30 16:31:53
     * plnc : Zg*9598
     * headimgurl : http://www.cpigeon.com/Content/faces/20180310194914.png
     * pt : 评论
     */
    private String pushId;
    @SerializedName("content-available")
    private String contentavailable;
    private int uid;
    private String plnr;
    private String bplnr;
    private int mid;
    private String plsj;
    private String plnc;
    private String headimgurl;
    private String pt;

    public String getContentavailable() {
        return contentavailable;
    }

    public void setContentavailable(String contentavailable) {
        this.contentavailable = contentavailable;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPlnr() {
        return plnr;
    }

    public void setPlnr(String plnr) {
        this.plnr = plnr;
    }

    public String getBplnr() {
        return bplnr;
    }

    public void setBplnr(String bplnr) {
        this.bplnr = bplnr;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getPlsj() {
        return plsj;
    }

    public void setPlsj(String plsj) {
        this.plsj = plsj;
    }

    public String getPlnc() {
        return plnc;
    }

    public void setPlnc(String plnc) {
        this.plnc = plnc;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public JPushCircleEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pushId);
        dest.writeString(this.contentavailable);
        dest.writeInt(this.uid);
        dest.writeString(this.plnr);
        dest.writeString(this.bplnr);
        dest.writeInt(this.mid);
        dest.writeString(this.plsj);
        dest.writeString(this.plnc);
        dest.writeString(this.headimgurl);
        dest.writeString(this.pt);
    }

    protected JPushCircleEntity(Parcel in) {
        this.pushId = in.readString();
        this.contentavailable = in.readString();
        this.uid = in.readInt();
        this.plnr = in.readString();
        this.bplnr = in.readString();
        this.mid = in.readInt();
        this.plsj = in.readString();
        this.plnc = in.readString();
        this.headimgurl = in.readString();
        this.pt = in.readString();
    }

    public static final Creator<JPushCircleEntity> CREATOR = new Creator<JPushCircleEntity>() {
        @Override
        public JPushCircleEntity createFromParcel(Parcel source) {
            return new JPushCircleEntity(source);
        }

        @Override
        public JPushCircleEntity[] newArray(int size) {
            return new JPushCircleEntity[size];
        }
    };
}
