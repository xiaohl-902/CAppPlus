package com.cpigeon.app.entity;

import com.cpigeon.app.utils.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/10.
 */

public class DynamicEntity extends BaseDynamicEntity {
    public int mid;//鸽迷圈ID
    public String uid;//会员ID
    public String nicheng;
    public String headurl;//头像图片路径
    public boolean guanzhu;//（登陆后）是否关注。true为已关注
    public String content;//发布信息内容
    public List<ImageEntity> imglist;

    public void setType() {
        super.setType(imglist.size() > 3 ? 3 : imglist.size());
    }
}
