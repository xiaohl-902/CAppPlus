package com.cpigeon.app.modular.matchlive.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.view.fragment.MonitorEndMapLiveFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.MonitoringMapLiveFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.MapPhotoFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.MapVideoFragment;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;
import com.cpigeon.app.view.MyViewPager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MapLiveActivity extends BaseActivity {

    public static SmartTabLayout viewpagertab;
    public static MyViewPager viewPager;

    @BindView(R.id.title)
    MarqueeTextView title;
    private GeCheJianKongRace geCheJianKongRace;

    private double v1;
    private double v2;
    private double v3;
    private double v4;
    private double displacement;

    @Override
    public int getLayoutId() {
        return R.layout.activity_map_live_layout;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

        viewPager = findViewById(R.id.view_pager);
        viewpagertab = findViewById(R.id.viewpagertab);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        geCheJianKongRace = (GeCheJianKongRace) bundle.getSerializable("geCheJianKongRace");
        title.setText(geCheJianKongRace.getRaceName());
        toolbar.getMenu().clear();
        toolbar.getMenu().add("赛线天气")
                .setOnMenuItemClickListener(item -> {
                    startWeather();
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        toolbar.setNavigationOnClickListener(v -> finish());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                GSYVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        initFragments();

    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (MapVideoFragment.smallVideoHelper != null && MapVideoFragment.smallVideoHelper.backFromFull()) {
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void initFragments() {
        FragmentPagerItemAdapter adapter;
        if(StringValid.isStringValid(geCheJianKongRace.getMEndTime())){
            adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("路程回放", MonitorEndMapLiveFragment.class)
                    .add("路程照片", MapPhotoFragment.class)
                    .add("路程视频", MapVideoFragment.class)
                    .create());

        }else {
            adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("鸽车监控", MonitoringMapLiveFragment.class)
                    .add("路程照片", MapPhotoFragment.class)
                    .add("路程视频", MapVideoFragment.class)
                    .create());
        }


        viewPager.setAdapter(adapter);
        viewpagertab.setViewPager(viewPager);

    }


    public GeCheJianKongRace getGeCheJianKongRace() {
        return geCheJianKongRace;
    }

    private void startWeather() {
        if (v1 == 0 || v2 == 0 || v3 == 0 || v4 == 0) {
            DialogUtils.createErrorDialog(getActivity(), "没有设置司放地");
        }else {
            Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.putExtra("v1", v1);
            intent.putExtra("v2", v2);
            intent.putExtra("v3", CommonTool.Aj2GPSLocation(v3));
            intent.putExtra("v4", CommonTool.Aj2GPSLocation(v4));
            intent.putExtra("isShowEnd", true);
            intent.putExtra(IntentBuilder.KEY_TITLE, geCheJianKongRace.getRaceName());
            intent.putExtra(IntentBuilder.KEY_DATA, displacement);
            startActivity(intent);
        }

        /*IntentBuilder.Builder(getActivity(), WeatherActivity.class)
                .putExtra("v1", v1)
                .putExtra("v2", v2)
                .putExtra("v3", v3)
                .putExtra("v4", v4)
                .startActivity();*/
    }

    public void setPointAndDisplacement(List<Double> points, double displacement){
        v1 = points.get(0);
        v2 = points.get(1);
        v3 = points.get(2);
        v4 = points.get(3);
        this.displacement = displacement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
