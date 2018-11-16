package com.cpigeon.app.modular.matchlive.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.view.fragment.GeCheJianKongListFragment;
import com.cpigeon.app.modular.usercenter.view.fragment.MyFollowSubFragment;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/7/11.
 * 鸽车监控列表显示页面
 */

public class GeCheJianKongListActicity extends BaseActivity {
    //private final static String APP_STATE_KEY_VIEWPAGER_SELECT_INDEX = "com.cpigeon.app.modular.usercenter.view.activity.MyFollowActivity.SelectItemIndex.";
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
        setTitle("鸽车监控");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundleXiehui = new Bundle();
        Bundle bundleGonepeng = new Bundle();
        Bundle bundleGeren = new Bundle();

        bundleXiehui.putString(GeCheJianKongListFragment.KEY_TYPE, GeCheJianKongListFragment.TYPE_XIEHUI);
        bundleGonepeng.putString(GeCheJianKongListFragment.KEY_TYPE, GeCheJianKongListFragment.TYPE_GONGPENG);
        bundleGeren.putString(GeCheJianKongListFragment.KEY_TYPE, GeCheJianKongListFragment.TYPE_GEREN);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("协会", GeCheJianKongListFragment.class, bundleXiehui)
                .add("公棚", GeCheJianKongListFragment.class, bundleGonepeng)
                .add("个人", GeCheJianKongListFragment.class, bundleGeren)
                .create());

        viewPager.setAdapter(adapter);
        viewpagertab.setViewPager(viewPager);
        //viewPager.setCurrentItem(SharedPreferencesTool.Get(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), 0, SharedPreferencesTool.SP_FILE_APPSTATE));
    }

    @Override
    protected void onDestroy() {
        //SharedPreferencesTool.Save(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), viewPager.getCurrentItem(), SharedPreferencesTool.SP_FILE_APPSTATE);
        super.onDestroy();
    }
}
