package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.presenter.SearchMatchPre;
import com.cpigeon.app.modular.matchlive.view.adapter.JiGeDataAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.SearchJGAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.SearchMatchAdapter;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class SearchJGFragment extends BaseSearchResultFragment<SearchMatchPre> {

    JiGeDataAdapter adapter;

    int currentPosition = -1;
    @Override
    protected SearchMatchPre initPresenter() {
        return new SearchMatchPre(getActivity());
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle1.setText("序号");

        adapter = new JiGeDataAdapter(mPresenter.matchInfo.getLx());
        adapter.setMatchInfo(mPresenter.matchInfo
        );
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Object item = ((JiGeDataAdapter) adapter).getData().get(position);
            if(item instanceof JiGeDataAdapter.JiGeTitleItem_XH
                    || item instanceof JiGeDataAdapter.JiGeTitleItem_GP){
                if(currentPosition == -1){ //当前没有展开项
                    adapter.expand(position);
                    currentPosition = position;

                }else {
                    if(currentPosition == position){
                        adapter.collapse(position);
                        currentPosition = -1;
                    }else if(currentPosition > position){
                        adapter.collapse(currentPosition);
                        adapter.expand(position);
                        currentPosition = position;
                    }else {
                        adapter.collapse(currentPosition);
                        int expandPosition = position - 1;
                        adapter.expand(expandPosition);
                        currentPosition = expandPosition;
                    }
                }
            }

        });

        showLoading();

        bindData();

        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page ++;
            bindData();
        }, recyclerView);

    }

    public void bindData(){
        if (mPresenter.matchInfo.getLx().equals("xh")) {
            hideLoading();
            mPresenter.getJGMessageXH(data -> {
                if(data.isEmpty()){
                    adapter.loadMoreEnd();
                }else {
                    adapter.loadMoreComplete();
                    adapter.addData(JiGeDataAdapter.getXH(data));
                }
            });
        }else {
            mPresenter.getJGMessageGP(data -> {
                hideLoading();
                if(data.isEmpty()){
                    adapter.loadMoreEnd();
                }else {
                    adapter.loadMoreComplete();
                    adapter.addData(JiGeDataAdapter.getGP(data));
                }
            });
        }
    }
}
