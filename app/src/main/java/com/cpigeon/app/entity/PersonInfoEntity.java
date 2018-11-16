package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;

/**
 * Created by Zhu TingYu on 2017/12/1.
 */

public class PersonInfoEntity implements Parcelable {

    public byte[] yyzz; //营业执照图片 data
    public byte[] sfzzm;      //身份证正面图片 data
    public String sjhm;  //联系电话
    public String dwmc;//单位名称",
    public byte[] sfzbm; //身份证背面图片 data
    public String xingming; //姓名"

    public String qianming; //签名",
    public int qmshenhe;  //签名审核标记：1为已审核，否则未审核
    public String shenqingqm;

    public boolean isExamine(){
        return qmshenhe == 1;
    }

    public PersonInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.yyzz);
        dest.writeByteArray(this.sfzzm);
        dest.writeString(this.sjhm);
        dest.writeString(this.dwmc);
        dest.writeByteArray(this.sfzbm);
        dest.writeString(this.xingming);
        dest.writeString(this.qianming);
        dest.writeInt(this.qmshenhe);
        dest.writeString(this.shenqingqm);
    }

    protected PersonInfoEntity(Parcel in) {
        this.yyzz = in.createByteArray();
        this.sfzzm = in.createByteArray();
        this.sjhm = in.readString();
        this.dwmc = in.readString();
        this.sfzbm = in.createByteArray();
        this.xingming = in.readString();
        this.qianming = in.readString();
        this.qmshenhe = in.readInt();
        this.shenqingqm = in.readString();
    }

    public static final Creator<PersonInfoEntity> CREATOR = new Creator<PersonInfoEntity>() {
        @Override
        public PersonInfoEntity createFromParcel(Parcel source) {
            return new PersonInfoEntity(source);
        }

        @Override
        public PersonInfoEntity[] newArray(int size) {
            return new PersonInfoEntity[size];
        }
    };
}
