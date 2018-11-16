package com.cpigeon.app.modular.login;

import android.os.Bundle;

import com.cpigeon.app.modular.login.presenter.ForgetPasswordPre;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;

/**
 * Created by Zhu TingYu on 2018/6/4.
 */

public class ForgetPasswordFragment extends BaseVerifyFragment<ForgetPasswordPre>{

    @Override
    protected ForgetPasswordPre initPresenter() {
        return new ForgetPasswordPre(getActivity());
    }


    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);

        setTitle("找回密码");

        bindUi(RxUtils.textChanges(etUserName), mPresenter.setPhone());
        bindUi(RxUtils.textChanges(edPassword), mPresenter.setPassword());
        bindUi(RxUtils.textChanges(etCode), mPresenter.setCode());

        btnCode.setOnClickListener(v -> {
            mPresenter.getCode(stringApiResponse -> {
                if(stringApiResponse.status){
                    customCountDownTimer.start();
                }else {
                    String msg = null;
                    switch ((int) stringApiResponse.errorCode) {
                        case -1:
                        case -2:
                            msg = "操作超时";
                            break;
                        case 1000:
                            msg = "手机号不能为空";
                            break;
                        case 1003:
                            msg = "手机号已被注册";
                            break;
                        case 1004:
                            msg = "手机号格式不正确";
                            break;
                        case 1005:
                            msg = "同一手机号每天最多获取两次";
                            break;
                        case 1008:
                            msg = "该手机号码未绑定账户";
                            break;
                        case 1009:
                            msg = "验证码已发送";
                            break;
                    }
                    error(msg);
                }
            });
        });

        tvOk.setText("找回密码");

        tvOk.setOnClickListener(v -> {
            if(!checkboxAgree.isChecked()){
                error("请阅读并同意中鸽网协议");
                return;
            }
            mPresenter.findPassword(stringApiResponse -> {
                if(stringApiResponse.status){
                    ToastUtil.showLongToast(getActivity(), "修改成功~");
                    finish();
                }else {
                    String msg = null;
                    if(stringApiResponse.errorCode == 1000){
                        msg = "不是已绑定的手机";
                    }else if(stringApiResponse.errorCode == 1003) {
                        msg = "手机验证码错误或失效";
                    }
                    error(msg);
                }
            });
        });

    }

}
