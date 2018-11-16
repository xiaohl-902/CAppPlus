package com.cpigeon.app.circle.ui;

import android.os.Bundle;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.presenter.UserInfoPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.utils.ToastUtil;

/**
 * Created by Zhu TingYu on 2018/5/21.
 */

public class UserInfoFragment extends BaseMVPFragment<UserInfoPre> {
    @Override
    protected UserInfoPre initPresenter() {
        return  new UserInfoPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        ToastUtil.showShortToast(getActivity(), mPresenter.userId+"");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_info_layout;
    }
}
