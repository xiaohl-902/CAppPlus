package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2018/1/12.
 */

public class SnsEntity implements Parcelable {

    /**
     * 当前用户是否对该信息进行点赞
     */

    public int zan =  0;

    public static final Creator<SnsEntity> CREATOR = new Creator<SnsEntity>() {
        @Override
        public SnsEntity createFromParcel(Parcel in) {
            return new SnsEntity(in);
        }

        @Override
        public SnsEntity[] newArray(int size) {
            return new SnsEntity[size];
        }
    };

    public boolean isThumb(){
        return zan != 0; //0为取消点赞
    }

    public void setThumb(){
        zan = 1;
    }
    public void setCancelThumb(){
        zan = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.zan);
    }

    public SnsEntity() {
    }

    protected SnsEntity(Parcel in) {
        this.zan = in.readInt();
    }

}
