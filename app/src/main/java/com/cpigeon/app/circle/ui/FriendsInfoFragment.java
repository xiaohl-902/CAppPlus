package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleUserInfoEntity;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.StringValid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Zhu TingYu on 2018/5/24.
 */

public class FriendsInfoFragment extends BaseMVPFragment {
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.phoneNumber)
    TextView phoneNumber;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.information)
    TextView information;

    CircleUserInfoEntity data;



    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_friends_info_layout;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        data = getArguments().getParcelable(IntentBuilder.KEY_DATA);

        nickName.setText(data.nickname);
        phoneNumber.setText(data.sjhm);
        area.setText(data.diqu);
        sex.setText(data.sex);
        birthday.setText(data.birth);
        information.setText(StringValid.isStringValid(data.intro) ? data.intro : "这人太懒了没有简介");
    }
}
