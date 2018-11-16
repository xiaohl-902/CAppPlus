package com.cpigeon.app.modular.guide.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class SignMultiSelectEntity implements Parcelable {

    public boolean isChoose;
    public boolean isChooseVisible;

    public SignMultiSelectEntity() {
    }

    public static final Creator<SignMultiSelectEntity> CREATOR = new Creator<SignMultiSelectEntity>() {
        @Override
        public SignMultiSelectEntity createFromParcel(Parcel in) {
            return new SignMultiSelectEntity(in);
        }

        @Override
        public SignMultiSelectEntity[] newArray(int size) {
            return new SignMultiSelectEntity[size];
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

    protected SignMultiSelectEntity(Parcel in) {
        this.isChoose = in.readByte() != 0;
        this.isChooseVisible = in.readByte() != 0;
    }

}
