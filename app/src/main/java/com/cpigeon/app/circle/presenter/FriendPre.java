package com.cpigeon.app.circle.presenter;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.entity.CircleFriendEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class FriendPre extends BasePresenter{

    public int page = 1;
    public String type;
    int userId;
    public int followId;
    int isFollow = 0;

    public FriendPre(BaseFragment baseFragment) {
        super(baseFragment);
        userId = CpigeonData.getInstance().getUserId(baseFragment.getActivity());
        type = baseFragment.getArguments().getString(IntentBuilder.KEY_TYPE);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getFriends(Consumer<List<CircleFriendEntity>> consumer){
        submitRequestThrowError(CircleModel.fansAndFollowList(userId, type, page, 10).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void setFollow(Consumer<String> consumer){
        submitRequestThrowError(CircleModel.circleFollow(userId, followId, isFollow).map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void setIsFollow(boolean isFollow){
        this.isFollow = isFollow ? 1 : 0;
    }

}
