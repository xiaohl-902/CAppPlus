package com.cpigeon.app.pigeonnews.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

/**
 * Created by Zhu TingYu on 2018/1/6.
 */

public class PigeonNewsActivity extends BaseActivity {

    SmartTabLayout tabLayout;
    ViewPager viewPager;
    int position;

    @Override
    public int getLayoutId() {
        return R.layout.activity_tab_layout;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        position = getIntent().getIntExtra(IntentBuilder.KEY_DATA,0);

        setTitle("中鸽快报");
        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);

        Bundle earth = new Bundle();
        earth.putInt(IntentBuilder.KEY_TYPE, PigeonMessageFragment.TYPE_EARTH_QUAKE);

        Bundle solar = new Bundle();
        solar.putInt(IntentBuilder.KEY_TYPE, PigeonMessageFragment.TYPE_SOLAR_STORM);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("新闻资讯", NewsListFragment.class)
                .add("地震信息", PigeonMessageFragment.class, earth)
                .add("太阳磁暴", PigeonMessageFragment.class, solar)
                .create());

        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(position);
    }
}
