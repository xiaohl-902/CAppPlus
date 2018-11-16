package com.cpigeon.app.message.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.message.adapter.MessageHistoryAdapter;
import com.cpigeon.app.message.ui.history.presenter.MessageHistoryPre;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class MessageHistoryFragment extends BaseMVPFragment<MessageHistoryPre>{

    RecyclerView recyclerView;
    MessageHistoryAdapter adapter;
    TextView date;
    TextView searchDate;
    TextView number;
    String dateString;

    int datePosition = 0;

    @Override
    protected MessageHistoryPre initPresenter() {
        return new MessageHistoryPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }

    private void initView() {

        dateString = DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY_MM);
        datePosition = Integer.parseInt(getMonth(dateString)) - 1;
        mPresenter.date =  Integer.parseInt(getMonth(dateString));
        setTitle("发送历史记录");
        initHeadView();

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecorationLine(recyclerView);
        adapter = new MessageHistoryAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, adapter.getItem(position))
                    .startParentActivity(getActivity(), MessageDetailsFragment.class);
        });
        bindData();
    }

    private void initHeadView() {

        date = findViewById(R.id.date);
        searchDate = findViewById(R.id.date2);
        number = findViewById(R.id.number);

        date.setText(dateString);

        searchDate.setText(getString(R.string.string_text_date_for_message_history
                ,getMonth(dateString)));


        findViewById(R.id.rl_date).setOnClickListener(v -> {
            showPicker();
        });
    }

    private void bindData() {
        showLoading();
        mPresenter.getMessageHistoryList(messageEntities -> {
            adapter.setNewData(messageEntities);
            number.setText(getString(R.string.string_text_message_addressee_number
                    ,String.valueOf(mPresenter.getSendMessageCountInDate())));
            hideLoading();

        });

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message_history_layout;
    }

    private void showPicker(){
        OptionPicker picker = new OptionPicker(getSupportActivity(), getDates());
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(datePosition);
        picker.setCycleDisable(true);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                date.setText(item);
                searchDate.setText(getString(R.string.string_text_date_for_message_history
                        ,getMonth(item)));
                mPresenter.date = Integer.parseInt(getMonth(item));
                bindData();
                datePosition = index;
            }
        });
        picker.show();
    }
    //以后转移到到presenter里面

    public List<String> getDates(){
        List<String> dateList = Lists.newArrayList();
        String year = DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY);
        for (int i = 1; i <= 12; i++) {
            dateList.add(year + "-" + i);
        }
        return dateList;
    }

    private String getMonth(String date){
        return StringUtil.getCutString(date, 5,date.length());
    }
}
