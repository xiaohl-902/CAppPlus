package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class MultiSelectEntity implements Parcelable {

    public boolean isChoose;
    public boolean isChooseVisible;

    public MultiSelectEntity() {
    }

    public static final Creator<MultiSelectEntity> CREATOR = new Creator<MultiSelectEntity>() {
        @Override
        public MultiSelectEntity createFromParcel(Parcel in) {
            return new MultiSelectEntity(in);
        }

        @Override
        public MultiSelectEntity[] newArray(int size) {
            return new MultiSelectEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isChooseVisible ? (byte) 1 : (byte) 0);
    }

    protected MultiSelectEntity(Parcel in) {
        this.isChoose = in.readByte() != 0;
        this.isChooseVisible = in.readByte() != 0;
    }

}
