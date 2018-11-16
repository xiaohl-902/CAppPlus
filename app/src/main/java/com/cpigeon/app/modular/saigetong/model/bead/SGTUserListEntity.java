package com.cpigeon.app.modular.saigetong.model.bead;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTUserListEntity {


    /**
     * userid : 5473
     * gpmc : 信鸽公棚
     * tpcount : 0
     */

    private String userid;
    private String gpmc;
    private int tpcount;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGpmc() {
        return gpmc;
    }

    public void setGpmc(String gpmc) {
        this.gpmc = gpmc;
    }

    public int getTpcount() {
        return tpcount;
    }

    public void setTpcount(int tpcount) {
        this.tpcount = tpcount;
    }
}
