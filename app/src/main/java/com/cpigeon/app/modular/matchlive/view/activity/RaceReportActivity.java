package com.cpigeon.app.modular.matchlive.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.guide.view.SplashActivity;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.RaceReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.viewdao.IRaceReportView;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuBaoDaoFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuZhiDingFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.JiGeDataFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.RaceDetailsFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ReportDataFragment;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/10.
 */

public class RaceReportActivity extends BaseActivity<RaceReportPre> implements IRaceReportView, RaceDetailsFragment.DialogFragmentDataImpl {
    @BindView(R.id.race_details_marqueetv)
    MarqueeTextView raceDetailsMarqueetv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.race_report_smartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.list_header_race_detial_gg)
    MarqueeTextView listHeaderRaceDetialGg;
    @BindView(R.id.layout_gg)
    LinearLayout layoutGg;
    @BindView(R.id.race_report_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.menu_item_race)
    FloatingActionButton menuItemRace;
    @BindView(R.id.menu_item_org)
    FloatingActionButton menuItemOrg;
    @BindView(R.id.menu)
    FloatingActionMenu menu;
    @BindView(R.id.menu_item_gyt)
    FloatingActionButton menuItemGyt;
    @BindView(R.id.menu_item_weather)
    FloatingActionButton menuItemWeather;

    ///////////////////////////////////////////////////////////////////////////
    // 视图
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // 适配器和数据
    ///////////////////////////////////////////////////////////////////////////
    private MatchInfo matchInfo;//赛事信息
    private Bundle bundle;
    private Intent intent;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private GeCheJianKongRace defaultgeCheJianKongRace;
    private LocationManager locationManager;

    private LatLng currentLocation;

    public Bulletin getBulletin() {
        return bulletin;
    }

    private Bulletin bulletin;
    private String loadType;
    private String tablayout_seconde_name;

    private boolean isGps = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_race_details;
    }

    @Override
    public RaceReportPre initPresenter() {
        return new RaceReportPre(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mPresenter.userId = CpigeonData.getInstance().getUserId(getActivity());

        locationManager = new LocationManager(getActivity());

        intent = this.getIntent();
        bundle = intent.getExtras();
        matchInfo = (MatchInfo) bundle.getSerializable("matchinfo");
        loadType = bundle.getString("loadType");
        if (TextUtils.isEmpty(loadType)) {
            if (matchInfo != null) {
                loadType = matchInfo.getLx();
            }
        }

//        Logger.e("当前页面加载的数据是：" + loadType + "类型的数据");
        if ("xh".equals(loadType)) {
            mPresenter.matchId = String.valueOf(matchInfo.getId());
            tablayout_seconde_name = "集鸽数据";
        } else if ("gp".equals(loadType)) {
            mPresenter.matchId = matchInfo.getSsid();
            tablayout_seconde_name = "上笼清单";
        }
        initBulletin();
        mPresenter.addRaceClickCount();
        if (matchInfo != null) {
//            Logger.e("matchinfo" + matchInfo.getBsmc());
            if (bundle.getString("jigesuccess") != null) {
                mFragmentPagerAdapter = new FragmentPagerItemAdapter(
                        getSupportFragmentManager(), FragmentPagerItems.with(this)
                        .add(tablayout_seconde_name, JiGeDataFragment.class)
                        .add("插组指定", ChaZuZhiDingFragment.class)
                        .create());
            } else {
                mFragmentPagerAdapter = new FragmentPagerItemAdapter(
                        getSupportFragmentManager(), FragmentPagerItems.with(this)
                        .add("报到数据", ReportDataFragment.class)
                        .add("插组报到", ChaZuBaoDaoFragment.class)
                        .add(tablayout_seconde_name, JiGeDataFragment.class)
                        .add("插组指定", ChaZuZhiDingFragment.class)
                        .create());
            }

            mViewPager.setAdapter(mFragmentPagerAdapter);
            mSmartTabLayout.setViewPager(mViewPager);
        }
        if (mToolbar != null) {
            mToolbar.setTitle("");
            raceDetailsMarqueetv.setText(matchInfo.getMc());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initBoomMnue();

        if ("xh".equals(loadType)) {
            mPresenter.getFirstByAssociationLocation(aBoolean -> {
                if (aBoolean) {
                    currentLocation = mPresenter.backLocation;
                    menuItemWeather.setEnabled(true);
                } else {
                    locationManager.setLocationListener(aMapLocation -> {
                        menuItemWeather.setEnabled(true);
                        currentLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        isGps = true;
                    }).star();
                }
            });
        } else if ("gp".equals(loadType)) {
            mPresenter.getFirstByLoftLocation(aBoolean -> {
                if (aBoolean) {
                    currentLocation = mPresenter.backLocation;
                    menuItemWeather.setEnabled(true);
                } else {
                    locationManager.setLocationListener(aMapLocation -> {
                        menuItemWeather.setEnabled(true);
                        currentLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        isGps = true;
                    }).star();
                }
            });
        }


    }

    public void initBulletin() {
        mPresenter.showBulletin();
    }

    private void initBoomMnue() {
        menu.setClosedOnTouchOutside(true);
        if (Build.VERSION.SDK_INT < 21) {
            menuItemGyt.setImageResource(R.mipmap.ic_truck_white);
        } else {
            menuItemGyt.setImageResource(R.drawable.ic_svg_truck_white);
        }
        menuItemGyt.setColorNormalResId(R.color.colorButton_primary_normal);
        menuItemGyt.setColorPressedResId(R.color.colorButton_primary_focus);
        menuItemGyt.setColorDisabledResId(R.color.colorButton_Default_disable);

        menuItemOrg.setColorNormalResId(R.color.colorButton_orange_normal);
        menuItemOrg.setColorPressedResId(R.color.colorButton_orange_focus);
        menuItemOrg.setColorDisabledResId(R.color.colorButton_Default_disable);

        menuItemRace.setColorNormalResId(R.color.colorButton_Default_normal);
        menuItemRace.setColorNormalResId(R.color.colorButton_Default_focus);
        menuItemRace.setColorDisabledResId(R.color.colorButton_Default_disable);


        menuItemWeather.setColorNormalResId(R.color.colorPrimary);
        menuItemWeather.setColorPressedResId(R.color.colorPrimaryDark);
        menuItemWeather.setColorDisabledResId(R.color.colorButton_Default_disable);

        mPresenter.getDefaultGCJKInfo();
        refreshMenu();
    }

    /**
     * 刷新菜单显示内容
     */
    private void refreshMenu() {
        boolean _isJg = "jg".equals(matchInfo.getDt());
        //加载数据
        DbManager db = x.getDb(CpigeonConfig.getDataDb());
        UserFollow userFollow = null;
        try {
            userFollow = db.selector(UserFollow.class)
                    .where("uid", "=", CpigeonData.getInstance().getUserId(this))
                    .and("ftype", "=", matchInfo.getLx().equals("xh") ? "协会" : "公棚")
                    .and("rela", "=", EncryptionTool.encryptAES(EncryptionTool.decryptAES(matchInfo.getSsid()).split("/")[0]))
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        menuItemOrg.setLabelText((userFollow != null ? "取消关注" : "关注") + (matchInfo.getLx().equals("xh") ? "协会" : "公棚"));
        if (Build.VERSION.SDK_INT < 21) {
            menuItemOrg.setImageResource(userFollow != null ? R.mipmap.ic_favorite_white : R.mipmap.ic_favorite_white_border);
            menuItemOrg.setCropToPadding(true);
        } else {
            menuItemOrg.setImageResource(userFollow != null ? R.drawable.ic_svg_favorite_white_24dp : R.drawable.ic_svg_favorite_border_white_24dp);
        }

        menuItemOrg.setTag(userFollow);

        if (!_isJg) {
            try {
                userFollow = null;
                userFollow = db.selector(UserFollow.class)
                        .where("uid", "=", CpigeonData.getInstance().getUserId(this))
                        .and("ftype", "=", "比赛")
                        .and("rela", "=", matchInfo.getSsid())
                        .findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
            menuItemRace.setLabelText(userFollow != null ? "取消关注比赛" : "关注比赛");
            if (Build.VERSION.SDK_INT < 21) {
                Drawable drawable = this.getResources().getDrawable(userFollow != null ? R.mipmap.ic_favorite_white : R.mipmap.ic_favorite_white_border);
                menuItemRace.setImageDrawable(drawable);
            } else {
                menuItemRace.setImageResource(userFollow != null ? R.drawable.ic_svg_favorite_white_24dp : R.drawable.ic_svg_favorite_border_white_24dp);
            }
            menuItemRace.setTag(userFollow);
        } else {
            menuItemWeather.setVisibility(View.GONE);
            menuItemRace.setVisibility(View.GONE);
        }
        menuItemWeather.setImageResource(R.drawable.ic_sun);
        if (currentLocation == null) {
            menuItemWeather.setEnabled(false);
        }
    }

    public String getLoadType() {
        return loadType;
    }

    @Override
    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    @Override
    public String getSsid() {
        try {
            return matchInfo.getSsid();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getLx() {
        try {
            return matchInfo.getLx();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void showBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        if (bulletin != null && !TextUtils.isEmpty(bulletin.getContent().trim())) {
            layoutGg.setVisibility(View.VISIBLE);
            listHeaderRaceDetialGg.setText("公告:" + bulletin.getContent());
        } else {
            layoutGg.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDefaultGCJKInfo(GeCheJianKongRace geCheJianKongRace) {
        menuItemGyt.setLabelText(geCheJianKongRace != null ? "鸽车监控" : "未开启鸽车监控");
        menuItemGyt.setEnabled(geCheJianKongRace != null);
        this.defaultgeCheJianKongRace = geCheJianKongRace;
    }

    @Override
    public void refreshBoomMnue() {
        Logger.d("刷新Menu");
        this.refreshMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_race_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //android.R.id.home是Android内置home按钮的id
                finish();
                break;
            case R.id.action_save:

                break;
            case R.id.action_details:
                showDialogFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String msg) {

    }

    public void showDialogFragment() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null) {
            mFragmentTransaction.remove(fragment);
        }
        RaceDetailsFragment detailsFragment = RaceDetailsFragment.newInstance("直播数据");
        detailsFragment.show(mFragmentTransaction, "dialogFragment");

    }

    @Override
    protected void onDestroy() {
        Activity activity = AppManager.getAppManager().getActivityByClass(MainActivity.class);
        if (activity == null) {
            Intent mIntent = new Intent(this, SplashActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
        super.onDestroy();
    }

    @OnClick({R.id.layout_gg, R.id.menu_item_org, R.id.menu_item_race, R.id.menu_item_gyt, R.id.menu_item_weather})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_gg:
                if (bulletin == null || TextUtils.isEmpty(bulletin.getContent())) {
                    return;
                }
                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("公告");
                dialog.setContentText(bulletin.getContent());
                dialog.setCancelable(true);
                dialog.show();
                break;
            case R.id.menu_item_org:
                UserFollow tag = (UserFollow) view.getTag();
                if (tag != null) {
                    mPresenter.removeFollow(tag);
                    menu.close(true);
                    return;
                }
                mPresenter.addRaceOrgFollow();
                menu.close(true);
                break;
            case R.id.menu_item_race:
                UserFollow tag1 = (UserFollow) view.getTag();
                if (tag1 != null) {
                    mPresenter.removeFollow(tag1);
                    menu.close(true);
                    return;
                }
                mPresenter.addRaceFollow();
                menu.close(true);
                break;
            case R.id.menu_item_gyt:

                if (this.defaultgeCheJianKongRace != null) {
                    Intent intent = new Intent(this, MapLiveActivity.class);
                    intent.putExtra("geCheJianKongRace", defaultgeCheJianKongRace);
                    Logger.e(defaultgeCheJianKongRace.getId() + "：iD");
                    startActivity(intent);
                } else {
                    showTips("暂时没有该赛事信息", TipType.DialogError);
                }
                menu.close(true);
                break;

            case R.id.menu_item_weather:
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.putExtra("v1", CommonTool.Aj2GPSLocation(Double.parseDouble(matchInfo.getSfwd())));
                intent.putExtra("v2", CommonTool.Aj2GPSLocation(Double.parseDouble(matchInfo.getSfjd())));
                if (isGps) {
                    intent.putExtra("v3", currentLocation.latitude);
                    intent.putExtra("v4", currentLocation.longitude);
                } else {
                    intent.putExtra("v3", CommonTool.Aj2GPSLocation(currentLocation.latitude));
                    intent.putExtra("v4", CommonTool.Aj2GPSLocation(currentLocation.longitude));
                }
                intent.putExtra("isShowEnd", false);
                intent.putExtra(IntentBuilder.KEY_DATA, matchInfo.getBskj());
                intent.putExtra(IntentBuilder.KEY_TITLE, matchInfo.getMc());
                startActivity(intent);
                menu.close(true);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if(menu.isOpened()){
            menu.close(true);
        }else {
            super.onBackPressed();
        }
    }
}
