package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.home.view.activity.SearchActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveSubPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceXunFangActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IMatchSubView;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.customview.CustomEmptyView;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;

import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubFragment extends BaseFragment implements IMatchSubView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private MatchLiveExpandAdapter matchLiveAdapter;
    private List<MatchInfo> matchInfos;
    private int delayMillis = 1000;

    private MatchLiveSubPre pre = new MatchLiveSubPre(this);
    String currMatchType = "";
    private OnRefreshListener onRefreshListener;
    private Intent intent;
    private int lastExpandItemPosition = -1;//最后一个索引


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchLiveAdapter = new MatchLiveExpandAdapter(MatchLiveExpandAdapter.get(null), 0);
        matchLiveAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        matchLiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((MatchLiveExpandAdapter) adapter).getData().get(position);
//                Logger.d(item.getClass().getName());
                if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                    if (!"bs".equals(((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo().getDt())) {
                        if (checkArrearage(((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo()))
                            return;
                        intent = new Intent(getActivity(), RaceReportActivity.class);
                        MatchInfo mi = ((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo();
                        Bundle bundle = new Bundle();                //创建Bundle对象
                        bundle.putSerializable("matchinfo", mi);     //装入数据
                        bundle.putString("loadType", currMatchType);
                        bundle.putString("jigesuccess", "jigesuccess");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                    if (((MatchLiveExpandAdapter.MatchTitleItem) item).isExpanded()) {
                        if (lastExpandItemPosition == position) {
                            lastExpandItemPosition = -1;
                        }
                        adapter.collapse(position);
                    } else {
                        if (lastExpandItemPosition >= 0) {
                            adapter.collapse(lastExpandItemPosition);
                            if (lastExpandItemPosition > position) {//展开上面的项
                                adapter.expand(position);
                                lastExpandItemPosition = position;
                            } else if (lastExpandItemPosition < position) {//展开下面的项
                                adapter.expand(position - 1);
                                lastExpandItemPosition = position - 1;
                            }

                        } else {
                            lastExpandItemPosition = position;
                            adapter.expand(lastExpandItemPosition);
                        }


                    }
                    if (lastExpandItemPosition == -1) {
                    }
                } else if (item instanceof MatchLiveExpandAdapter.MatchDetialItem) {
                    MatchInfo mi = ((MatchLiveExpandAdapter.MatchDetialItem) item).getSubItem(0);
                    if (checkArrearage(mi)) return;
                    if (mi != null && !"jg".equals(mi.getDt())) {
                        if (mi.isMatch()) {
                            intent = new Intent(getActivity(), RaceReportActivity.class);
                        } else {
                            intent = new Intent(getActivity(), RaceXunFangActivity.class);
                        }
                        Bundle bundle = new Bundle();                //创建Bundle对象
                        bundle.putSerializable("matchinfo", mi);     //装入数据
                        bundle.putString("loadType", currMatchType);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;


                    }
                }
            }
        });

        matchLiveAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Object item = ((MatchLiveExpandAdapter) baseQuickAdapter).getData().get(i);
                if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                    final String s = ((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo().getMc();
                    new SaActionSheetDialog(getActivity())
                            .builder()
                            .addSheetItem(String.format(getString(R.string.search_prompt_has_key), s), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                                    Bundle bundle = new Bundle();                           //创建Bundle对象
                                    bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCHKEY, s);     //装入数据
                                    bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCH_HINT_TEXT, "比赛名称、" + (currMatchType.equals(Const.MATCHLIVE_TYPE_GP) ? "公棚名称" : "协会名称"));     //装入数据
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        mRecyclerView.swapAdapter(matchLiveAdapter, true);
        onRefresh();
        isPrepared = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && mCustomEmptyView != null && mCustomEmptyView.getVisibility() == View.VISIBLE) {
            onRefresh();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_matchlive_sub;
    }


//    @Override
//    public void showGPData(List<MatchInfo> matchInfoList) {
//        this.matchInfos = matchInfoList;
////        if (type == 1) {
////            matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfos));
////        }
//
//        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
//        if (onRefreshListener != null)
//            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_GP, matchInfoList);
//    }
//
//    @Override
//    public void showXHData(List<MatchInfo> matchInfoList) {
//        this.matchInfos = matchInfoList;
//        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
//        if (onRefreshListener != null)
//            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_XH, matchInfoList);
//    }

    @Override
    public void showData(List<MatchInfo> matchInfoList) {
        this.matchInfos = matchInfoList;
        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(currMatchType.equals(Const.MATCHLIVE_TYPE_GP) ? OnRefreshListener.DATA_Type_GP : OnRefreshListener.DATA_Type_XH, matchInfoList);
    }

    @Override
    public void setLoadType(int type) {

    }

    @Override
    public boolean hasDataList() {
        return matchLiveAdapter != null && matchLiveAdapter.getData() != null && matchLiveAdapter.getData().size() > 0;
    }

    public void setMatchType(String matchType) {
        currMatchType = matchType;
    }

    /**
     * 检查是否是欠费平台的直播
     *
     * @param matchInfo
     * @return
     */
    private boolean checkArrearage(MatchInfo matchInfo) {
        if (matchInfo != null && matchInfo.getRuid() != 0) {
            SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("提示");
            if (matchInfo.getRuid() == CpigeonData.getInstance().getUserId(getActivity())) {
                dialog.setContentText("您的直播平台已欠费\n请前往中鸽网充值缴费.");
                dialog.setCancelText("关闭");
                dialog.setConfirmText("知道了");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
            } else {
                dialog.setConfirmText("关闭");
                dialog.setContentText("该直播平台已欠费.");
            }
            dialog.setCancelable(false);
            dialog.show();
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener != null)
            onRefreshListener.onStartRefresh(MatchLiveSubFragment.this);
        if (lastExpandItemPosition >= 0) {
            matchLiveAdapter.collapse(lastExpandItemPosition);
            lastExpandItemPosition = -1;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        if (Const.MATCHLIVE_TYPE_GP.equals(currMatchType)) {
            pre.loadGPData();
        } else if (Const.MATCHLIVE_TYPE_XH.equals(currMatchType)) {
            pre.loadXHData();
        }
        matchLiveAdapter.setEnableLoadMore(true);
    }


    @Override
    public void showRefreshLoading() {
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshLoading() {
        if(mCustomEmptyView != null){
            mCustomEmptyView.setVisibility(View.GONE);
        }
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showEmptyData() {
        showTips("暂无比赛", TipType.View);
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if(mSwipeRefreshLayout != null && mCustomEmptyView != null && mRecyclerView != null){
                mSwipeRefreshLayout.setRefreshing(false);
                mCustomEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);

                if(isNetworkConnected()){
                    mCustomEmptyView.isHaveNet();
                    mCustomEmptyView.setEmptyImage(R.drawable.ic_empty_img);
                    mCustomEmptyView.setEmptyText(tip);
                }else {
                    mCustomEmptyView.isNotHaveNet();
                    mCustomEmptyView.setEmptyImage(R.drawable.ic_net_error);
                    mCustomEmptyView.setButtomOnClickListener(v -> {
                        onRefresh();
                    });
                }

            }
            return true;
        }
        return super.showTips(tip, tipType);
    }

    public interface OnRefreshListener {
        int DATA_Type_GP = 1;
        int DATA_Type_XH = 2;

        void onStartRefresh(MatchLiveSubFragment fragment);

        void onRefreshFinished(int type, List<MatchInfo> list);
    }
}
