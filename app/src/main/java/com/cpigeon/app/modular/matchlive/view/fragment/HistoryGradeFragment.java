package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.HistoryGradePre;
import com.cpigeon.app.modular.matchlive.view.adapter.HistoryGradeAdapter;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.FileTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.cpigeon.app.view.GradeMarkerView;
import com.cpigeon.app.view.LineTextView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.File;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/17.
 */

public class HistoryGradeFragment extends BaseMVPFragment<HistoryGradePre> {

    RecyclerView recyclerView;
    HistoryGradeAdapter adapter;

    public static final String FOOD_ID = "FOOD_ID";
    public static final String CARD_ID = "CARD_ID";
    public static final String RANK = "RANK";
    public static final String DISPLACEMENT = "DISPLACEMENT";

    LineChart listChart;
    LineChart shareChart;

    LineTextView time;
    LineTextView entity_name;
    LineTextView rank;
    LineTextView displacement;
    LineTextView pigeon_number;
    LineTextView food_id;

    TextView team_name;

    LinearLayout share_k_line;

    ShareDialogFragment shareDialogFragment;

    public static void start(Activity activity, String foodId, String cardId, String type, MatchInfo matchInfo, int rank) {
        IntentBuilder.Builder()
                .putExtra(HistoryGradeFragment.FOOD_ID, foodId)
                .putExtra(HistoryGradeFragment.CARD_ID, cardId)
                .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                .putExtra(IntentBuilder.KEY_TYPE, type)
                .putExtra(HistoryGradeFragment.RANK, String.valueOf(rank))
                .startParentActivity(activity, HistoryGradeFragment.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_with_k_line_to_share_layout;
    }

    @Override
    protected HistoryGradePre initPresenter() {
        return new HistoryGradePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("赛鸽大数据");

        toolbar.getMenu().clear();
        toolbar.getMenu().add("分享").setOnMenuItemClickListener(item -> {
            share_k_line.setBackgroundColor(getResources().getColor(R.color.white));
            Bitmap bitmap = BitmapUtils.getViewBitmap(share_k_line);
            String url = BitmapUtils.getBitmapFile(bitmap, "share_grade.jpg");
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
            mPresenter.uploadImage = url;
            showLoading("请稍后");
            mPresenter.uploadShareImage(s -> {
                share_k_line.setBackgroundColor(getResources().getColor(R.color.transparent));
                hideLoading();
                shareDialogFragment.setShareUrl(s);
                shareDialogFragment.show(getActivity().getFragmentManager(), "share");
            });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryGradeAdapter();
        adapter.bindToRecyclerView(recyclerView);

        initShareView();

        initData();

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

    private void bindDataToShare() {
        initChar(shareChart, mPresenter.data, false);
        HistoryGradeInfo entity = mPresenter.data.get(0);

        time.setContent(entity.getBssj());
        entity_name.setContent(entity.getXmmc());
        rank.setContent(entity.getBsmc());
        pigeon_number.setContent(entity.getBsgm());
        food_id.setContent(entity.getFoot());
        displacement.setContent(String.valueOf(entity.getBskj()) + "KM");

        team_name.setText(mPresenter.matchInfo.getMc());

    }

    private View initChar(LineChart chart, List<HistoryGradeInfo> data, boolean isHaveAnimate) {
        chart.setDragEnabled(false);
        chart.setDrawBorders(true);
        chart.setVisibleXRangeMaximum(4);
        GradeMarkerView gradeMarkerView = new GradeMarkerView(getContext(), data);
        chart.setMarker(gradeMarkerView);
        if(isHaveAnimate){
            chart.animateY(2000, Easing.EasingOption.EaseInElastic);
        }
        Description description = new Description();
        description.setText("cpigeon.com");
        chart.setDescription(description);

        XAxis xAxis = chart.getXAxis();
        YAxis right = chart.getAxisRight();
        YAxis left = chart.getAxisLeft();

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(4, false);
        xAxis.setValueFormatter((v, axisBase) -> {
            if (v == 0) {
                return "0";
            } else {
                return data.get((int) v - 1).getBskj() + "KM";
            }
        });
        left.setTextColor(getResources().getColor(R.color.colorGreen));
        //是否绘制Y轴网格线
        left.setDrawGridLines(false);
        //left.setAxisMaximum(mPresenter.getMaxRank());
        left.setGranularity(100f);

        right.setTextColor(getResources().getColor(R.color.colorGreen));
//        right.setValueFormatter((v, axisBase) -> {
//            if (v == 0) {
//                return "0";
//            } else {
//                return data.get((int) v - 1).getBskj() + "KM";
//            }
//        });

        right.setGranularity(100f);
        right.setDrawGridLines(false);
        //right.setAxisMaximum(mPresenter.getMaxDistance());

        List<Entry> entries = Lists.newArrayList();
        entries.add(new Entry(0, 0));
        for (int i = 0, len = data.size(); i < len; i++) {
            entries.add(new Entry(i + 1, Float.valueOf(data.get(i).getBsmc())));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "名次");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setColor(getActivity().getResources().getColor(R.color.colorButton_orange_focus));
        lineDataSet.setCircleColor(getActivity().getResources().getColor(R.color.colorButton_orange_focus));
        lineDataSet.setHighLightColor(getActivity().getResources().getColor(R.color.colorButton_orange_focus));
        lineDataSet.setValueTextColor(getActivity().getResources().getColor(R.color.colorButton_orange_focus));
        lineDataSet.setValueFormatter((v, entry, i, viewPortHandler) -> {
            return String.valueOf((int) v);
        });

        lineDataSet.setDrawCircleHole(true);
        //设置显示值的字体大小
        lineDataSet.setValueTextSize(12f);
        //线模式为圆滑曲线（默认折线）
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCircleHoleRadius(1.5f);
        lineDataSet.setCircleSize(3f);


        List<Entry> entries_disposten = Lists.newArrayList();
        entries_disposten.add(new Entry(0, 0));
        for (int i = 0, len = data.size(); i < len; i++) {
            entries_disposten.add(new Entry(i + 1, Float.valueOf(data.get(i).getBskj())));
        }
        LineDataSet lineDataSet1 = new LineDataSet(entries_disposten, "空距");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setCircleRadius(3f);
        lineDataSet1.setColor(getActivity().getResources().getColor(R.color.colorGreen));
        lineDataSet1.setCircleColor(getActivity().getResources().getColor(R.color.colorGreen));
        lineDataSet1.setHighLightColor(getActivity().getResources().getColor(R.color.colorGreen));
        lineDataSet1.setValueTextColor(getActivity().getResources().getColor(R.color.colorGreen));
        lineDataSet1.setValueFormatter((v, entry, i, viewPortHandler) -> {
            return String.valueOf((int) v);
        });

        lineDataSet1.setDrawCircleHole(true);
        //设置显示值的字体大小
        lineDataSet1.setValueTextSize(12f);
        //线模式为圆滑曲线（默认折线）
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet1.setCircleHoleRadius(1.5f);
        lineDataSet1.setCircleSize(3f);




        /*
       //设置曲线填充 lineDataSet.setDrawFilled(true);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);*/

        LineData lineData = new LineData(lineDataSet, lineDataSet1);
        chart.setData(lineData);
        return chart;
    }

    private void initData() {
        setLoadText("云数据分析中···");
        setProgressVisible(true);
        composite.add(RxUtils.delayed(2500, aLong -> {
            setProgressVisible(false);
        }));
        mPresenter.getHistoryGrade(r -> {
            List<HistoryGradeInfo> data = Lists.newArrayList();
            if (r.isOk()) {
                mPresenter.isCacahe = r.isCache;
                mPresenter.searchNumber = r.data.tcsyts;
                if (Integer.valueOf(mPresenter.lastRank) != 0) {
                    HistoryGradeInfo entity = new HistoryGradeInfo();
                    entity.setBsmc(mPresenter.lastRank);
                    entity.setBsgm(String.valueOf(mPresenter.matchInfo.getSlys()));
                    entity.setBssj(DateTool.format(DateTool.strToDate(mPresenter.matchInfo.getSt()).getTime(), DateTool.FORMAT_DATE));
                    entity.setFoot(mPresenter.foodId);
                    entity.setXmmc(mPresenter.matchInfo.getBsmc());
                    entity.setBskj(String.valueOf(mPresenter.matchInfo.getBskj()));
                    data.add(entity);
                }
                if (r.data != null) {
                    data.addAll(r.data.racelist);
                }
                mPresenter.data = data;
                adapter.setNewData(data);
                try {
                    if (adapter.getFooterLayoutCount() == 0) {
                        adapter.addHeaderView(initHead(data));
                    }
                    bindDataToShare();
                } catch (Exception e) {
                    DialogUtils.createHintDialog(getActivity(), "很遗憾！没有找到您需要的数据，本次不扣次数~", sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        finish();
                    });
                }
            } else {
                if (r.errorCode == 10005 || r.errorCode == 10006) {
                    IntentBuilder.Builder(getActivity(), OpenServiceActivity.class)
                            .putExtra(OpenServiceActivity.INTENT_DATA_KEY_SERVICENAME, "大数据查询")
                            .startActivity();
                    finish();
                    return;
                }
                DialogUtils.createHintDialog(getActivity(), r.msg, sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    finish();
                });
            }


        });
    }

    private View initHead(List<HistoryGradeInfo> data) {
        View view = LinearLayout.inflate(getContext(), R.layout.item_history_grade_head_layout, null);
        LineChart lineChart = findViewById(view, R.id.line_chart);
        TextView food = findViewById(view, R.id.food);
        TextView number = findViewById(view, R.id.number);
        food.setText(data.get(0).getFoot());
        if(mPresenter.isCacahe){
            number.setVisibility(View.GONE);
        }else {
            number.setVisibility(View.VISIBLE);
            number.setText(getString(R.string.history_grade_search_number, mPresenter.searchNumber));
        }
        initChar(lineChart, data, true);
        return view;
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
}
