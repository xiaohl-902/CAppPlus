package com.cpigeon.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Zhu TingYu on 2017/11/15.
 */

public class FragmentParentActivity extends BaseActivity {

    public static String KEY_FRAGMENT = "KEY_FRAGMENT";
    public static String KEY_IS_HAVE_TOOLBAR = "KEY_IS_HAVE_TOOLBAR";

    boolean isHaveToolbar = true;

    BaseFragment baseFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_with_not_toolbar_layout;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Class clz = (Class) getIntent().getSerializableExtra(KEY_FRAGMENT);
        isHaveToolbar = getIntent().getBooleanExtra(KEY_IS_HAVE_TOOLBAR, true);

//        String cls = clz.getName();
        String cls = clz.getCanonicalName();


        if(isHaveToolbar){
            setContentView(R.layout.activity_with_toolbar_layout);
        }else {
            setContentView(R.layout.activity_with_not_toolbar_layout);
        }

        Fragment fragment = Fragment.instantiate(getActivity(), cls);
        if (fragment instanceof BaseFragment)
            baseFragment = (BaseFragment) fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_holder, fragment, cls);
        ft.commitAllowingStateLoss();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(baseFragment!=null)
            baseFragment.onActivityResult(requestCode,resultCode,data);
    }
}
