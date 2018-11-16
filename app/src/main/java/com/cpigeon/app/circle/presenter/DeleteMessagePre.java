package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;
import retrofit2.http.PUT;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class DeleteMessagePre extends BasePresenter{

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_SELF = 2;

    int userId;
    public int messageId;
    public int showType;

    public DeleteMessagePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void setShowType(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CircleModel.setMessageShowType(userId, messageId, showType),consumer);
    }

    public void deleteMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CircleModel.deleteMessage(userId, messageId),consumer);
    }
}
