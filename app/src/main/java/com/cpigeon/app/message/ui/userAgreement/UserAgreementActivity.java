package com.cpigeon.app.message.ui.userAgreement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseWebViewActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class UserAgreementActivity extends BaseWebViewActivity<UserAgreementPre> {

    boolean agreeState;

    public static void startActivity(Activity activity, boolean agreeState){
        IntentBuilder.Builder(activity, UserAgreementActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, agreeState)
                .putExtra(IntentBuilder.KEY_TITLE, "用户协议")
                .putExtra(IntentBuilder.KEY_DATA, CPigeonApiUrl.getInstance().getServer()
                        + "/APP/Protocol?type=gxt").startActivity();
    }

    public static void startActivity(Activity activity, boolean agreeState, int requestCode){
        IntentBuilder.Builder(activity, UserAgreementActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, agreeState)
                .putExtra(IntentBuilder.KEY_TITLE, "用户协议")
                .putExtra(IntentBuilder.KEY_DATA, CPigeonApiUrl.getInstance().getServer()
                        + "/APP/Protocol?type=gxt").startActivity(activity, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_agreement_webview_layout;
    }

    @Override
    public UserAgreementPre initPresenter() {
        return new UserAgreementPre(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        agreeState = getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN,false);

        AppCompatCheckBox checkBox = findViewById(R.id.checkbox);
        TextView btn = findViewById(R.id.text_btn);

        if(agreeState){
            checkBox.setVisibility(View.GONE);
            btn.setOnClickListener(v -> {
                finish();
            });
        }else {
            btn.setOnClickListener(v -> {
                if(checkBox.isChecked()){
                    mPresenter.setUserAgreement(apiResponse -> {
                        if(apiResponse.status){
                            DialogUtils.createDialog(this,"提示",apiResponse.msg,"确定",sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(IntentBuilder.KEY_BOOLEAN, true);
                                getActivity().setResult(0,intent);
                                finish();
                            });
                        }else {
                            showTips(apiResponse.msg, TipType.DialogError);
                        }
                    });
                }else {
                    showTips("请点击同意", TipType.DialogError);
                }
            });
        }


    }
}
