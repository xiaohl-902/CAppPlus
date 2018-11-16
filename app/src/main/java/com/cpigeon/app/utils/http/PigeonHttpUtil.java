package com.cpigeon.app.utils.http;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class PigeonHttpUtil<T> extends HttpUtil<T> {
    public static <T> HttpUtil<T> build(){
        HttpUtil<T> httpUtil = HttpUtil.builder();
        httpUtil.addHeader("u", CommonTool.getUserToken(MyApp.getInstance().getBaseContext()));
        httpUtil.setHeadUrl(MyApp.getInstance().getBaseContext().getString(R.string.api_head_url));
        //httpUtil.setUserId("u", CpigeonData.getInstance().getUserId(MyApp.getInstance().getBaseContext()));
        return httpUtil;

    }
}
