package com.cpigeon.app.modular.usercenter.model.bean;

import java.io.Serializable;

/**
 * Created by chenshuai on 2017/6/22.
 */

public class FeedBackResult implements Serializable {

    /**
     * state : 待处理
     * time : 2017-04-11 16:01:07
     * content : 1111
     * result :
     */

    private String state;
    private String time;
    private String content;
    private String result;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
