package com.cpigeon.app.modular.lineweather.model.bean;

/**
 * Created by Administrator on 2018/5/9.
 */

public class GetGongPengListEntity {


    /**
     * gpmc : 公棚名称
     * jd : 经 度
     * wd : 纬度
     */

    private String gpmc;
    private String jd;
    private String wd;

    public String getGpmc() {
        return gpmc;
    }

    public void setGpmc(String gpmc) {
        this.gpmc = gpmc;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }
}
