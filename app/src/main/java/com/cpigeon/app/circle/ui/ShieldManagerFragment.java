package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShieldManagerFragment extends BaseMVPFragment {



    SmartTabLayout tabLayout;
    ViewPager viewPager;

    int type;

    @Override
    protected int getLayoutResource() {
        return R.layout.item_tab_layout;
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

        setTitle("屏蔽管理");

        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);

        Bundle shield = new Bundle();
        Bundle blacklist = new Bundle();

        shield.putString(IntentBuilder.KEY_TYPE, ShieldFriendFragment.TYPE_SHIELD);
        blacklist.putString(IntentBuilder.KEY_TYPE, ShieldFriendFragment.TYPE_BLACKLIST);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("屏蔽用户", ShieldFriendFragment.class, shield)
                .add("拉黑用户", ShieldFriendFragment.class, blacklist)
                .add("屏蔽动态", ShieldDynamicFragment.class)
                .create());

        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);
    }
}
