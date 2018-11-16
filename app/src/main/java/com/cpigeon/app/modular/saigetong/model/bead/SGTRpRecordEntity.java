package com.cpigeon.app.modular.saigetong.model.bead;

import java.util.List;

/**
 * 赛鸽通入棚记录
 * Created by Administrator on 2018/1/20.
 */

public class SGTRpRecordEntity {

    /**
     * list : [{"tpcount":"图片数量（数字类型）","title":"标题","tid":"索引ID（数字类型，作为参数获取鸽主列表）"}]
     * allgzcount : 22227
     * alltpcount : 0
     */

    private int allgzcount;//公棚入棚鸽子总数
    private int alltpcount;//图片总数
    private List<ListBean> list;
    private  String guid;////公棚会员ID，作为参数传递到足环详细信息视图

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getAllgzcount() {
        return allgzcount;
    }

    public void setAllgzcount(int allgzcount) {
        this.allgzcount = allgzcount;
    }

    public int getAlltpcount() {
        return alltpcount;
    }

    public void setAlltpcount(int alltpcount) {
        this.alltpcount = alltpcount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * tpcount : 图片数量（数字类型）
         * title : 标题
         * tid : 索引ID（数字类型，作为参数获取鸽主列表）
         */

        private int tpcount;
        private String title;
        private String tid;

        public int getTpcount() {
            return tpcount;
        }

        public void setTpcount(int tpcount) {
            this.tpcount = tpcount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
    }
}
