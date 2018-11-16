package com.cpigeon.app.utils.databean;

import com.cpigeon.app.utils.http.GsonUtil;

import java.io.Serializable;

/**
 * Created by chenshuai on 2017/4/27.
 */

public class ApiResponse<T> implements Serializable {

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
    public boolean isCache = false;

    public boolean isOk(){
        return errorCode != -1 ;
    }
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isNotDate(){
        return !status;
    }

    public boolean isHaveDate(){
        return isOk() && status;
    }

    public String toJsonString() {
        return GsonUtil.toJson(this);
    }
}
