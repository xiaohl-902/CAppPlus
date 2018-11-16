package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * Created by chenshuai on 2017/6/22.
 */

public class FeedBackResultDetialActivity extends BaseActivity {
    public static final String INTENT_KEY_FEEDBACKRESULT = "FeedBackResult";
    FeedBackResult feedBackResult;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etv_feedback_content)
    ExpandableTextView etvFeedbackContent;
    @BindView(R.id.etv_feedback_result)
    ExpandableTextView etvFeedbackResult;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback_result_detial;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        toolbar.setTitle("回复详情");
        feedBackResult = (FeedBackResult) intent.getSerializableExtra(INTENT_KEY_FEEDBACKRESULT);
        if (feedBackResult == null) finish();
        etvFeedbackContent.setText(feedBackResult.getContent());
        etvFeedbackResult.setText("已回复".equals(feedBackResult.getState()) ? feedBackResult.getResult() : feedBackResult.getState());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
