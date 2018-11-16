package com.cpigeon.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cpigeon.app.broadcastreceiver.JPushBroadcastReceiver;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleHomeFragment;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.home.HomeNewFragment;
import com.cpigeon.app.modular.login.LoginActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.GeCheJianKongListActicity;
import com.cpigeon.app.modular.matchlive.view.fragment.MatchLiveFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.MatchLiveSubFragment;
import com.cpigeon.app.modular.usercenter.view.activity.MyFollowActivity;
import com.cpigeon.app.modular.usercenter.view.fragment.UserCenterFragment;
import com.cpigeon.app.pigeonnews.ui.NewsListFragment;
import com.cpigeon.app.pigeonnews.ui.PigeonNewsActivity;
import com.cpigeon.app.service.MainActivityService;
import com.cpigeon.app.service.databean.UseDevInfo;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.PermissonUtil;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.StatusBarTool;
import com.cpigeon.app.utils.UpdateManager;
import com.cpigeon.app.utils.app.ControlManager;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.MyViewPager;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.UMShareAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;

//@RuntimePermissions
public class

MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    public final static String APP_STATE_KEY_VIEWPAGER_SELECT_INDEX = "MainActivity.SelectItemIndex.";
    public final static String IS_OPEN_FROM_J_PUSH = "OPEN_FROM_J_PUSH";
    public final static String IS_ACTIVITY = "IS_ACTIVITY";
    public final static String ACTIVITY_NAME = "ACTIVITY_NAME";
    public final static String CIRCLE_DETAILS = "CIRCLE_DETAILS";
    public final static String EARTH_QUACK = "EARTH_QUACK";




    private static boolean isExit = false;
    @BindView(R.id.viewpager)
    MyViewPager mViewPager;
    public static BottomNavigationBar mBottomNavigationBar;
    private UpdateManager mUpdateManager;
    private ControlManager mControlManager;
    private OnMatchTypeChangeListener onMatchTypeChangeListener;
    private int lastTabIndex = 0;//当前页面索引

    //主页
    private HomeNewFragment homeFragment;
    //直播
    private MatchLiveFragment matchLiveFragment;
    //个人中心
    private UserCenterFragment userCenterFragment;
    //足环查询
    private CircleHomeFragment friendCircleFragment;
    //鸽友圈
    private List<Fragment> mFragments = new ArrayList<>();
    private ContentFragmentAdapter mContentFragmentAdapter;
    private BadgeItem numberBadgeItem;
    private int laseSelectedPosition = 0;
    private boolean mHasUpdata = false;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    public static ViewGroup videoFullContainer;

    RxPermissions permissions;
    MainActivityService mainActivityService;
    MainActivityService.OnDeviceLoginCheckListener onDeviceLoginCheckListener = new MainActivityService.OnDeviceLoginCheckListener() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        @Override
        public boolean onOtherDeviceLogin(final UseDevInfo useDevInfo) {
            if (useDevInfo == null) return false;

            Logger.d(useDevInfo.getString());

            clearLoginInfo();
            return true;
        }
    };
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(name);
            Logger.d(service);
            mainActivityService = ((MainActivityService.ThisBinder) service).getService();
            mainActivityService.setOnDeviceLoginCheckListener(onDeviceLoginCheckListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(name);
            mainActivityService = null;
        }
    };
    //赛事加载完毕之后的监听
    private MatchLiveSubFragment.OnRefreshListener onMatchInfoRefreshListener = new MatchLiveSubFragment.OnRefreshListener() {
        @Override
        public void onStartRefresh(MatchLiveSubFragment fragment) {
        }

        @Override
        public void onRefreshFinished(int type, List<MatchInfo> list) {
            //if (homeFragment != null) homeFragment.loadMatchInfo();
        }
    };

    public void setOnMatchTypeChangeListener(OnMatchTypeChangeListener onMatchTypeChangeListener) {
        this.onMatchTypeChangeListener = onMatchTypeChangeListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    public void checkUpData() {
        //更新检查
        mUpdateManager = new UpdateManager(mContext);
        mUpdateManager.setOnInstallAppListener(new UpdateManager.OnInstallAppListener() {
            @Override
            public void onInstallApp() {
                mHasUpdata = true;
            }
        });
        mUpdateManager.checkUpdate();
    }

    /**
     * 检查当前版本是否可用
     */
    public void checkAvailableVersion() {
        mControlManager = new ControlManager(mContext);
        mControlManager.checkIsAvailableVersion(new ControlManager.OnCheckedListener() {
            @Override
            public void onChecked(boolean isAvailableVersion) {
                if (!isAvailableVersion) {
                    SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("当前版本已停止服务\n请您到中鸽网下载最新版本.")
                            .setConfirmText("去中鸽网下载")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(Const.URL_APP_DOWNLOAD + "?autostart=false"));
                                    startActivity(intent);

                                    sweetAlertDialog.dismiss();
                                    AppManager.getAppManager().AppExit(mContext);
                                }
                            })
                            .setCancelText("退出程序")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppManager.getAppManager().AppExit(mContext);
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String type = intent.getStringExtra(IntentBuilder.KEY_TYPE);
        if(JPushBroadcastReceiver.TYPE_TODAY_RACE_COUNT.equals(type)){
            int selectIndex = SharedPreferencesTool.Get(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), 0, SharedPreferencesTool.SP_FILE_APPSTATE);
            setCurrIndex(selectIndex);
        }
    }

    private void requestPermissions() {
        PermissonUtil.getAppDetailSettingIntent(this);//权限检查（添加权限）
    }
    
    public void initView(Bundle savedInstanceState) {
        requestPermissions();

        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentBuilder.KEY_BOOLEAN, false);
        mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        homeFragment = new HomeNewFragment();
        matchLiveFragment = new MatchLiveFragment();
        matchLiveFragment.setOnRefreshListener(onMatchInfoRefreshListener);
        userCenterFragment = new UserCenterFragment();
        friendCircleFragment = new CircleHomeFragment();
