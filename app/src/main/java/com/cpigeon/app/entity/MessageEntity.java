package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class MessageEntity implements Parcelable {

    public int fsid;//短信发送ID，整数，传给获取发送号码接口获取发送的所有号码
    public String dxnr; //发送的短信内容",
    public String sjhm; //发送的手机号码",
    public int fscount;//发送条数，数字
    public String fssj; //2017/3/15 18:59:04"

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fsid);
        dest.writeString(this.dxnr);
        dest.writeString(this.sjhm);
        dest.writeInt(this.fscount);
        dest.writeString(this.fssj);
    }

    public MessageEntity() {
    }

    protected MessageEntity(Parcel in) {
        this.fsid = in.readInt();
        this.dxnr = in.readString();
        this.sjhm = in.readString();
        this.fscount = in.readInt();
        this.fssj = in.readString();
    }

    public static final Parcelable.Creator<MessageEntity> CREATOR = new Parcelable.Creator<MessageEntity>() {
        @Override
        public MessageEntity createFromParcel(Parcel source) {
            return new MessageEntity(source);
        }

        @Override
        public MessageEntity[] newArray(int size) {
            return new MessageEntity[size];
        }
    };
}
