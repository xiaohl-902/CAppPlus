package com.cpigeon.app.event;

import com.cpigeon.app.entity.CircleMessageEntity;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleMessageDetailsEvent {

    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_THUMB = 1;

    public int type;

    CircleMessageEntity circleMessageEntity;

    public CircleMessageDetailsEvent(int type){
        this.type = type;
    }


}
