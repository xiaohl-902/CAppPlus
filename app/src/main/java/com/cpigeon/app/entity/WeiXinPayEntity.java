package com.cpigeon.app.entity;

import com.google.gson.annotations.SerializedName;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class WeiXinPayEntity {
    public String orderNo;
    public String outTradeNo;
    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    public String sign;
    @SerializedName("package")
    public String packValue;

    public PayReq getPayReq(){
        PayReq payReq = new PayReq();
        payReq.appId = appid;
        payReq.partnerId = partnerid;
        payReq.prepayId = prepayid;
        payReq.packageValue = packValue == null ? "Sign=WXPay" : packValue;
        payReq.nonceStr = noncestr;
        payReq.timeStamp = timestamp;
        payReq.sign = sign;
        return payReq;
    }
}
