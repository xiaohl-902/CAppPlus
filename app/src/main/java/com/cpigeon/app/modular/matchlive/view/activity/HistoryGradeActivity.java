package com.cpigeon.app.modular.matchlive.view.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.HistoryGradePre;
import com.cpigeon.app.modular.matchlive.view.adapter.HistoryGradeNewAdapter;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.KLineManager;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.view.GradeMarkerView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import java.util.Collections;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/3.
 */

public class HistoryGradeActivity extends BaseActivity<HistoryGradePre> {

    RecyclerView recyclerView;
    HistoryGradeNewAdapter adapter;

    ShareDialogFragment shareDialogFragment;
    RelativeLayout share;

    public static final String FOOD_ID = "FOOD_ID";
    public static final String CARD_ID = "CARD_ID";
    public static final String RANK = "RANK";
    public static final String SPEED = "SPEED";
    public static final String DISTANCE = "DISTANCE";
    public static final String PIGEON_MASTER = "PIGEON_MASTER";

    public static void start(Activity activity, String foodId, String cardId, String type
            , MatchInfo matchInfo, int rank, Double speed, String pigeonMaster, double distance) {
        IntentBuilder.Builder(activity, HistoryGradeActivity.class)
                .putExtra(HistoryGradeActivity.FOOD_ID, foodId)
                .putExtra(HistoryGradeActivity.CARD_ID, cardId)
                .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                .putExtra(IntentBuilder.KEY_TYPE, type)
                .putExtra(HistoryGradeActivity.RANK, String.valueOf(rank))
                .putExtra(HistoryGradeActivity.SPEED, speed)
                .putExtra(HistoryGradeActivity.PIGEON_MASTER, pigeonMaster)
                .putExtra(HistoryGradeActivity.DISTANCE, distance)
                .startActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history_grade_layout;
    }

