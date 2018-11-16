package com.cpigeon.app.modular.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CustomCountDownTimer;

import butterknife.BindView;

/**
 * Created by Zhu TingYu on 2018/6/4.
 */

public abstract class BaseVerifyFragment<P extends BasePresenter> extends BaseMVPFragment<P> {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btnCode)
    TextView btnCode;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.rlCode)
    RelativeLayout rlCode;
    @BindView(R.id.edPassword)
    EditText edPassword;
    @BindView(R.id.tvOk)
    TextView tvOk;
    @BindView(R.id.checkboxAgree)
    AppCompatCheckBox checkboxAgree;
    @BindView(R.id.tvAgreement)
    TextView tvAgreement;

    protected CustomCountDownTimer customCountDownTimer;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_phone_verify_layout;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        customCountDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 300000, 1000);

        tvAgreement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            String url = CPigeonApiUrl.getInstance().getServer() + "/APP/Protocol?type=regist";
            intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, url);
            intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "服务");
            startActivity(intent);
        });
    }

}
