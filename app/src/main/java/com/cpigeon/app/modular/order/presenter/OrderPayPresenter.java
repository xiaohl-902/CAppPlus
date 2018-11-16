package com.cpigeon.app.modular.order.presenter;

import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetWXPrepayOrder;
import com.cpigeon.app.commonstandard.model.daoimpl.GetWXPrepayOrderImpl;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.message.ui.order.ui.OrderModel;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.IOrderPayDao;
import com.cpigeon.app.modular.order.model.daoimpl.OrderPayDaoImpl;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderPayView;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OrderPayPresenter extends BasePresenter<IOrderPayView, IOrderPayDao> {
    IGetWXPrepayOrder mGetWXPrepayOrder;

    int userId;
    public String orderId;
    public String voiceId;
    private String isBindVoice;

    public OrderPayPresenter(IOrderPayView mView) {
        super(mView);
        mGetWXPrepayOrder = new GetWXPrepayOrderImpl();
        userId = CpigeonData.getInstance().getUserId(MyApp.getInstance());
    }

    @Override
    protected IOrderPayDao initDao() {
        return new OrderPayDaoImpl();
    }

    public void getOrderInfoById(long orderId) {
        mView.showTips("加载订单中...", IView.TipType.LoadingShow);
        mDao.getOrderInfoById(orderId, new IBaseDao.OnCompleteListener<CpigeonOrderInfo>() {
            @Override
            public void onSuccess(final CpigeonOrderInfo data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide);
                        mView.showOrderInfo(data);
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide);
                        mView.showTips("加载订单信息失败", IView.TipType.DialogError);
                    }
                }, 300);
            }
        });
    }

    IBaseDao.OnCompleteListener<Boolean> onPayCompleteListener = new IBaseDao.OnCompleteListener<Boolean>() {
        @Override
        public void onSuccess(final Boolean data) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showPayResult(data);
                }
            }, 300);
        }

        @Override
        public void onFail(final String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            }, 300);
        }
    };

    public void payOrder(String payPwd) {
        if (mView.PAY_TYPE_YUE.equals(mView.getPayType())) {
            mView.showTips("支付中...", IView.TipType.LoadingShow);
            mDao.orderPayByBalance(mView.getOrderId(), payPwd, onPayCompleteListener);
        } else if (mView.PAY_TYPE_JIFEN.equals(mView.getPayType())) {
            mView.showTips("支付中...", IView.TipType.LoadingShow);
            mDao.orderPayByScore(mView.getOrderId(), payPwd, onPayCompleteListener);
        }
    }

    public void loadUserScoreAndBalance() {
        mDao.getUserScoreAndBalance(CpigeonData.getInstance().getUserId(MyApp.getInstance()), new IBaseDao.OnCompleteListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data.containsKey("jifen"))
                    CpigeonData.getInstance().setUserScore((Integer) data.get("jifen"));
                if (data.containsKey("yue"))
                    CpigeonData.getInstance().setUserBalance((Double) data.get("yue"));
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public void wxPay() {
        if (mView.getPayReqCache() != null) {
            mView.entryWXPay(mView.getPayReqCache());
            return;
        }
        mView.showTips("跳转中...", IView.TipType.LoadingShow);
        mGetWXPrepayOrder.getWXPrepayOrderForOrder(mView.getOrderId(), new IBaseDao.OnCompleteListener<PayReq>() {
            @Override
            public void onSuccess(final PayReq data) {
                postDelayed(
                        new CheckAttachRunnable() {
                            @Override
                            public void runAttached() {
                                mView.showTips(null, IView.TipType.LoadingHide);
                                mView.entryWXPay(data);
                            }
                        }, 300);
            }

            @Override
            public void onFail(final String msg) {
                postDelayed(
                        new CheckAttachRunnable() {
                            @Override
                            public void runAttached() {
                                mView.showTips(null, IView.TipType.LoadingHide);
                                mView.showTips(msg, IView.TipType.ToastShort);
                            }
                        }, 300);
            }
        });
    }

    public void bindVoice(Consumer<String> consumer){
        submitRequestThrowError(OrderModel.bindVoiceOnOrder(userId, voiceId,orderId,isBindVoice).map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void getVoice(Consumer<VoiceEntity> consumer){
        submitRequestThrowError(OrderModel.getVoiceInfo(userId, voiceId).map(r -> {
            if(r.status){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void setIsBindVoice(boolean isBindVoice){
        if(isBindVoice){
            this.isBindVoice = "y";
        }else {
            this.isBindVoice = "n";
        }
    }
}
