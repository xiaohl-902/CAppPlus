package com.cpigeon.app.utils.http;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class CPAPIHttpUtilNoV1<T> extends HttpUtil<T>{

    public static <T> HttpUtil<T> build(){
        HttpUtil<T> httpUtil = HttpUtil.builder();
        httpUtil.addHeader("u", CommonTool.getUserToken(MyApp.getInstance().getBaseContext()));
        httpUtil.setHeadUrl(MyApp.getInstance().getBaseContext().getString(R.string.api_head_url_no_v1));
        httpUtil.setUserId("u", CpigeonData.getInstance().getUserId(MyApp.getInstance().getBaseContext()));
        return httpUtil;

    }
}
