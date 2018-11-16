package com.cpigeon.app.modular.saigetong.model.bead;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SGTFootInfoEntity {

    /**
     * ttzb :
     * diqu : 成都
     * id : 116415
     * ring : FFBDE0032EC33D92
     * sjhm : 18008073708
     * scsj : /Date(1435659683550)/
     * sex :
     * eye :
     * foot : 2015-22-0950491
     * color : 灰
     * xingming : 尹子叶
     * cskh : 0228
     * rpsj : /Date(1435657082000)/
     */

    /**
     * 足环详情：/CHAPI/V1/SGT_GetFootInfo
     参数：
     uid:会员ID
     id:足环索引ID
     返回值：
     {"status":true,"errorCode":0,"msg":"","data":{
     "ttzb":"",				//团体组别
     "diqu":"成都",				//地区
     "id":116415,				//足环索引ID
     "ring":"FFBDE0032EC33D92",		//电子环
     "sjhm":"18008073708",			//手机号码
     "scsj":"\/Date(1435659683550)\/",	//上传日期
     "sex":"",				//性别：雌雄
     "eye":"",				//眼砂
     "foot":"2015-22-0950491",		//足环号码
     "color":"灰",				//羽色
     "xingming":"尹子叶",			//鸽主姓名
     "cskh":"0228",				//参赛卡号
     "rpsj":"\/Date(1435657082000)\/"	//入棚时间
     }}
     */

    private String ttzb;
    private String diqu;
    private int id;
    private String ring;
    private String sjhm;
    private String scsj;
    private String sex;
    private String eye;
    private String foot;
    private String color;
    private String xingming;
    private String cskh;
    private String rpsj;

    public String getTtzb() {
        return ttzb;
    }

    public void setTtzb(String ttzb) {
        this.ttzb = ttzb;
    }

    public String getDiqu() {
        return diqu;
    }

    public void setDiqu(String diqu) {
        this.diqu = diqu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getScsj() {
        return scsj;
    }

    public void setScsj(String scsj) {
        this.scsj = scsj;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getCskh() {
        return cskh;
    }

    public void setCskh(String cskh) {
        this.cskh = cskh;
    }

    public String getRpsj() {
        return rpsj;
    }

    public void setRpsj(String rpsj) {
        this.rpsj = rpsj;
    }
}
