package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.TrainingDataPre;
import com.cpigeon.app.modular.matchlive.view.adapter.HistoryGradeNewAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.TrainingImageAdapter;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.KLineManager;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.view.TrainingMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/4.
 */

public class TrainingDataNewActivity extends BaseActivity<TrainingDataPre> {

    public static final String MATCH_INFO = "MATCH_INFO";
    public static final String FOOD_ID = "FOOD_ID";
    public static final String FIRST_SPEED = "FIRST_SPEED";
    public static final String SPEED = "SPEED";
    public static final String RANK = "RANK";
    public static final String PIGEON_MASTER = "PIGEON_MASTER";


    RecyclerView recyclerView;
    TrainingImageAdapter imageAdapter;
    HistoryGradeNewAdapter historyGradeNewAdapter;
    ShareDialogFragment shareDialogFragment;
    RelativeLayout share;

    public static void start(Activity activity, MatchInfo matchInfo, String foodId, String firstSpeed, String speed, String rank, String pigeonMaster) {
        IntentBuilder.Builder(activity, TrainingDataNewActivity.class)
                .putExtra(TrainingDataNewActivity.MATCH_INFO, matchInfo)
                .putExtra(TrainingDataNewActivity.FOOD_ID, foodId)
                .putExtra(TrainingDataNewActivity.FIRST_SPEED, firstSpeed)
                .putExtra(TrainingDataNewActivity.SPEED, speed)
                .putExtra(TrainingDataNewActivity.RANK, rank)
                .putExtra(TrainingDataNewActivity.PIGEON_MASTER, pigeonMaster)
                .startActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_training_data_new_2_layout;
    }

    @Override
    public TrainingDataPre initPresenter() {
        return new TrainingDataPre(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        setTitle("足环" + EncryptionTool.decryptAES(mPresenter.foodId) + "大数据");

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

        recyclerView = findViewById(R.id.list);
        share = findViewById(R.id.share);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        imageAdapter = new TrainingImageAdapter();
        historyGradeNewAdapter = new HistoryGradeNewAdapter();
        historyGradeNewAdapter.setType("gp");


        recyclerView.setAdapter(historyGradeNewAdapter);

        setLoadText("云数据分析中···");
        setProgressVisible(true);
        composite.add(RxUtils.delayed(2500, aLong -> {
            setProgressVisible(false);
        }));

        mPresenter.getTrainingData(r -> {

            try {
                if (r.status) {

                    mPresenter.getData(r.getData());
                    mPresenter.isCache = r.isCache;

                    historyGradeNewAdapter.setNewData(mPresenter.reverseLineData);
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_history_grade_head_new_layout, null);
                    historyGradeNewAdapter.addHeaderView(initHead(view, true));
                    bindShareData();
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
            } catch (Exception e) {
                error(e.getLocalizedMessage());
            }

        });
    }

    private void bindShareData() {
        initHead(share, false);
    }

    private View initHead(View head, boolean isVisible) {

        LineChart lineChart = head.findViewById(R.id.k_line);
        TextView entity_name = head.findViewById(R.id.entity_name);
        TextView food_id = head.findViewById(R.id.food_id);
        TextView pigeon_master = head.findViewById(R.id.pigeon_master);
        TextView text1 = head.findViewById(R.id.text_1);
        TextView text2 = head.findViewById(R.id.text_2);
        TextView text3 = head.findViewById(R.id.text_3);
        TextView searchNumber = head.findViewById(R.id.search_number);
        TextView data_list = head.findViewById(R.id.data_list);
        TextView data_photo = head.findViewById(R.id.data_photo);

        LinearLayout info_ll_3 = head.findViewById(R.id.info_ll_3);
        LinearLayout ll_switch = head.findViewById(R.id.ll_switch);
        info_ll_3.setVisibility(View.VISIBLE);
        if (isVisible) {
            ll_switch.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
        } else {
            ll_switch.setVisibility(View.GONE);
        }

        data_list.setOnClickListener(v -> {
            recyclerView.setAdapter(historyGradeNewAdapter);
            historyGradeNewAdapter.setNewData(mPresenter.reverseLineData);
            if (historyGradeNewAdapter.getHeaderLayoutCount() == 0) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_history_grade_head_new_layout, null);
                historyGradeNewAdapter.addHeaderView(initHead(view, true));
            }
        });

        data_photo.setOnClickListener(v -> {
            if (!mPresenter.images.isEmpty()) {
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.setNewData(mPresenter.images);
                if (imageAdapter.getHeaderLayoutCount() == 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_history_grade_head_new_layout, null);
                    imageAdapter.addHeaderView(initHead(view, true));

                }
            } else {
                error("该公棚尚未拍照！");
            }
        });

        text1.setText("当前名次");
        text2.setText("当前分速");
        text3.setText("历次第一名");

        if (mPresenter.isCache) {
            searchNumber.setVisibility(View.INVISIBLE);
        } else {
            searchNumber.setVisibility(View.VISIBLE);
            searchNumber.setText(getString(R.string.history_grade_search_number, mPresenter.searchNumber));
        }

        entity_name.setText(mPresenter.matchInfo.getMc());
        food_id.setText(EncryptionTool.decryptAES(mPresenter.foodId));
        pigeon_master.setText(getString(R.string.string_pigeon_master, mPresenter.pigeonMaster));

        TrainingMarkerView markerView = new TrainingMarkerView(getActivity(), mPresenter.lineData);

        lineChart.setMarker(markerView);

        KLineManager kLineManager = new KLineManager(lineChart);
        kLineManager.xAxis.setDrawGridLines(false);

        kLineManager.xAxis.setValueFormatter((v, axisBase) -> {
            if (v == 0) {
                return "0";
            } else {
                if (!mPresenter.lineData.isEmpty()) {
                    return mPresenter.lineData.get((int) (v - 1)).getBskj();
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
        List<Entry> firstspeed = Lists.newArrayList();
        rank.add(new Entry(0, 0));
        speed.add(new Entry(0, 0));
        firstspeed.add(new Entry(0, 0));
        for (int i = 0, len = mPresenter.lineData.size(); i < len; i++) {
            rank.add(new Entry(i + 1, -Float.valueOf(mPresenter.lineData.get(i).getBsmc())));
            speed.add(new Entry(i + 1, Float.valueOf(mPresenter.lineData.get(i).getSpeed())));
            firstspeed.add(new Entry(i + 1, Float.valueOf(StringValid.isStringValid(mPresenter.lineData.get(i).getFirstSpeed())
                    ? mPresenter.lineData.get(i).getFirstSpeed() : "0")));
        }

        kLineManager.setXMaxNumber(rank.size());


        kLineManager.addLineData(rank, R.color.light_red_ff0000, "名次", YAxis.AxisDependency.LEFT);
        kLineManager.addLineData(speed, R.color.light_blue_005aff, "分速", YAxis.AxisDependency.RIGHT);
        kLineManager.addLineData(firstspeed, R.color.light_green_116237, "", YAxis.AxisDependency.RIGHT);
        kLineManager.setAnimate();
        kLineManager.setDataToChart();

        return head;

    }

}