//        mCpigeonGroupFragment = new CpigeonGroupFragment();
        mFragments = new ArrayList<>();
        homeFragment.setArguments(bundle);
        matchLiveFragment.setArguments(bundle);
        friendCircleFragment.setArguments(bundle);
        userCenterFragment.setArguments(bundle);

        mFragments.add(homeFragment);
        mFragments.add(matchLiveFragment);
//        mFragments.add(mCpigeonGroupFragment);
        mFragments.add(friendCircleFragment);
        mFragments.add(userCenterFragment);

        mContentFragmentAdapter = new ContentFragmentAdapter(getSupportFragmentManager(), mFragments);

        //设置limit
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setScanScroll(false);
        mViewPager.setAdapter(mContentFragmentAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GSYVideoPlayer.releaseAllVideos();
                if (lastTabIndex == position) return;
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setMode(MODE_FIXED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_home, "首页").setActiveColorResource(R.color.colorPrimary).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_live, "直播").setActiveColorResource(R.color.colorPrimary))
//                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_cpigeon_group).setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_circle, "鸽迷圈").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_my, "我的").setActiveColorResource(R.color.colorPrimary))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();*/
            mBottomNavigationBar.clearAnimation();
            mBottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_home_selected, "首页").setInactiveIconResource(R.drawable.ic_home_home).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_live_selected, "直播").setInactiveIconResource(R.drawable.ic_home_live).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_circle_selected, "鸽迷圈").setInactiveIconResource(R.drawable.ic_home_circle).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_my_selected, "我的").setInactiveIconResource(R.drawable.ic_home_my).setActiveColorResource(R.color.color_82b7e9))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();
        } else {
            mBottomNavigationBar.clearAnimation();
            mBottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_home_selected, "首页").setInactiveIconResource(R.drawable.ic_home_home).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_live_selected, "直播").setInactiveIconResource(R.drawable.ic_home_live).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_circle_selected, "鸽迷圈").setInactiveIconResource(R.drawable.ic_home_circle).setActiveColorResource(R.color.color_82b7e9))
                    .addItem(new BottomNavigationItem(R.drawable.ic_home_my_selected, "我的").setInactiveIconResource(R.drawable.ic_home_my).setActiveColorResource(R.color.color_82b7e9))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();
        }


        mBottomNavigationBar.setTabSelectedListener(this);
        if (!BuildConfig.DEBUG) {
            checkUpData();
            LogUtil.print("DEBUG: " + (!BuildConfig.DEBUG));
        }
        checkUpData();
        checkAvailableVersion();
        // startService(new Intent(mContext.getApplicationContext(), CoreService.class));
        bindService(new Intent(MyApp.getInstance(), MainActivityService.class), conn, Context.BIND_AUTO_CREATE);
        int selectIndex = SharedPreferencesTool.Get(mContext, APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(mContext), 0, SharedPreferencesTool.SP_FILE_APPSTATE);
        setCurrIndex(selectIndex);

        if (CpigeonData.getInstance().getUserInfo() == null) {
            CpigeonData.DataHelper.getInstance().updateUserInfo(null);
        }
        videoFullContainer = findViewById(R.id.videoFullContainer);

    }

    private void hintLogin() {
        /*if(dialogPrompt == null) {
            dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
            dialogPrompt.setCanceledOnTouchOutside(false);
            dialogPrompt.setCancelable(false);
            dialogPrompt.setTitleText("提示")
                    .setCancelText("取消")
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        setCurrIndex(0);
                        mBottomNavigationBar.selectTab(0);
                    })
                    .setConfirmClickListener(sweetAlertDialog -> {
                        IntentBuilder.Builder(this, LoginActivity.class).startActivity();
                    })
                    .setContentText("您还没有登录，请登录!")
                    .setConfirmText("确定");
        }

        if(!dialogPrompt.isShowing()){
            dialogPrompt.show();
        }*/

        try {
            if (laseSelectedPosition != 0) {
                mViewPager.setCurrentItem(0);
                mBottomNavigationBar.selectTab(0);
                laseSelectedPosition = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(LoginActivity.class);

    }

//


    @Override
    public void onTabSelected(int position) {
        laseSelectedPosition = position;
        if (!checkLogin() && laseSelectedPosition != 0) {
            hintLogin();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragment(transaction);
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeNewFragment();
                    transaction.add(R.id.viewpager, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }

                break;
            case 1:
                if (matchLiveFragment == null) {
                    matchLiveFragment = new MatchLiveFragment();
                    transaction.add(R.id.viewpager, matchLiveFragment);
                } else {
                    transaction.show(matchLiveFragment);
                }

                break;
            case 2:
                if (friendCircleFragment == null) {
                    friendCircleFragment = new CircleHomeFragment();
                    transaction.add(R.id.viewpager, friendCircleFragment);
                } else {
                    transaction.show(friendCircleFragment);
                }
                break;
            case 3:
                if (userCenterFragment == null) {
                    userCenterFragment = new UserCenterFragment();
                    transaction.add(R.id.viewpager, userCenterFragment);
                } else {
                    transaction.show(userCenterFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();
        setCurrIndex(position);
    }

    public void setCurrIndex(int index) {

        try {
            if (index == lastTabIndex) return;
            StatusBarTool.setWindowStatusBarColor(this, getResources().getColor(index == 4 ? R.color.user_center_header_top : R.color.colorPrimary));
            lastTabIndex = index;
            mViewPager.setCurrentItem(index);
            mBottomNavigationBar.selectTab(index);
            if (checkLogin()) {
                SharedPreferencesTool.Save(MyApp.getInstance(), APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(MyApp.getInstance()), index, SharedPreferencesTool.SP_FILE_APPSTATE);
            } else {
                SharedPreferencesTool.Save(MyApp.getInstance(), APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(MyApp.getInstance()), 0, SharedPreferencesTool.SP_FILE_APPSTATE);
            }
            onTabSelected(index);
        } catch (Throwable throwable) {

        }

    }

    public void setSelectIndex(int index) {
        setCurrIndex(index);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasUpdata) {
            AppManager.getAppManager().AppExit(mContext);
        }
        StatusBarTool.setWindowStatusBarColor(this, getResources().getColor(lastTabIndex == 3 ? R.color.user_center_header_top : R.color.colorPrimary));
        //MyApp.setJPushAlias();
    }


    /**
     * 比赛类型切换的监听器
     */
    public interface OnMatchTypeChangeListener {
        void onChanged(String lastType, String currType);
    }


    public void onHomeItemClick(View v) {
        switch (v.getId()) {
//            case R.id.layout_gcjk:
//                startActivity(GeCheJianKongListActicity.class);
//                break;
//            case R.id.layout_bszb:
//                if (onMatchTypeChangeListener != null)
//                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_XH);
//                setCurrIndex(1);
//                break;
            case R.id.layout_gpzb:
                startActivity(GeCheJianKongListActicity.class);
                break;
            case R.id.layout_xhzb:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_XH);
                setCurrIndex(1);
                mBottomNavigationBar.selectTab(1);
                break;
            case R.id.layout_zhcx:
                onTabReselected(2);
                setCurrIndex(2);
                break;
            case R.id.layout_wdsc:
                startActivity(new Intent(MainActivity.this, MyFollowActivity.class));
                break;
            case R.id.tv_raceinfo_gp_count:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_GP);

                setCurrIndex(1);
                break;
            case R.id.recyclerview_home_gp:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_GP);

                setCurrIndex(1);
                break;
            case R.id.tv_raceinfo_xh_count:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_XH);
                setCurrIndex(1);
                break;
            case R.id.recyclerview_home_xh:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_XH);
                setCurrIndex(1);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (BaseCircleMessageFragment.smallVideoHelper != null && BaseCircleMessageFragment.smallVideoHelper.backFromFull()) {
            return;
        }

        if (lastTabIndex != 0) {
            onTabReselected(0);
            setCurrIndex(0);
        } else {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Then_click_one_exit_procedure),
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                AppManager.getAppManager().AppExit(mContext);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}