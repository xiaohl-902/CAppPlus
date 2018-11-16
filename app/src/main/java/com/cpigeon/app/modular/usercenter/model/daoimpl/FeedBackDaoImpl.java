package com.cpigeon.app.modular.usercenter.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.model.daoimpl.GetUserBandPhoneImpl;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;
import com.cpigeon.app.modular.usercenter.model.dao.IFeedBackDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class FeedBackDaoImpl extends GetUserBandPhoneImpl implements IFeedBackDao {

    IFeedBackDao.OnCompleteListener onCompleteListener;
    private CallAPI.Callback<Boolean> mSendFeedbackCallback = new CallAPI.Callback<Boolean>() {
        @Override
        public void onSuccess(Boolean data) {
            if (onCompleteListener != null) {
                onCompleteListener.onSuccess();
            }
        }

        @Override
        public void onError(int errorType, Object data) {
            String msg = "提交失败，请稍后再试";
            if (onCompleteListener != null) {
                if (errorType == ERROR_TYPE_API_RETURN) {
                    msg = "提交失败，请稍后再试(code:" + data + ")";
                    switch ((int) data) {
                        case 10004:
                            msg = "手机号码格式不正确";
                            break;
                        case 10005:
                            msg = "请填写反馈内容";
                            break;
                    }
                }
                onCompleteListener.onFail(msg);
            }
        }
    };

    @Override
    public void feedback(String content, String phoneNum, IFeedBackDao.OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        CallAPI.addFeedback(MyApp.getInstance(), content, phoneNum, mSendFeedbackCallback);
    }

    /**
     * 获取最新意见反馈结果
     *
     * @param onCompleteListener
     */
    @Override
    public void getLastFeedback(final IBaseDao.OnCompleteListener<FeedBackResult> onCompleteListener) {
        CallAPI.getFeedbackResult(MyApp.getInstance(), new CallAPI.Callback<FeedBackResult>() {
            @Override
            public void onSuccess(FeedBackResult data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("获取失败");
            }
        });
    }

    /**
     * 获取意见反馈结果列表
     *
     * @param pageIndex
     * @param pageSize
     * @param onCompleteListener
     */
    @Override
    public void getFeedbacks(int pageIndex, int pageSize, final IBaseDao.OnCompleteListener<List<FeedBackResult>> onCompleteListener) {
        CallAPI.getFeedbackResults(MyApp.getInstance(), pageIndex, pageSize, new CallAPI.Callback<List<FeedBackResult>>() {
            @Override
            public void onSuccess(List<FeedBackResult> data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("获取失败");
            }
        });
    }
}
