package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class OrderInfoEntity implements Parcelable {
    public String id;//订单 ID,
    public String time;//订单时间’,
    public String number;//订单编号’,
    public String item;//订单内容”,
    public String price;//所需金额,’
    public String scores;//所需积分”
    public String fpid;//发票ID。大于0为用户需要发票。

    public OrderInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.time);
        dest.writeString(this.number);
        dest.writeString(this.item);
        dest.writeString(this.price);
        dest.writeString(this.scores);
        dest.writeString(this.fpid);
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = in.readString();
        this.time = in.readString();
        this.number = in.readString();
        this.item = in.readString();
        this.price = in.readString();
        this.scores = in.readString();
        this.fpid = in.readString();
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel source) {
            return new OrderInfoEntity(source);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
