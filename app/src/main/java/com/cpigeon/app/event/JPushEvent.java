package com.cpigeon.app.event;

/**
 * Created by Zhu TingYu on 2018/5/30.
 */

public class JPushEvent<T> {
    public static final String TYPE_CIRCLE = "TYPE_CIRCLE";
    private T data;
    private String type;
    public JPushEvent(T data, String type){
        this.data = data;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public String getType() {
        return type;
    }
}
