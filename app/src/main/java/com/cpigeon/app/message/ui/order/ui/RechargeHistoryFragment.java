package com.cpigeon.app.message.ui.order.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.message.ui.order.adpter.RechargeHistoryAdapter;
import com.cpigeon.app.message.ui.order.ui.presenter.RechargeHistoryPre;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.LogUtil;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class RechargeHistoryFragment extends BaseMVPFragment<RechargeHistoryPre> {

    private static final String TYPE_START_TIME = "TYPE_START_TIME";
    private static final String TYPE_END_TIME = "TYPE_END_TIME";

    RecyclerView recyclerView;
    RechargeHistoryAdapter adapter;

    TextView tvDateLeft;
    TextView tvDataRight;

    private int cStartY = 2017;
    private int cStartM = 1;
    private int cStartD = 1;

    private int rangeEndY;
    private int rangeEndM;
    private int rangeEndD;

    private int cEndY;
    private int cEndM;
    private int cEndD;


    @Override
    protected RechargeHistoryPre initPresenter() {
        return new RechargeHistoryPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        rangeEndY = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));
        rangeEndM = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_MM));
        rangeEndD = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DD));

        cEndY = rangeEndY;
        cEndM = rangeEndM;
        cEndD = rangeEndD;

        setTitle("充值记录");
        initView();
    }

    private void initView() {
        tvDateLeft = findViewById(R.id.date_left);
        tvDataRight = findViewById(R.id.date_right);

        tvDateLeft.setText("2017-01-01");
        tvDataRight.setText(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATE));

        mPresenter.startTime = "2017-1-1";
        mPresenter.endTime = DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATE);


        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RechargeHistoryAdapter();
        adapter.bindToRecyclerView(recyclerView);

        tvDateLeft.setOnClickListener(v -> {
            showTimePicker(tvDateLeft, TYPE_START_TIME);
        });

        tvDataRight.setOnClickListener(v -> {
            showTimePicker(tvDataRight, TYPE_END_TIME);
        });

        bindData();
    }

    private void bindData() {
        showLoading();
        mPresenter.getHistory(data -> {
            hideLoading();
            adapter.setNewData(data);
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recharge_history_layout;
    }

    public void showTimePicker(TextView textView, String type) {

        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(getContext(), 10));
        picker.setRangeStart(2017, 1, 1);
        picker.setRangeEnd(rangeEndY, rangeEndM, rangeEndD);
        if(type.equals(TYPE_START_TIME)){
            picker.setSelectedItem(cStartY, cStartM, cStartD);
        }else {
            picker.setSelectedItem(cEndY, cEndM, cEndD);
        }
        picker.setResetWhileWheel(false);
        picker.setTopLineColor(getResources().getColor(R.color.colorPrimary));
        picker.setLabelTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setDividerColor(getResources().getColor(R.color.colorPrimary));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String time = year + "-" + month + "-" + day;
                textView.setText(time);
                if (TYPE_START_TIME.equals(type)) {
                    cStartY = Integer.parseInt(year);
                    cStartM = Integer.parseInt(month);
                    cStartD = Integer.parseInt(day);
                    mPresenter.startTime = time;
                } else {
                    cEndY = Integer.parseInt(year);
                    cEndM = Integer.parseInt(month);
                    cEndD = Integer.parseInt(day);
                    mPresenter.endTime = time;
                    LogUtil.print(time);
                }
                if(mPresenter.timeValid()){
                    bindData();
                }else {
                    error("开始时间比结束时间大");
                }
            }
        });
        picker.show();
    }
}
