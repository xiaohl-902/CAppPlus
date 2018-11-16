package com.cpigeon.app.message.ui.home;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.message.ui.order.ui.OrderModel;
import com.cpigeon.app.utils.databean.ApiResponse;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class PigeonHomePre extends BasePresenter {

    public int userId;

    public static final int STATE_NO_OPEN = 10000; //没有开通鸽信通
    public static final int STATE_ID_CARD_NOT_NORMAL = 10012;
    public static final int STATE_PERSON_INFO_NOT_NORMAL = 10013;
    public static final int STATE_NOT_PAY = 10014;

    public UserGXTEntity userGXTEntity;

    public PigeonHomePre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getUserInfo(Consumer<ApiResponse<UserGXTEntity>> consumer) {
        submitRequestThrowError(UserGXTModel.getUserInfo(userId).map(r -> {
            userGXTEntity = r.data;
            return r;
        }), consumer);
    }
    public void getGXTOrder(Consumer<ApiResponse<OrderInfoEntity>> consumer) {
        submitRequestThrowError(OrderModel.greatServiceOrder(userId).map(r -> {
            return r;
        }), consumer);
    }

}
