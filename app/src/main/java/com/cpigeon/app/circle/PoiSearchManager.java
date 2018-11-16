package com.cpigeon.app.circle;


import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiSearch;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class PoiSearchManager {

    private static PoiSearch.Query query;
    static PoiSearch poiSearch;
    public static PoiSearchManager build(Context context, String cityCode){
        PoiSearchManager poiSearchManager = new PoiSearchManager();
        query = new PoiSearch.Query("风景名胜|商务住宅|政府机构及社会团体|道附属设施|地名地址信息", "", cityCode);
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);
        poiSearch = new PoiSearch(context, query);
        return poiSearchManager;
    }

    public PoiSearchManager setSearchListener(PoiSearch.OnPoiSearchListener listener){
        poiSearch.setOnPoiSearchListener(listener);
        return this;
    }
    public PoiSearchManager setBound(double lat, double lot){
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lot), 1000));
        return this;
    }

    public void search(){
        poiSearch.searchPOIAsyn();
    }

}
