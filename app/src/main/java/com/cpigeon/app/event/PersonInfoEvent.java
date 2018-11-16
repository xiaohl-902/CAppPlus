package com.cpigeon.app.event;

import com.cpigeon.app.entity.PersonInfoEntity;

/**
 * Created by Zhu TingYu on 2017/12/1.
 */

public class PersonInfoEvent {
    public PersonInfoEntity entity;
    public int type;
    public PersonInfoEvent(int type){
        this.type = type;
    }
}
