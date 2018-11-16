package com.cpigeon.app.event;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class WXPayResultEvent {
    public int code;

    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = -1;
    public static final int CODE_CANCLE = -2;
    public WXPayResultEvent(int code){
        this.code = code;
    }

}
