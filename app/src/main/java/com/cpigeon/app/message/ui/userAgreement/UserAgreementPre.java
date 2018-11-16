package com.cpigeon.app.message.ui.userAgreement;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.databean.ApiResponse;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class UserAgreementPre extends BasePresenter {

    int userId;
    boolean agreementState;

    public UserAgreementPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        agreementState = activity.getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void setUserAgreement(Consumer<ApiResponse> consumer){
        submitRequestThrowError(UserAgreementModel.setUserAgreement(userId).map(r -> {
            return r;
        }),consumer);
    }

}
