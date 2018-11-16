package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

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

public class CircleFriendFragment extends BaseMVPFragment {

    SmartTabLayout tabLayout;
    ViewPager viewPager;

    int position;

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

        position = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_DATA, 0);

        setTitle("好友");

        toolbar.getMenu().clear();
        toolbar.getMenu().add("屏蔽")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), ShieldManagerFragment.class);
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);

        Bundle fans = new Bundle();
        Bundle follow = new Bundle();

        fans.putString(IntentBuilder.KEY_TYPE, BaseShowFriendFragment.TYPE_FANS);
        follow.putString(IntentBuilder.KEY_TYPE, BaseShowFriendFragment.TYPE_FOLLOW);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("我的关注", BaseShowFriendFragment.class, follow)
                .add("我的粉丝", BaseShowFriendFragment.class, fans)
                .create());

        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(position);
    }
}
