package com.cpigeon.app.modular.usercenter.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by chenshuai on 2017/6/29.
 */
@Table(name = "userfollow")
public class UserFollow {

    @Column(name = "display")
    private String display;
    @Column(name = "time")
    private String time;
    @Column(name = "ftype")
    private String ftype;
    @Column(name = "fid", isId = true, autoGen = false)
    private int fid;
    @Column(name = "uid")
    private int uid;
    @Column(name = "rela")
    private String rela;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getRela() {
        return rela;
    }

    public void setRela(String rela) {
        this.rela = rela;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
