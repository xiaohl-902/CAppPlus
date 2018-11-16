package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class CircleReportPre extends BasePresenter {

    int userId;
    public String content;
    int messageId;

    public CircleReportPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        messageId = activity.getIntent().getIntExtra(IntentBuilder.KEY_DATA, 0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void reportMessage(Consumer<String> consumer){
        submitRequestThrowError(CircleModel.addMessageReport(content, messageId).map(r -> {
            if(r.status){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public Consumer<String> setContent(){
        return s -> {
            content = s;
        };
    }

}
