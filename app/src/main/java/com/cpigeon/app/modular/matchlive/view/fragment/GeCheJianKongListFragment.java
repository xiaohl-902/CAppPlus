package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.presenter.GeCheJianKongPersenter;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.GeCheJianKongExpandListAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IGeCheJianKongListView;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.cpigeon.app.utils.http.LogUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/7/11.
 */

public class GeCheJianKongListFragment extends BasePageTurnFragment<GeCheJianKongPersenter, GeCheJianKongExpandListAdapter, MultiItemEntity> implements IGeCheJianKongListView {
    public static final String KEY_TYPE = "show_type";
    public static final String TYPE_XIEHUI = "2";
    public static final String TYPE_GONGPENG = "1";
    public static final String TYPE_GEREN = "3";
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;
    private String _showType;

    String _searchKey = "";

    int currentPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _showType = getArguments().getString(KEY_TYPE);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                search(keyword);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
       /* _searchKey = "";
        if (searchEdittext != null) {
            searchEdittext.setText("");
        }*/
    }

    public void search(String keyword) {
        this._searchKey = keyword;
        onRefresh();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_com_swiperefreshlayout_recyclerview_has_searchedittext;
    }

    @Override
    public String getOrgType() {
        return _showType;
    }

    @Override
    public String getSearchKey() {
        return _searchKey;
    }

    @Override
    protected GeCheJianKongPersenter initPresenter() {
        return new GeCheJianKongPersenter(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getDefaultPageSize() {
        return 10;
    }

    @Override
    protected String getEmptyDataTips() {
        return "没有鸽车监控信息";
    }

    @Override
    public GeCheJianKongExpandListAdapter getNewAdapterWithNoData() {
        GeCheJianKongExpandListAdapter adapter = new GeCheJianKongExpandListAdapter(null);
        adapter.setOnItemClickListener(onItemClickListener);
        //lastExpandItemPosition = -1;
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadNext();
    }

    @Override
    public void showMoreData(List<MultiItemEntity> dataBeen) {
        LogUtil.print(dataBeen);
        super.showMoreData(dataBeen);
        currentPosition = -1;
    }

    //private int lastExpandItemPosition = -1;//最后一个索引
    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Object item = ((GeCheJianKongExpandListAdapter) adapter).getData().get(position);
            if (item instanceof GeCheJianKongExpandListAdapter.OrgItem) {
                LogUtil.print("p: " + position);
                if(currentPosition == -1){//当前没有展开项
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
                        GeCheJianKongExpandListAdapter.OrgItem orgItem = (GeCheJianKongExpandListAdapter.OrgItem) mAdapter.getItem(currentPosition);
                        int dataSize = orgItem.getOrgInfo().getRaces().size();
                        int expandPosition = position - dataSize;
                        adapter.expand(expandPosition);
                        currentPosition = expandPosition;
                    }
                }
                mAdapter.orgItemPosition = currentPosition;
                LogUtil.print("cp: " + currentPosition);
            } else if (item instanceof GeCheJianKongExpandListAdapter.RaceItem) {
                GeCheJianKongExpandListAdapter.RaceItem raceItem = (GeCheJianKongExpandListAdapter.RaceItem) item;
                if(raceItem.getRace().getStateCode() == 0){

                }else {
                    Intent intent = new Intent(getActivity(), MapLiveActivity.class);
                    intent.putExtra("geCheJianKongRace",((GeCheJianKongExpandListAdapter.RaceItem) item).getRace());
                    Logger.e(((GeCheJianKongExpandListAdapter.RaceItem) item).getRace().getId()+"：iD");
                    startActivity(intent);
                }
            }
        }
    };

}
