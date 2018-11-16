package com.cpigeon.app.modular.footsearch.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.footsearch.presenter.FootSearchHistoryPre;
import com.cpigeon.app.modular.footsearch.ui.adapter.FootSearchAdapter;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.ArrayList;

/**
 * Created by Zhu TingYu on 2017/12/21.
 */

public class FootSearchHistoryFragment extends BaseMVPFragment<FootSearchHistoryPre>{

    RecyclerView recyclerView;
    FootSearchAdapter adapter;
    RelativeLayout btn;

    @Override
    protected FootSearchHistoryPre initPresenter() {
        return new FootSearchHistoryPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_foot_search_help_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("足环查询");

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecorationLine(recyclerView);
        adapter = new FootSearchAdapter();
        btn = findViewById(R.id.rl1);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(v -> {
            DialogUtils.createDialogWithLeft(getActivity(),"是否要清空历史记录？",sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                showLoading();
                mPresenter.cleanHistory(s -> {
                    hideLoading();
                    adapter.getData().clear();
                    adapter.notifyDataSetChanged();
                    adapter.setEmptyView();
                });
            });
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            showLoading();
            mPresenter.id = adapter.getItem(position).id;
            mPresenter.getHistoryDetails(data -> {
                hideLoading();
                IntentBuilder.Builder()
                        .putParcelableArrayListExtra(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) data)
                        .startParentActivity(getActivity(), FootSearchResultFragment.class);
            });
        });

        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page ++;
            mPresenter.getHistory(data -> {
                if(data.isEmpty()){
                    adapter.setLoadMore(true);
                }else {
                    adapter.setLoadMore(false);
                    adapter.addData(data);
                }
            });
        },recyclerView);

        recyclerView.setAdapter(adapter);


        initTopImg();
        showLoading();
        mPresenter.getHistory(data -> {
            hideLoading();
            adapter.setNewData(data);
        });
    }

    private void initTopImg() {
        AppCompatImageView imageView = findViewById(R.id.icon);

        int w = ScreenTool.getScreenWidth(getContext());
        int h = (int) (w * 0.46f);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w,h);
        imageView.setLayoutParams(layoutParams);
    }
}
