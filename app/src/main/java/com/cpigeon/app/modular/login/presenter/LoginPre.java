package com.cpigeon.app.modular.login.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.login.LoginModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/6/1.
 */

public class LoginPre extends BasePresenter {

   public String userId;
    public String password;

    public LoginPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void loginIn(Consumer<ApiResponse<String>> consumer){
        submitRequestThrowError(LoginModel.login(String.valueOf(userId), password).map(r -> {
            return r;
        }),consumer);
    }

    public Consumer<String> setPassword(){
        return s -> {
          password = s;
        };
    }

    public Consumer<String> setUserId(){
        return s -> {
            userId = s;
        };
    }
}
