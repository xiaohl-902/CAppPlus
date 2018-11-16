package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.RacePre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.butterknife.AntiShake;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/15.
 */

public class ReportDataFragment extends BasePageTurnFragment<RacePre, RaceReportAdapter, MultiItemEntity> implements IReportData {

    @BindView(R.id.list_header_race_detial_table_header_1)
    TextView listHeaderRaceDetialTableHeader1;
    @BindView(R.id.list_header_race_detial_table_header_2)
    TextView listHeaderRaceDetialTableHeader2;
    @BindView(R.id.list_header_race_detial_table_header_3)
    TextView listHeaderRaceDetialTableHeader3;
    @BindView(R.id.layout_list_table_header)
    LinearLayout layoutListTableHeader;
    @BindView(R.id.searchEditText)
    SearchEditText searchEditText;
    Unbinder unbinder;

    private MatchInfo matchInfo;
    private String sKey = "";//当前搜索关键字

    private int lastExpandItemPosition = -1;//最后一个索引

    @Override
    public void onRefresh() {
        super.onRefresh();
        mAdapter.notifyDataSetChanged();
        lastExpandItemPosition = -1;
        if (searchEditText != null) {
            searchEditText.setText(this.sKey);
            searchEditText.setSelection(this.sKey.length());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initMatchinfo();
    }

    private void initMatchinfo() {
        if (matchInfo == null)
            this.matchInfo = ((RaceReportActivity) getActivity()).getMatchInfo();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_race_data;
    }

    @Override
    protected RacePre initPresenter() {
        return new RacePre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }


    private void initSearch() {
        searchEditText.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                //search(keyword);
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                        .putExtra(BaseSearchResultFragment.KEY_WORD, keyword)
                        .putExtra(IntentBuilder.KEY_TYPE, matchInfo.getLx())
                        .startParentActivity(getSupportActivity(), SearchReportFragment.class);
                searchEditText.setText(keyword);
            }
        });
    }

    @Override
    public String getMatchType() {
        initMatchinfo();
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
        initMatchinfo();
        return matchInfo.getSsid();
    }

    @Override
    public String getFoot() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hascz() {
        return true;
    }

    @Override
    public int czIndex() {
        return -1;
    }

    @Override
    public String sKey() {
        return sKey;
    }

    @Override
    public MatchInfo getMatchInfo() {
        initMatchinfo();
        return matchInfo;
    }

    @Override
    public void showDefaultGCJKInfo(GeCheJianKongRace geCheJianKongRace) {

    }

    @Override
    public void refreshBoomMnue() {

    }

    @Override
    protected int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected String getEmptyDataTips() {
        return "暂时没有报到数据";
    }

    @Override
    public RaceReportAdapter getNewAdapterWithNoData() {
        RaceReportAdapter adapter = new RaceReportAdapter(getMatchType());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((RaceReportAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if ("xh".equals(getMatchType())) {
                    if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
                        if (((RaceReportAdapter.MatchTitleXHItem) item).isExpanded()) {
                            if (lastExpandItemPosition == position) {
                                lastExpandItemPosition = -1;
                            }
                            adapter.collapse(position);
                        } else {
                            if (lastExpandItemPosition >= 0) {
                                adapter.collapse(lastExpandItemPosition);
                                Logger.e("上一个关闭的项的postion" + lastExpandItemPosition);
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
                                Logger.e("当前被展开的项的lastExpandItemPosition" + lastExpandItemPosition);
                            }
                        }
                    }
                } else if ("gp".equals(getMatchType())) {
                    if (item instanceof RaceReportAdapter.MatchTitleGPItem) {
                        if (((RaceReportAdapter.MatchTitleGPItem) item).isExpanded()) {
                            if (lastExpandItemPosition == position) {
                                lastExpandItemPosition = -1;
                            }
                            adapter.collapse(position);
                        } else {
                            if (lastExpandItemPosition >= 0) {
                                adapter.collapse(lastExpandItemPosition);
                                Logger.e("上一个关闭的项的postion" + lastExpandItemPosition);
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
                                Logger.e("当前被展开的项的lastExpandItemPosition" + lastExpandItemPosition);
                            }
                        }
                    }

                }

            }

        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String key = sKey;
                boolean show = false;
                if (!TextUtils.isEmpty(key)) {
                    new SaActionSheetDialog(getActivity())
                            .builder()
                            .addSheetItem(getString(R.string.search_prompt_clear_key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    search("");
                                }
                            })
                            .setCancelable(true)
                            .show();
                } else {
                    if ("xh".equals(getMatchType())) {
                        Object item = ((RaceReportAdapter) baseQuickAdapter).getData().get(i);
                        if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
                            key = ((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getName();
                            show = true;
                        }
                    } else if ("gp".equals(getMatchType())) {
                        Object item = ((RaceReportAdapter) baseQuickAdapter).getData().get(i);
                        if (item instanceof RaceReportAdapter.MatchTitleGPItem) {
                            key = ((RaceReportAdapter.MatchTitleGPItem) item).getMatchReportGP().getName();
                            show = true;
                        }
                    }
                    final String finalKey = key;
                    if (show)
                        new SaActionSheetDialog(getActivity())
                                .builder()
                                .addSheetItem(String.format(getString(R.string.search_prompt_has_key), key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //search(finalKey);
                                        IntentBuilder.Builder()
                                                .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                                                .putExtra(BaseSearchResultFragment.KEY_WORD, finalKey)
                                                .putExtra(IntentBuilder.KEY_TYPE, matchInfo.getLx())
                                                .startParentActivity(getSupportActivity(), SearchReportFragment.class);
                                    }
                                })
                                .setCancelable(true)
                                .show();
                }
                return true;
            }
        });

        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        ((RaceReportActivity) getActivity()).initBulletin();
