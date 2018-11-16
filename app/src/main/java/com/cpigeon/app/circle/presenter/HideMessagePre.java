package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class HideMessagePre extends BasePresenter {

    BaseActivity activity;
    int userId;
    public int messageId;
    public int hideUserId;
    public int blackUserId;
    public int isHide;//1 屏蔽， 0取消屏蔽

    public HideMessagePre(Activity activity) {
        super(activity);
        this.activity = (BaseActivity) activity;
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void hideMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CircleModel.hideCircleMessage(userId, messageId, isHide),consumer);
    }

    public void hideUser(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CircleModel.hideCircleUser(userId, hideUserId, isHide),consumer);
    }

    public void addBlackList(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CircleModel.addUserToBlackList(userId, blackUserId, isHide),consumer);
    }



    public void setIsHide(boolean isHide){
       this.isHide = isHide ? 1 : 0;
    }

}
