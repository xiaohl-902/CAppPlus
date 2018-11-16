package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.guide.view.SignActivity;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserScoreSub3Fragment extends BaseFragment {
    @BindView(R.id.btn_goto_sign)
    Button btnGotoSign;

    @Override
    public void finishCreateView(Bundle state) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_score_s3;
    }

    @OnClick(R.id.btn_goto_sign)
    public void onViewClicked() {
        IntentBuilder.Builder(getActivity(), SignActivity.class).startActivity();
    }
}
