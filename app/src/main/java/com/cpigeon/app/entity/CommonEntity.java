package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class CommonEntity extends MultiSelectEntity implements Parcelable {
    public String id;
    public String dxnr; //短语内容

    public CommonEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.dxnr);
    }

    protected CommonEntity(Parcel in) {
        super(in);
        this.id = in.readString();
        this.dxnr = in.readString();
    }

    public static final Creator<CommonEntity> CREATOR = new Creator<CommonEntity>() {
        @Override
        public CommonEntity createFromParcel(Parcel source) {
            return new CommonEntity(source);
        }

        @Override
        public CommonEntity[] newArray(int size) {
            return new CommonEntity[size];
        }
    };
}
