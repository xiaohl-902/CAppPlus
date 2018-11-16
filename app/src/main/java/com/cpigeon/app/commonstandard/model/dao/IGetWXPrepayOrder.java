package com.cpigeon.app.commonstandard.model.dao;

import android.support.annotation.NonNull;

import com.tencent.mm.opensdk.modelpay.PayReq;


/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IGetWXPrepayOrder {
    void getWXPrepayOrderForOrder(long orderId, @NonNull IBaseDao.OnCompleteListener<PayReq> onCompleteListener);

    void getWXPrePayOrderForRecharge(long depositId, @NonNull IBaseDao.OnCompleteListener<PayReq> onCompleteListener);
}
