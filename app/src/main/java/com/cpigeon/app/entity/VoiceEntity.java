package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class VoiceEntity implements Parcelable {
    public String id;//发票信息索引ID
    public String lxr;//收件人
    public String yx;//收件人电子邮箱
    public String lx;//类型：纸质发票|电子发票
    public String sh;//税号
    public String dz;//地址
    public String dh;
    public String dwmc;//单位名称
    public String  p;//收件人所在省
    public String  c;//收件人所在市
    public String  a;//收件人所在区县

    public VoiceEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.lxr);
        dest.writeString(this.yx);
        dest.writeString(this.lx);
        dest.writeString(this.sh);
        dest.writeString(this.dz);
        dest.writeString(this.dh);
        dest.writeString(this.dwmc);
        dest.writeString(this.p);
        dest.writeString(this.c);
        dest.writeString(this.a);
    }

    protected VoiceEntity(Parcel in) {
        this.id = in.readString();
        this.lxr = in.readString();
        this.yx = in.readString();
        this.lx = in.readString();
        this.sh = in.readString();
        this.dz = in.readString();
        this.dh = in.readString();
        this.dwmc = in.readString();
        this.p = in.readString();
        this.c = in.readString();
        this.a = in.readString();
    }

    public static final Creator<VoiceEntity> CREATOR = new Creator<VoiceEntity>() {
        @Override
        public VoiceEntity createFromParcel(Parcel source) {
            return new VoiceEntity(source);
        }

        @Override
        public VoiceEntity[] newArray(int size) {
            return new VoiceEntity[size];
        }
    };
}