//        mPresenter.loadRaceData(0, 0);

        getServerData1();
    }

    public void search(String keyword) {
        this.sKey = keyword;
        onRefresh();
    }

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);

        initSearch();
        if ("gp".equals(getMatchType())) {
            listHeaderRaceDetialTableHeader1.setText("名次");
        }

        img_hint.setVisibility(View.VISIBLE);
        img_ic_instructions.setVisibility(View.VISIBLE);

        mSaActionSheetDialog = new SaActionSheetDialog(getActivity()).builder();

        mSaActionSheetDialog.addSheetItem("按名次排序", position -> {
            mAdapter.getData().clear();
            setPageIndex(1);
            mPresenter.loadRaceData(0, 0);
            img_ic_instructions.setChecked(false);
        });
        mSaActionSheetDialog.addSheetItem("按时间排序", position -> {
            mAdapter.getData().clear();
            setPageIndex(1);
            mPresenter.loadRaceData(0, 1);
            img_ic_instructions.setChecked(true);
        });
    }

    @BindView(R.id.img_hint)
    ImageView img_hint;
    @BindView(R.id.img_ic_instructions)
    CheckBox img_ic_instructions;

    @OnClick({R.id.img_hint, R.id.img_ic_instructions, R.id.ll_sort})
    public void onViewClicked(View view) {

        if (AntiShake.getInstance().check()) {//防止点击过快
            return;
        }

        switch (view.getId()) {
            case R.id.img_hint:
                if ("gp".equals(getMatchType())) {
                    DialogUtils.createDialog(getActivity(), "数据源于公棚上传，成绩内容仅作为参考，最终成绩以公棚公布为准，中鸽网不承担相关责任！", dialog -> {
                        dialog.dismiss();
                    });
                } else {
                    DialogUtils.createDialog(getActivity(), "数据源于协会上传，成绩内容仅作为参考，最终成绩以协会公布为准，中鸽网不承担相关责任！", dialog -> {
                        dialog.dismiss();
                    });
                }
                break;

            case R.id.img_ic_instructions:

                break;

            case R.id.ll_sort:

                if (mRecyclerView.getScrollState() != 0) {
                    //recycleView正在滑动
                    return;
                }

                if (img_ic_instructions.isChecked()) {
                    img_ic_instructions.setChecked(false);
                } else {
                    img_ic_instructions.setChecked(true);
                }

                getServerData2();
                break;
        }
    }

    private SaActionSheetDialog mSaActionSheetDialog;

    private void getServerData2() {
        try {
            if ("gp".equals(getMatchType())) {
                onRefresh();
            } else {
                if (img_ic_instructions.isChecked()) {
                    img_ic_instructions.setChecked(false);
                } else {
                    img_ic_instructions.setChecked(true);
                }

                try {
                    mSaActionSheetDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServerData1() {
        if ("gp".equals(getMatchType())) {
            if (img_ic_instructions.isChecked()) {
                mPresenter.loadRaceData(0, 1);
            } else {
                mPresenter.loadRaceData(0, 0);
            }
        } else {
            if (img_ic_instructions.isChecked()) {
                mPresenter.loadRaceData(0, 1);
            } else {
                mPresenter.loadRaceData(0, 0);
            }
        }
    }

}
