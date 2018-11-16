package com.cpigeon.app.message.ui.order.ui.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.GXTMessagePrice;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.message.ui.order.ui.OrderModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class MessageCreateOrderPre extends BasePresenter {

    public final static int SID_MESSAGE = 23;
    int userId;
    public int messageCount;
    public double price;

    public MessageCreateOrderPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getGXTMessagePrice(Consumer<ApiResponse<GXTMessagePrice>> consumer){
        submitRequestThrowError(OrderModel.getMessagePrice(), consumer);
    }

    public void createGXTMessageOrder(Consumer<ApiResponse<OrderInfoEntity>> consumer){
        submitRequestThrowError(OrderModel.createGXTMessageOrder(userId, messageCount, price),consumer);
    }


    public Consumer<String> setMessageCount(Consumer<Integer> consumer){
        return s -> {
            if(StringValid.isStringValid(s)){
                messageCount = Integer.valueOf(s);
            }else {
                messageCount = 0;
            }
            Observable.<Integer>create(observableEmitter -> {
                observableEmitter.onNext(messageCount);
            }).subscribe(consumer);
        };
    }

}
