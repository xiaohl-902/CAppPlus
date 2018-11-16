package com.cpigeon.app.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class ChooseImageEntity implements MultiItemEntity {

    public static final int TYPE_IMG = 3;
    public static final int TYPE_ADD = 4;

    int type;
    public String url;

    @Override
    public int getItemType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
