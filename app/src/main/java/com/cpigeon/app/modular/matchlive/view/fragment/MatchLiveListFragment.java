package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.event.MatchLiveRefreshEvent;
import com.cpigeon.app.modular.home.view.activity.SearchActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveListPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceXunFangActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Zhu TingYu on 2018/6/12.
 */

public class  MatchLiveListFragment extends BaseMVPFragment<MatchLiveListPre> {

    RecyclerView recyclerView;
    MatchLiveExpandAdapter adapter;
    private int lastExpandItemPosition = -1;//最后一个索引

    @Override
    protected MatchLiveListPre initPresenter() {
        return  new MatchLiveListPre(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MatchLiveExpandAdapter(Lists.newArrayList(),0);
        adapter.bindToRecyclerView(recyclerView);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Object item = ((MatchLiveExpandAdapter) adapter).getData().get(position);
//                Logger.d(item.getClass().getName());
            if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                if (!"bs".equals(((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo().getDt())) {
                    if (mPresenter.checkArrearage(((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo()))
                        return;
                    Intent intent = new Intent(getActivity(), RaceReportActivity.class);
                    MatchInfo mi = ((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo();
                    Bundle bundle = new Bundle();                //创建Bundle对象
                    bundle.putSerializable("matchinfo", mi);     //装入数据
                    bundle.putString("loadType", mPresenter.type);
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
                if (mPresenter.checkArrearage(mi)) return;
                if (mi != null && !"jg".equals(mi.getDt())) {
                    Intent intent;
                    if (mi.isMatch()) {
                        intent = new Intent(getActivity(), RaceReportActivity.class);
                    } else {
                        intent = new Intent(getActivity(), RaceXunFangActivity.class);
                    }
                    Bundle bundle = new Bundle();                //创建Bundle对象
                    bundle.putSerializable("matchinfo", mi);     //装入数据
                    bundle.putString("loadType", mPresenter.type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;


                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
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
                                    bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCH_HINT_TEXT, "比赛名称、" + (mPresenter.type.equals(Const.MATCHLIVE_TYPE_GP) ? "公棚名称" : "协会名称"));     //装入数据
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                return true;
            }
        });

        adapter.setOnNotNetClickListener(v -> {
            bindData();
        });

        setRefreshListener(() -> {
            bindData();
        });

        bindData();

    }

    private void bindData() {
        lastExpandItemPosition = -1;
        showLoading();
        mPresenter.getMatchList(matchInfos -> {
            hideLoading();
            adapter.setNewData(MatchLiveExpandAdapter.get(matchInfos));

            int loadCount = 0;
            for (int i = 0; i < matchInfos.size(); i++) {
                if (!matchInfos.get(i).isCollectionPigeon()) {
                    loadCount = i;
                    break;
                }
            }

            EventBus.getDefault().post(new MatchLiveRefreshEvent(mPresenter.type, matchInfos.size() - loadCount));
        });
    }
}
