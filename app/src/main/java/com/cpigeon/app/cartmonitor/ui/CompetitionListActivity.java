package com.cpigeon.app.cartmonitor.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.view.fragment.GeCheJianKongListFragment;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

/**
 * Created by chenshuai on 2017/11/7.
 */

public class CompetitionListActivity extends BaseActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    SmartTabLayout tabLayout;


    @Override
    public int getLayoutId() {
        return R.layout.layout_com_tab_viewpager;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SmartTabLayout) findViewById(R.id.viewpagertab);

        toolbar.setTitle("鸽车监控");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        Bundle bundleXiehui = new Bundle();
        Bundle bundleGonepeng = new Bundle();

        bundleXiehui.putInt(CompetitionListFragment.TYPE, CompetitionListFragment.TYPE_ASSOCIATION);
        bundleGonepeng.putInt(CompetitionListFragment.TYPE, CompetitionListFragment.TYPE_BOB);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("协会", GeCheJianKongListFragment.class, bundleXiehui)
                .add("公棚", GeCheJianKongListFragment.class, bundleGonepeng)
                .create());

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        //viewPager.setCurrentItem(SharedPreferencesTool.Get(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), 0, SharedPreferencesTool.SP_FILE_APPSTATE));
    }
}
