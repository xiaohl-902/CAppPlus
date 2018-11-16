package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.CircleUserInfoEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class CirclePre extends BasePresenter {

    public static final int TYPE_USER = 0;
    public static final int TYPE_HIS = 1;
    public int userId;
    public int type;
    private int isFollow; //1关注，0取消关注
    public int followId;
    public CircleUserInfoEntity circleUserInfoEntity;
    public String homeMessageType;

    public CirclePre(Activity activity) {
        super(activity);
        homeMessageType = activity.getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getUserInfo(Consumer<CircleUserInfoEntity> consumer){
        submitRequestThrowError(CircleModel.getCircleInfo(userId, type).map(r -> {
            if(r.status){
                circleUserInfoEntity = r.data;
                circleUserInfoEntity.id = userId;
                return circleUserInfoEntity;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void setFollow(Consumer<String> consumer) {
        submitRequestThrowError(CircleModel.circleFollow(CpigeonData.getInstance().getUserId(getActivity()), userId, isFollow).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow ? 1 : 0;
    }

}
