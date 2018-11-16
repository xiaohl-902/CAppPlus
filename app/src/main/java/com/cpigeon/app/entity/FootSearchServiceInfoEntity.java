package com.cpigeon.app.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class FootSearchServiceInfoEntity {

    /**
     * brief : 查询100次，每次显示5条，不能搜索和显示鸽主姓名
     * unitname : 次
     * package : 普通套餐
     * serviceid : 5
     * name : 足环查询服务
     * showNum : 5
     * servicetimes : 100
     * numbers : 27
     */

    public String brief;
    public String unitname;
    @SerializedName("package")
    public String packageX;
    public int serviceid;
    public String name;
    public int showNum;
    public int servicetimes;
    public int numbers;

}