    @Override
    public HistoryGradePre initPresenter() {
        return new HistoryGradePre(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        toolbar.getMenu().clear();
        toolbar.getMenu().add("分享").setOnMenuItemClickListener(item -> {
            Bitmap bitmap = BitmapUtils.getViewBitmap(share);
            String url = BitmapUtils.getBitmapFile(bitmap, "share_grade.jpg");
            shareDialogFragment = new ShareDialogFragment();
            shareDialogFragment.setTitle("中鸽网大数据");
            shareDialogFragment.setDescription("云数据分析，助鸽友选好鸽、买好鸽！");
            shareDialogFragment.setShareType(ShareDialogFragment.TYPE_URL);
            shareDialogFragment.setOnShareCallBackListener(new ShareDialogFragment.OnShareCallBackListener() {
                @Override
                public void onSuccess() {
                    shareDialogFragment = null;
                }

                @Override
                public void onFail() {
                    shareDialogFragment = null;
                }
            });
            mPresenter.uploadImage = url;
            showLoading();
            mPresenter.uploadShareImage(s -> {
                hideLoading();
                shareDialogFragment.setShareUrl(s);
                shareDialogFragment.show(getActivity().getFragmentManager(), "share");
            });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        setTitle("足环" + mPresenter.foodId + "大数据");
        recyclerView = findViewById(R.id.list);
        share = findViewById(R.id.share);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HistoryGradeNewAdapter();
        adapter.setType("xh");
        adapter.bindToRecyclerView(recyclerView);

        initData();
    }


    private void initData() {
        setLoadText("云数据分析中···");
        setProgressVisible(true);
        composite.add(RxUtils.delayed(2500, aLong -> {
            setProgressVisible(false);
        }));
        mPresenter.getHistoryGrade(r -> {
            if (r.errorCode == 0) {
                mPresenter.isCacahe = r.isCache;
                mPresenter.searchNumber = r.data.tcsyts;

                if (r.data != null) {
                    mPresenter.data = r.data.racelist;
                }

                if (mPresenter.speed != 0) {
                    HistoryGradeInfo entity = new HistoryGradeInfo();
                    entity.setBsmc(mPresenter.lastRank);
                    entity.setBsgm(String.valueOf(mPresenter.matchInfo.getSlys()));
                    entity.setBssj(DateTool.format(DateTool.strToDate(mPresenter.matchInfo.getSt()).getTime(), DateTool.FORMAT_DATE));
                    entity.setFoot(mPresenter.foodId);
                    entity.setXmmc(mPresenter.matchInfo.getBsmc());
                    entity.setSpeed(String.valueOf(mPresenter.speed));
                    entity.setBskj(String.valueOf(mPresenter.distance));
                    mPresenter.data.add(mPresenter.data.size(), entity);
                }

                mPresenter.lineData.addAll(mPresenter.data);
                Collections.reverse(mPresenter.lineData);


                adapter.setNewData(mPresenter.lineData);

                if(mPresenter.lineData.isEmpty()){
                    DialogUtils.createHintDialog(getActivity(), "没有找到您需要的数据，本次查看不扣次数！", sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        finish();
                    });
                }else {
                    if (adapter.getFooterLayoutCount() == 0) {
                        View head = LayoutInflater.from(getActivity()).inflate(R.layout.item_history_grade_head_new_layout, null);
                        adapter.addHeaderView(initHead(head, mPresenter.data, true));
                    }
                    bindDataToShare();
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

    private void bindDataToShare() {
        initHead(share, mPresenter.data, false);
    }

    private View initHead(View head, List<HistoryGradeInfo> data, boolean isVisible) {
        if (isVisible) {
            head.setVisibility(View.VISIBLE);
        }
        LineChart lineChart = head.findViewById(R.id.k_line);
        TextView entity_name = head.findViewById(R.id.entity_name);
        TextView food_id = head.findViewById(R.id.food_id);
        TextView pigeon_master = head.findViewById(R.id.pigeon_master);
        TextView text1 = head.findViewById(R.id.text_1);
        TextView text2 = head.findViewById(R.id.text_2);
        TextView searchNumber = head.findViewById(R.id.search_number);

        text1.setText("当前名次");
        text2.setText("当前分速");

        if (mPresenter.isCacahe) {
            searchNumber.setVisibility(View.INVISIBLE);
        } else {
            searchNumber.setVisibility(View.VISIBLE);
            searchNumber.setText(getString(R.string.history_grade_search_number, mPresenter.searchNumber));
        }

        entity_name.setText(mPresenter.matchInfo.getMc());
        food_id.setText(mPresenter.foodId);
        pigeon_master.setText(getString(R.string.string_pigeon_master, mPresenter.pigeonMaster));

        GradeMarkerView markerView = new GradeMarkerView(getActivity(), data);

        lineChart.setMarker(markerView);

        KLineManager kLineManager = new KLineManager(lineChart);
        kLineManager.xAxis.setDrawGridLines(false);

        kLineManager.xAxis.setValueFormatter((v, axisBase) -> {
            if (v == 0) {
                return "0";
            } else {
                if (!data.isEmpty()) {
                    return data.get((int) (v - 1)).getBskj();
                } else {
                    return "0";
                }
            }
        });

        kLineManager.yLeft.setValueFormatter((v, axisBase) -> {
            if (v == 0) {
                return "0";
            } else {
                return String.valueOf(-v);
            }
        });

        List<Entry> rank = Lists.newArrayList();
        List<Entry> speed = Lists.newArrayList();
        rank.add(new Entry(0, 0));
        speed.add(new Entry(0, 0));
        for (int i = 0, len = data.size(); i < len; i++) {
            rank.add(new Entry(i + 1, -Float.valueOf(data.get(i).getBsmc())));
            speed.add(new Entry(i + 1, Float.valueOf(data.get(i).getSpeed())));
        }

        kLineManager.addLineData(rank, R.color.light_red_ff0000, "名次", YAxis.AxisDependency.LEFT);
        kLineManager.addLineData(speed, R.color.light_blue_005aff, "分速", YAxis.AxisDependency.RIGHT);
        kLineManager.setAnimate();
        kLineManager.setDataToChart();


        return head;

    }
}
