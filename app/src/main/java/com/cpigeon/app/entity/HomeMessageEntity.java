package com.cpigeon.app.entity;

import java.util.Date;

/**
 * Created by Zhu TingYu on 2018/1/30.
 */

public class HomeMessageEntity {

    /**
     * istop : 0
     * title : 公棚直播分流公告
     * time : 2016-05-09 14:42:24
     * id : 40
     * content : <p>
     由于赛事频繁，观看人数火爆，频繁刷新网页可能会出现以下提示：
     </p>
     <p>
     <a href="http://221.236.20.76/Content/nocontent.jpg" target="_blank"><img width="600" height="207" alt="" src="http://221.236.20.76/Content/nocontent.jpg" /></a>
     </p>
     <p>
     <a href="http://221.236.20.76/Content/nocontent.jpg" target="_blank">(点击看大图)</a>
     </p>
     <p>
     &nbsp;
     </p>
     <p>
     为了让用户能正常观看比赛，现将公棚直播进行分流。
     </p>
     <p>
     公棚直播分流地址：<span><a href="http://gp.aj52gx.com/" target="_blank">公棚直播2</a></span>
     </p>
     */

    private int istop;
    private String title;
    private Date time;
    private int id;
    private String content;

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
