package com.cpigeon.app.utils.http;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;

/**
 * Created by Zhu TingYu on 2018/1/10.
 */

public class RHttpUtil<T> extends RequestUtil<T>{
    public static <T> RequestUtil<T> build(){
        RequestUtil<T> httpUtil = RequestUtil.builder();
        httpUtil.addHead("u", CommonTool.getUserToken(MyApp.getInstance().getBaseContext()));
        httpUtil.setBaseUrl(CPigeonApiUrl.getInstance().getServer());
        httpUtil.headUrl(MyApp.getInstance().getBaseContext().getString(R.string.api_head_url));
        httpUtil.setUserId(CpigeonData.getInstance().getUserId(MyApp.getInstance().getBaseContext()));
        return httpUtil;

    }
}
