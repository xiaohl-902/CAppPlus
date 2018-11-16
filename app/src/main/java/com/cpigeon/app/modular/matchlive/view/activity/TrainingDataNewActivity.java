package com.cpigeon.app.modular.matchlive.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.adapter.HistoryGradeNewAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.TrainingDataActivity;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/5/3.
 */

public class TrainingDataNewActivity extends BaseActivity {
    RecyclerView recyclerView;
    HistoryGradeNewAdapter adapter;

    public static void start(Activity activity, MatchInfo matchInfo, String foodId, String firstSpeed, String speed, String rank){
        IntentBuilder.Builder(activity, TrainingDataNewActivity.class)
                .putExtra(TrainingDataActivity.MATCH_INFO, matchInfo)
                .putExtra(TrainingDataActivity.FOOD_ID, foodId)
                .putExtra(TrainingDataActivity.FIRST_SPEED, firstSpeed)
                .putExtra(TrainingDataActivity.SPEED, speed)
                .putExtra(TrainingDataActivity.RANK, rank)
                .startActivity();
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_training_data_new_layout;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HistoryGradeNewAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setNewData(Lists.newArrayList(new HistoryGradeInfo(), new HistoryGradeInfo(), new HistoryGradeInfo(), new HistoryGradeInfo(), new HistoryGradeInfo()));
    }
}
