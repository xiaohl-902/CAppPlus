package com.cpigeon.app.entity;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class NewsEntity{

    public static final String TYPE_LIVE = "TYPE_LIVE";
    public static final String TYPE_DZCB = "TYPE_DZCB";
    public static final String TYPE_NEWS = "TYPE_NEWS";

    public String nid;
    public String id;
    public String type;
    public String title;
    public String imgurl;
    public String pic;


    public String getType(){
        String typeString = null;
        if(type.equals("zhibo")){
            typeString = TYPE_LIVE;
        }else if(type.equals("dzcb")){
            typeString = TYPE_DZCB;
        }else if(type.equals("news")){
            typeString = TYPE_NEWS;
        }
        return typeString;
    }

}
