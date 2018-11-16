package com.cpigeon.app.wxapi;

//import com.cpigeon.common.AppManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cpigeon.app.event.GXTUserInfoEvent;
import com.cpigeon.app.event.WXPayResultEvent;
import com.cpigeon.app.utils.CpigeonData;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/1/11.
 * 微信支付回调Activity
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    //微信开放平台appID
    public static final String APP_ID = "wxc9d120321bd1180a";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.d("baseReq = [" + baseReq.toString() + "]");
    }

    /**
     * 得到支付结果回调
     */
    @Override
    public void onResp(BaseResp baseResp) {
//        0 支付成功
//        -1 发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//        -2 用户取消 发生场景：用户不支付了，点击取消，返回APP。
        Logger.e("BaseResp{" +
                "errCode=" + baseResp.errCode +
                ", errStr='" + baseResp.errStr + '\'' +
                ", transaction='" + baseResp.transaction + '\'' +
                ", openId='" + baseResp.openId + '\'' +
                '}');// 支付结果码
        CpigeonData.getInstance().onWxPay(this, baseResp.errCode);
        EventBus.getDefault().post(new WXPayResultEvent(baseResp.errCode));
        EventBus.getDefault().post(new GXTUserInfoEvent());
        this.finish();
    }
}
