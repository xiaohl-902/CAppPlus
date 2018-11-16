package com.cpigeon.app.event;

import com.cpigeon.app.entity.CircleMessageEntity;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class CircleMessageEvent {

    //清除新消息
    public static final String TYPE_CLEAN_NEW_MESSAGE = "TYPE_CLEAN_NEW_MESSAGE";

    public String type;
    //更新整個列表

    public CircleMessageEvent(String type){
        this.type = type;
    }
}
