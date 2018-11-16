package com.cpigeon.app.modular.matchlive.view.fragment;

import com.cpigeon.app.modular.matchlive.presenter.SearchMatchPre;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;

/**
 * Created by Zhu TingYu on 2017/12/12.
 */

public class SearchXuFangFragment extends BaseSearchResultFragment<SearchMatchPre> {

    RaceXunFangAdapter adapter;
    int currentPosition = -1;

    @Override
    protected SearchMatchPre initPresenter() {
        return new SearchMatchPre(getActivity());
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle1.setText("名次");

        adapter = new RaceXunFangAdapter(mPresenter.matchInfo);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Object item = ((RaceXunFangAdapter) adapter).getData().get(position);
            if(item instanceof RaceXunFangAdapter.MatchTitleGPItem){
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
        mPresenter.getReportGP(data -> {
            hideLoading();
            if(data.isEmpty()){
                adapter.loadMoreEnd();
            }else {
                adapter.loadMoreComplete();
                adapter.addData(RaceXunFangAdapter.getGP(data));
            }
        });
    }

}
