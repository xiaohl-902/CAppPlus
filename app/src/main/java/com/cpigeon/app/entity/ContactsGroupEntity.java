package com.cpigeon.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.cpigeon.app.utils.StringValid;

/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class ContactsGroupEntity extends MultiSelectEntity implements Parcelable {
    public String fztype; // xtfz表示系统指定分组，不能编辑和删除。
    public int fzid;//分组ID（整数）
    public String fzmc; //分组名称
    public int fzcount; //分组下联系人个数（整数）

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSystemGroup(){
        return StringValid.isStringValid(fztype) && fztype.equals("xtfz");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fztype);
        dest.writeInt(this.fzid);
        dest.writeString(this.fzmc);
        dest.writeInt(this.fzcount);
    }

    public ContactsGroupEntity() {
    }

    protected ContactsGroupEntity(Parcel in) {
        this.fztype = in.readString();
        this.fzid = in.readInt();
        this.fzmc = in.readString();
        this.fzcount = in.readInt();
    }

    public static final Parcelable.Creator<ContactsGroupEntity> CREATOR = new Parcelable.Creator<ContactsGroupEntity>() {
        @Override
        public ContactsGroupEntity createFromParcel(Parcel source) {
            return new ContactsGroupEntity(source);
        }

        @Override
        public ContactsGroupEntity[] newArray(int size) {
            return new ContactsGroupEntity[size];
        }
    };
}
