package com.cpigeon.app.entity;

import com.cpigeon.app.utils.http.GsonUtil;

import java.io.Serializable;

/**
 * Created by chenshuai on 2017/4/27.
 */

public class MapApiResponse<T> implements Serializable {

    /**
     * status : false
     * errorCode : 20002
     * msg :
     * data : null
     */

    public boolean status;
    public int errorCode;
    public String msg;
    public T data;

    public String toJsonString() {
        return GsonUtil.toJson(this);
    }
}
