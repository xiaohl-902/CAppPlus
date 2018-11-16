package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/11/16.
 */

@Table(name = "collection")
public class Collection {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "value")
    private String value;//

    @Column(name = "key")
    private String key;//关注关键字

    @Column(name = "content")
    private String content;//关注内容

    @Column(name = "type", property = " NOT NULL")
    private String type;//关注类型

    @Column(name = "colltime")
    private long collTime;

    @Column(name = "colluserid")
    private int collUserid;

    public Collection() {
    }

    public Collection(int id, String type, String key,  long collTime) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.collTime = collTime;
    }

    public Collection(String type, String value,  long collTime) {
        this.type = type;
        this.value = value;
        this.collTime = collTime;
    }

    public Collection(String type, long collTime) {
        this.type = type;
        this.collTime = collTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public long getCollTime() {
        return collTime;
    }

    public void setCollTime(long collTime) {
        this.collTime = collTime;
    }

    public int getCollUserid() {
        return collUserid;
    }

    public void setCollUserid(int collUserid) {
        this.collUserid = collUserid;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", key='" + key + '\'' +
                ", type=" + type +
                ", collTime=" + collTime +
                '}';
    }

    public enum CollectionType {
        NEWS("news"),//新闻
        RACE("race"),//比赛
        ORG("org");//组织(协会 公棚)

        private final String value;

        CollectionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
