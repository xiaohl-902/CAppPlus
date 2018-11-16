package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.TrainingDataPre;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.KLineManager;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.LineTextView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.view.TrainingMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/18.
 */

public class TrainingDataActivity extends BaseActivity<TrainingDataPre> {

    public static final String MATCH_INFO = "MATCH_INFO";
    public static final String FOOD_ID = "FOOD_ID";
    public static final String FIRST_SPEED = "FIRST_SPEED";
    public static final String SPEED = "SPEED";
    public static final String RANK = "RANK";


    KLineManager kLineManager;
    LineChart chart;

    SmartTabLayout tabLayout;
    ViewPager viewPager;

    TextView food;


    ShareDialogFragment shareDialogFragment;
    LineChart shareChart;

    LineTextView time;
    LineTextView entity_name;
    LineTextView rank;
    LineTextView displacement;
    LineTextView pigeon_number;
    LineTextView food_id;
    TextView team_name;
    TextView search_number;

    LinearLayout share_k_line;

    public static void start(Activity activity, MatchInfo matchInfo, String foodId, String firstSpeed,String speed, String rank){
        IntentBuilder.Builder(activity, TrainingDataActivity.class)
                .putExtra(TrainingDataActivity.MATCH_INFO, matchInfo)
                .putExtra(TrainingDataActivity.FOOD_ID, foodId)
                .putExtra(TrainingDataActivity.FIRST_SPEED, firstSpeed)
                .putExtra(TrainingDataActivity.SPEED, speed)
                .putExtra(TrainingDataActivity.RANK, rank)
                .startActivity();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_training_data_layout;

    }

