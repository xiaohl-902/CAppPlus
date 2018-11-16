package com.cpigeon.app.modular.saigetong.model.bead;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTGzListEntity {


    /**
     * id : 421205
     * count : 1
     * data : [{"cskh":"2102","id":421205,"foot":"2017-22-0998689"}]
     * sjhm : 13540272181
     * cskh : 2102
     * xingming : 天胜鸽舍－任天富
     */

//    {"status":true,"errorCode":0,"msg":"","data":[{
//        "id":421205,//索引ID
//                "count":1,//记录个数
//                "data":[{"cskh":"2102"（参赛卡号，字符串）,"id":421205（索引ID，数字类型）,"foot":"2017-22-0998689"（足环号码）}],
//        "sjhm":"13540272181",//手机号码
//                "cskh":"2102",//参赛卡号
//                "xingming":"天胜鸽舍－任天富"//姓名
//    }]}


    private String id;//索引ID
    private int count;//记录个数
    private String sjhm;//手机号码
    private String cskh;//参赛卡号
    private String xingming;//姓名
    private List<DataBean> data;
    private int tag = 1;//点击展开tag 1 收缩，2展开


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getCskh() {
        return cskh;
    }

    public void setCskh(String cskh) {
        this.cskh = cskh;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cskh : 2102
         * id : 421205
         * foot : 2017-22-0998689
         */

        private String cskh;//参赛卡号，字符串
        private String id;//索引ID，数字类型
        private String foot;//足环号码
        private String color;//羽色
        private String pic;//照片数量

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCskh() {
            return cskh;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public void setCskh(String cskh) {
            this.cskh = cskh;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFoot() {
            return foot;
        }

        public void setFoot(String foot) {
            this.foot = foot;
        }
    }
}
