package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.usercenter.view.fragment.MyFollowSubFragment;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/4/20.
 * 我的关注
 */

public class MyFollowActivity extends BaseActivity {

    private final static String APP_STATE_KEY_VIEWPAGER_SELECT_INDEX = "com.cpigeon.app.modular.usercenter.view.activity.MyFollowActivity.SelectItemIndex.";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_view)
    SmartTabLayout viewpagertab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

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
        setTitle("我的关注");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundleRace = new Bundle();
        Bundle bundleXiehui = new Bundle();
        Bundle bundleGonepeng = new Bundle();

        bundleRace.putString(MyFollowSubFragment.KEY_FOLLOW_TYPE, MyFollowSubFragment.FOLLOW_TYPE_RACE);
        bundleXiehui.putString(MyFollowSubFragment.KEY_FOLLOW_TYPE, MyFollowSubFragment.FOLLOW_TYPE_XIEHUI);
        bundleGonepeng.putString(MyFollowSubFragment.KEY_FOLLOW_TYPE, MyFollowSubFragment.FOLLOW_TYPE_GONGPENG);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("关注公棚", MyFollowSubFragment.class, bundleGonepeng)
                .add("关注协会", MyFollowSubFragment.class, bundleXiehui)
                .add("关注比赛", MyFollowSubFragment.class, bundleRace)
                .create());

        viewPager.setAdapter(adapter);
        viewpagertab.setViewPager(viewPager);
        viewPager.setCurrentItem(SharedPreferencesTool.Get(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), 0, SharedPreferencesTool.SP_FILE_APPSTATE));
    }

    @Override
    protected void onDestroy() {
        SharedPreferencesTool.Save(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), viewPager.getCurrentItem(), SharedPreferencesTool.SP_FILE_APPSTATE);
        super.onDestroy();
    }
}
