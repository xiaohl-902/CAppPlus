package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class IdCardPInfoEntity implements Parcelable {

    /**
     * errorcode : 0
     * errormsg : OK
     * session_id : 
     * name : 李爽
     * name_confidence_all : [99,99]
     * sex : 男
     * sex_confidence_all : [79]
     * nation : 汉
     * nation_confidence_all : [99]
     * birth : 1983/4/13
     * birth_confidence_all : [100,100,100,100,100,100,100,100,100]
     * address : 武汉市洪山区李家大湾118号
     * address_confidence_all : [99,99,99,99,99,99,99,99,99,99,99,99,99,99]
     * id : 420111198304131819
     * id_confidence_all : [100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100]
     * frontimage : /9j/4AAQSkZJRgABAQAAAQABAAD/2wBDA
     * watermask_confidence_all : []
     * valid_date_confidence_all : []
     * authority_confidence_all : []
     * backimage_confidence_all : []
     * detail_errorcode : []
     * detail_errormsg : []
     */

    public int errorcode;
    public String errormsg;
    public String session_id;
    public String name;
    public String sex;
    public String nation;
    public String birth;
    public String address;
    public String id;
    public String frontimage;
    public List<Integer> name_confidence_all;
    public List<Integer> sex_confidence_all;
    public List<Integer> nation_confidence_all;
    public List<Integer> birth_confidence_all;
    public List<Integer> address_confidence_all;
    public List<Integer> id_confidence_all;
    public List<Object> frontimage_confidence_all;
    public List<Object> watermask_confidence_all;
    public List<Object> valid_date_confidence_all;
    public List<Object> authority_confidence_all;
    public List<Object> backimage_confidence_all;
    public List<Object> detail_errorcode;
    public List<Object> detail_errormsg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
        dest.writeString(this.session_id);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.nation);
        dest.writeString(this.birth);
        dest.writeString(this.address);
        dest.writeString(this.id);
        dest.writeString(this.frontimage);
        dest.writeList(this.name_confidence_all);
        dest.writeList(this.sex_confidence_all);
        dest.writeList(this.nation_confidence_all);
        dest.writeList(this.birth_confidence_all);
        dest.writeList(this.address_confidence_all);
        dest.writeList(this.id_confidence_all);
        dest.writeList(this.frontimage_confidence_all);
        dest.writeList(this.watermask_confidence_all);
        dest.writeList(this.valid_date_confidence_all);
        dest.writeList(this.authority_confidence_all);
        dest.writeList(this.backimage_confidence_all);
        dest.writeList(this.detail_errorcode);
        dest.writeList(this.detail_errormsg);
    }

    public IdCardPInfoEntity() {
    }

    protected IdCardPInfoEntity(Parcel in) {
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
        this.session_id = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.nation = in.readString();
        this.birth = in.readString();
        this.address = in.readString();
        this.id = in.readString();
        this.frontimage = in.readString();
        this.name_confidence_all = new ArrayList<Integer>();
        in.readList(this.name_confidence_all, Integer.class.getClassLoader());
        this.sex_confidence_all = new ArrayList<Integer>();
        in.readList(this.sex_confidence_all, Integer.class.getClassLoader());
        this.nation_confidence_all = new ArrayList<Integer>();
        in.readList(this.nation_confidence_all, Integer.class.getClassLoader());
        this.birth_confidence_all = new ArrayList<Integer>();
        in.readList(this.birth_confidence_all, Integer.class.getClassLoader());
        this.address_confidence_all = new ArrayList<Integer>();
        in.readList(this.address_confidence_all, Integer.class.getClassLoader());
        this.id_confidence_all = new ArrayList<Integer>();
        in.readList(this.id_confidence_all, Integer.class.getClassLoader());
        this.frontimage_confidence_all = new ArrayList<Object>();
        in.readList(this.frontimage_confidence_all, Object.class.getClassLoader());
        this.watermask_confidence_all = new ArrayList<Object>();
        in.readList(this.watermask_confidence_all, Object.class.getClassLoader());
        this.valid_date_confidence_all = new ArrayList<Object>();
        in.readList(this.valid_date_confidence_all, Object.class.getClassLoader());
        this.authority_confidence_all = new ArrayList<Object>();
        in.readList(this.authority_confidence_all, Object.class.getClassLoader());
        this.backimage_confidence_all = new ArrayList<Object>();
        in.readList(this.backimage_confidence_all, Object.class.getClassLoader());
        this.detail_errorcode = new ArrayList<Object>();
        in.readList(this.detail_errorcode, Object.class.getClassLoader());
        this.detail_errormsg = new ArrayList<Object>();
        in.readList(this.detail_errormsg, Object.class.getClassLoader());
    }

    public static final Creator<IdCardPInfoEntity> CREATOR = new Creator<IdCardPInfoEntity>() {
        @Override
        public IdCardPInfoEntity createFromParcel(Parcel source) {
            return new IdCardPInfoEntity(source);
        }

        @Override
        public IdCardPInfoEntity[] newArray(int size) {
            return new IdCardPInfoEntity[size];
        }
    };
}
