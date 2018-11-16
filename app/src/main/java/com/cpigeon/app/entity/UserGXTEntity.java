package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class UserGXTEntity implements Parcelable {
    public int tyxy; //是否已同意使用协议
    public int syts; //短信剩余条数
    public String qianming;

    public UserGXTEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tyxy);
        dest.writeInt(this.syts);
        dest.writeString(this.qianming);
    }

    protected UserGXTEntity(Parcel in) {
        this.tyxy = in.readInt();
        this.syts = in.readInt();
        this.qianming = in.readString();
    }

    public static final Creator<UserGXTEntity> CREATOR = new Creator<UserGXTEntity>() {
        @Override
        public UserGXTEntity createFromParcel(Parcel source) {
            return new UserGXTEntity(source);
        }

        @Override
        public UserGXTEntity[] newArray(int size) {
            return new UserGXTEntity[size];
        }
    };
}
