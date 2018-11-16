package com.cpigeon.app.entity;

import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/10.
 */

public class HomeNewsEntity {

    public static final int TYPE_ONE = 0;
    public static final int TYPE_ALL = 1;

    public List<NewsEntity> newList = Lists.newArrayList();

    public static List<HomeNewsEntity> get(List<NewsEntity> data, int type){
        List<HomeNewsEntity> homeNewsList = Lists.newArrayList();
        HomeNewsEntity entity = new HomeNewsEntity();
        for (int i = 0; i < data.size(); i++) {
            entity.newList.add(data.get(i));
            if(entity.newList.size() == 4){
                homeNewsList.add(entity);
                if(type == TYPE_ONE){
                    return homeNewsList;
                }
                entity = new HomeNewsEntity();
            }
        }

        return homeNewsList;
    }

}
