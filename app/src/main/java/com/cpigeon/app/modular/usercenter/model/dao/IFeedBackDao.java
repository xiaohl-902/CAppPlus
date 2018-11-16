package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IFeedBackDao extends IBaseDao, IGetUserBandPhone {
    interface OnCompleteListener {
        void onSuccess();

        void onFail(String msg);
    }

    void feedback(String content, String phoneNum, OnCompleteListener onCompleteListener);

    /**
     * 获取最新意见反馈结果
     * @param onCompleteListener
     */
    void getLastFeedback(IBaseDao.OnCompleteListener<FeedBackResult> onCompleteListener);

    /**
     * 获取意见反馈结果列表
     * @param pageIndex
     * @param pageSize
     * @param onCompleteListener
     */
    void getFeedbacks(int pageIndex, int pageSize, IBaseDao.OnCompleteListener<List<FeedBackResult>> onCompleteListener);
}
