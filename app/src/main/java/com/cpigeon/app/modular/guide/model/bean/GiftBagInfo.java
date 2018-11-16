package com.cpigeon.app.modular.guide.model.bean;

/**
 * Created by Administrator on 2018/5/26.
 */

public class GiftBagInfo {

    /**
     * items : 累计签到3日礼包
     * gb : 10
     * dw : 鸽币
     */

    private String items;
    private String gb;
    private String dw;
    private String gid;
    private boolean isChoose;

    public GiftBagInfo() {
    }

    public GiftBagInfo(String items, String gb, String dw, String g, boolean isChoose) {
        this.items = items;
        this.gb = gb;
        this.dw = dw;
        this.gid = g;
        this.isChoose = isChoose;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getGb() {
        return gb;
    }

    public void setGb(String gb) {
        this.gb = gb;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String g) {
        this.gid = g;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
