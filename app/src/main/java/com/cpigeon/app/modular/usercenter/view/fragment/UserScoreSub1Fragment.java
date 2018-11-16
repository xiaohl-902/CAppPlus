package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserScoreSub1Fragment extends BaseFragment {
    @BindView(R.id.btn_zhcx_dh)
    Button btnZhcxDh;


    @Override
    public void finishCreateView(Bundle state) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_score_s1;
    }

    @OnClick(R.id.btn_zhcx_dh)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), OpenServiceActivity.class);
        intent.putExtra(OpenServiceActivity.INTENT_DATA_KEY_SERVICENAME, "足环查询服务");
        startActivity(intent);
    }
}