    @Override
    public TrainingDataPre initPresenter() {
        return new TrainingDataPre(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("训赛大数据");

        toolbar.getMenu().clear();
        toolbar.getMenu().add("分享").setOnMenuItemClickListener(item -> {
            share_k_line.setBackgroundColor(getResources().getColor(R.color.white));
            showLoading();
            shareDialogFragment = new ShareDialogFragment();
            shareDialogFragment.setTitle("中鸽网大数据");
            shareDialogFragment.setDescription("云数据分析，助鸽友选好鸽、买好鸽！");
            shareDialogFragment.setShareType(ShareDialogFragment.TYPE_URL);
            shareDialogFragment.setOnShareCallBackListener(new ShareDialogFragment.OnShareCallBackListener() {
                @Override
                public void onSuccess() {
                    share_k_line.setVisibility(View.GONE);
                    shareDialogFragment = null;
                }

                @Override
                public void onFail() {
                    share_k_line.setVisibility(View.GONE);
                    shareDialogFragment = null;
                }
            });
            Bitmap bitmap = BitmapUtils.getViewBitmap(share_k_line);
            String url = BitmapUtils.getBitmapFile(bitmap, "share_grade.jpg");
            mPresenter.uploadImage = url;
            mPresenter.uploadShareImage(s -> {
                share_k_line.setBackgroundColor(getResources().getColor(R.color.transparent));
                hideLoading();
                shareDialogFragment.setShareUrl(s);
                shareDialogFragment.show(getActivity().getFragmentManager(), "share");
            });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        chart = findViewById(R.id.line_chart);
        food = findViewById(R.id.food);
        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);
        search_number = findViewById(R.id.search_number);
        viewPager.setOffscreenPageLimit(2);
        initShareView();

        setLoadText("云数据分析中···");
        setProgressVisible(true);
        composite.add(RxUtils.delayed(2500, aLong -> {
            setProgressVisible(false);
        }));


        mPresenter.getTrainingData(msg -> {

            try {
                if (msg.isOk()){
                    hideLoading();
                    String foods = EncryptionTool.decryptAES(mPresenter.foodId);
                    LogUtil.print("foodid:  " + foods);
                    food.setText(foods);
                    initFragments();
                    if(mPresenter.isCache){
                        search_number.setVisibility(View.GONE);
                    }else {
                        search_number.setVisibility(View.VISIBLE);
                        search_number.setText(getString(R.string.history_grade_search_number, mPresenter.searchNumber));
                    }
                    try {
                        initChar(chart, true);
                        bindDataToShare();
                    } catch (Exception e) {
                        DialogUtils.createHintDialog(this, "很遗憾！没有找到您需要的数据，本次不扣次数~", sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            finish();
                        });
                    }
                }else {

                }
            } catch (Exception e) {
                error(e.getLocalizedMessage());
            }

        });
    }

    private void bindDataToShare() {
        initChar(shareChart, false);
        HistoryGradeInfo entity = mPresenter.lineData.get(0);

        time.setContent(entity.getBssj());
        entity_name.setContent(entity.getXmmc());
        rank.setContent(entity.getBsmc());
        pigeon_number.setContent(entity.getBsgm());
        food_id.setContent(EncryptionTool.decryptAES(mPresenter.foodId));
        displacement.setContent(String.valueOf(entity.getBskj()) + "KM");
        team_name.setText(mPresenter.matchInfo.getMc());

    }

    private void initShareView() {
        share_k_line = findViewById(R.id.share_k_line);
        shareChart = findViewById(R.id.share_line_chart);
        time = findViewById(R.id.time);
        entity_name = findViewById(R.id.entity_name);
        rank = findViewById(R.id.rank);
        displacement = findViewById(R.id.displacement);
        pigeon_number = findViewById(R.id.pigeon_number);
        food_id = findViewById(R.id.foot_id);
        team_name = findViewById(R.id.team_name);
    }

    private void initFragments() {

        Bundle list = new Bundle();
        list.putParcelableArrayList(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) mPresenter.lineData);

        Bundle photo = new Bundle();
        photo.putParcelableArrayList(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) mPresenter.images);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("数据分析", TrainingListFragment.class, list)
                .add("赛鸽照片", TrainingPhotoFragment.class, photo)
                .create());

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    private void initChar(LineChart chart , boolean isHaveAnimate){
        List<HistoryGradeInfo> data = mPresenter.lineData;

        TrainingMarkerView trainingMarkerView = new TrainingMarkerView(this, data);

        chart.setMarker(trainingMarkerView);

        kLineManager = new KLineManager(chart);
        //kLineManager.yLeft.setAxisMaximum(mPresenter.getMaxRank());
        kLineManager.yLeft.setDrawGridLines(false);
        //kLineManager.yRight.setAxisMaximum(mPresenter.getMaxFirstSpeed());
        kLineManager.yRight.setDrawGridLines(false);

        kLineManager.xAxis.setValueFormatter((v, axisBase) -> {
            if (v == 0) {
                return "0";
            } else {
                if (!data.isEmpty()) {
                    return data.get((int) (v - 1)).getXmmc();
                }else {
                    return "0";
                }
            }
        });

        if(isHaveAnimate){
            kLineManager.setAnimate();
        }


        List<Entry> data1 = Lists.newArrayList();
        List<Entry> data2 = Lists.newArrayList();
        List<Entry> data3 = Lists.newArrayList();

        if(!data.isEmpty()){
            for (int i = 0, len = data.size(); i < len; i++) {
                Entry entry = new Entry(i + 1, Float.valueOf(data.get(i).getBsmc()));
                Entry entry1 = new Entry(i + 1, Float.valueOf(data.get(i).getSpeed()));
                Entry entry2 = new Entry(i + 1, Float.valueOf(data.get(i).getFirstSpeed()));

                data1.add(entry);
                data2.add(entry1);
                data3.add(entry2);
            }
        }


        kLineManager.addLineData(data1, R.color.light_red, "比赛名次", YAxis.AxisDependency.LEFT);
        kLineManager.addLineData(data2, R.color.color_yellow_f49562, "分速", YAxis.AxisDependency.RIGHT);
        kLineManager.addLineData(data3, R.color.light_green, "第一名分速", YAxis.AxisDependency.RIGHT);

        kLineManager.setDataToChart();
    }

    @Override
    public void error(int code, String error) {
        if (code == 10005 || code == 10006) {
            IntentBuilder.Builder(getActivity(), OpenServiceActivity.class)
                    .putExtra(OpenServiceActivity.INTENT_DATA_KEY_SERVICENAME, "大数据查询")
                    .startActivity();
            finish();
            return;
        }
        super.error(code, error);
    }

    @Override
    public void error(String message) {
        hideLoading();
        if(!TextUtils.isEmpty(message)) {
            if(errorDialog == null || !errorDialog.isShowing()){
                errorDialog = DialogUtils.createHintDialog(getActivity(), message,sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    finish();
                });
            }
        }
    }
}
