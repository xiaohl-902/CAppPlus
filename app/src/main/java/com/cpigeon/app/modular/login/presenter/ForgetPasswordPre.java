package com.cpigeon.app.modular.login.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.login.LoginModel;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/6/4.
 */

public class ForgetPasswordPre extends BasePresenter{

    String phone;
    String password;
    String code;

    public ForgetPasswordPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getCode(Consumer<ApiResponse<String>> consumer){
        if(!StringValid.phoneNumberValid(phone)){
            error.onNext(getErrorString("请输入正确手机号码"));
            return;
        }
        submitRequestThrowError(LoginModel.getCode(phone, 2).map(r -> {
            return r;
        }),consumer);
    }

    public void findPassword(Consumer<ApiResponse<String>> consumer){
        if(!StringValid.phoneNumberValid(phone)){
            error.onNext(getErrorString("请输入正确手机号码"));
            return;
        }

        if(!StringValid.isStringValid(code)){
            error.onNext(getErrorString("请输入验证码"));
            return;
        }

        if(!StringValid.passwordValid(password)){
            error.onNext(getErrorString("密码需要大于6位"));
            return;
        }

        submitRequestThrowError(LoginModel.findPassword(phone,password,code),consumer);
    }

    public Consumer<String> setPhone(){
        return s -> {
            this.phone = s;
        };
    }

    public Consumer<String> setPassword(){
        return s -> {
            this.password = s;
        };
    }

    public Consumer<String> setCode(){
        return s -> {
            this.code = s;
        };
    }

}
