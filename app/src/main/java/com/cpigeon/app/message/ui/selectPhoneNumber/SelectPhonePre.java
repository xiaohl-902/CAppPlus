package com.cpigeon.app.message.ui.selectPhoneNumber;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class SelectPhonePre extends BasePresenter {

    int userId;
    int groupId;
    public String phoneString;

    public SelectPhonePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        groupId = activity.getIntent().getIntExtra(IntentBuilder.KEY_DATA,0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void putTelephone(Consumer<ApiResponse> consumer){
        submitRequestThrowError(SelectPhoneModel.getCommons(userId,groupId,phoneString).map(r -> {
            if(r.status){
                return r;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

}
