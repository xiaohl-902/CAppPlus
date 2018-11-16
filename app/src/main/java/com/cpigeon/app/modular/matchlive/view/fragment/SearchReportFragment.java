package com.cpigeon.app.modular.matchlive.view.fragment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.modular.matchlive.presenter.SearchMatchPre;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.SearchMatchAdapter;
import com.cpigeon.app.utils.IntentBuilder;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class SearchReportFragment extends BaseSearchResultFragment<SearchMatchPre> {

    RaceReportAdapter adapter;

    int currentPosition = -1;

    String type;

    @Override
    protected SearchMatchPre initPresenter() {
        return new SearchMatchPre(getActivity());
    }

    @Override
    protected void initView() {
        super.initView();

        type = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);

        tvTitle1.setText("暂排名次");

        adapter = new RaceReportAdapter(type);
        recyclerView.setAdapter(adapter);

        adapter.setMatchInfo(mPresenter.matchInfo);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Object item = ((RaceReportAdapter) adapter).getData().get(position);
            if(item instanceof RaceReportAdapter.MatchTitleXHItem
                    || item instanceof RaceReportAdapter.MatchTitleGPItem){
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
            mPresenter.page++;
            bindData();
        }, recyclerView);


    }

    public void bindData(){
        if (type.equals("xh")) {
            mPresenter.getReportXH(data -> {
                if(data.isEmpty()){
                    adapter.loadMoreEnd();
                }else {
                    adapter.loadMoreComplete();
                    adapter.addData(RaceReportAdapter.getXH(data));
                }
                hideLoading();
            });
        }else {
            mPresenter.getReportGP(data -> {
                hideLoading();
                if(data.isEmpty()){
                    adapter.loadMoreEnd();
                }else {
                    adapter.loadMoreComplete();
                    adapter.addData(RaceReportAdapter.getGP(data));
                }
            });
        }
    }
}
