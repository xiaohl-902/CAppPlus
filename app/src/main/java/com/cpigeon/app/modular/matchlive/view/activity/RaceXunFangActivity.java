package com.cpigeon.app.modular.matchlive.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.modular.guide.view.SplashActivity;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.RacePre;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.BaseSearchResultFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.RaceDetailsXunFangFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.SearchXuFangFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 家飞测试和训放
 * Created by Administrator on 2017/4/23.
 */

public class RaceXunFangActivity extends BasePageTurnActivity<RacePre, RaceXunFangAdapter, MultiItemEntity> implements IReportData {

    @BindView(R.id.race_details_marqueetv)
    MarqueeTextView raceDetailsMarqueetv;
    @BindView(R.id.list_header_race_detial_gg)
    MarqueeTextView listHeaderRaceDetialGg;
    @BindView(R.id.layout_gg)
    LinearLayout layoutGg;
    @BindView(R.id.searchEditText)
    SearchEditText searchEditText;
    @BindView(R.id.list_header_race_detial_table_header_1)
    TextView listHeaderRaceDetialTableHeader1;
    @BindView(R.id.list_header_race_detial_table_header_2)
    TextView listHeaderRaceDetialTableHeader2;
    @BindView(R.id.list_header_race_detial_table_header_3)
    TextView listHeaderRaceDetialTableHeader3;
    @BindView(R.id.layout_list_table_header)
    LinearLayout layoutListTableHeader;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.menu_item_org)
    FloatingActionButton menuItemOrg;
    @BindView(R.id.menu_item_race)
    FloatingActionButton menuItemRace;
    @BindView(R.id.menu)
    FloatingActionMenu menu;
    @BindView(R.id.menu_item_gyt)
    FloatingActionButton menuItemGyt;

    private MatchInfo matchInfo;//赛事信息
    private Bundle bundle;
    private Intent intent;
    private String sKey = "";//当前搜索关键字
    private int lastExpandItemPosition = -1;//最后一个索引
    private GeCheJianKongRace defaultgeCheJianKongRace;

    public Bulletin getBulletin() {
        return bulletin;
    }

    private Bulletin bulletin;
    private String loadType;
    private List<UserFollow> userFollows = new ArrayList<>();

    @Override
    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    @Override
    public void refreshBoomMnue() {
        this.refreshMenu();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_race_xunfang_details;
    }

    @Override
    public RacePre initPresenter() {
        return new RacePre(this);
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        initData();
        initToolbar();
        initInfo();
        initBoomMnue();
        super.initView(savedInstanceState);
    }

    private void initBoomMnue() {
        menu.setClosedOnTouchOutside(true);
        if (Build.VERSION.SDK_INT < 21) {
            menuItemGyt.setImageResource(R.mipmap.ic_truck_white);
        } else {
            menuItemGyt.setImageResource(R.drawable.ic_svg_truck_white);
        }
        menuItemGyt.setColorNormalResId(R.color.colorButton_primary_normal);
        menuItemGyt.setColorPressedResId(R.color.colorButton_primary_focus);
        menuItemGyt.setColorDisabledResId(R.color.colorButton_Default_disable);

        menuItemOrg.setColorNormalResId(R.color.colorButton_orange_normal);
        menuItemOrg.setColorPressedResId(R.color.colorButton_orange_focus);
        menuItemOrg.setColorDisabledResId(R.color.colorButton_Default_disable);

        menuItemRace.setColorNormalResId(R.color.colorButton_Default_normal);
        menuItemRace.setColorPressedResId(R.color.colorButton_Default_focus);
        menuItemRace.setColorDisabledResId(R.color.colorButton_Default_disable);

        mPresenter.getDefaultGCJKInfo();
        refreshMenu();
    }

    /**
     * 刷新菜单显示内容
     */
    private void refreshMenu() {
        boolean _isJg = "jg".equals(matchInfo.getDt());
        //加载数据
        DbManager db = x.getDb(CpigeonConfig.getDataDb());
        UserFollow userFollow = null;
        try {
            userFollow = db.selector(UserFollow.class)
                    .where("uid", "=", CpigeonData.getInstance().getUserId(this))
                    .and("ftype", "=", matchInfo.getLx().equals("xh") ? "协会" : "公棚")
                    .and("rela", "=", EncryptionTool.encryptAES(EncryptionTool.decryptAES(matchInfo.getSsid()).split("/")[0]))
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        menuItemOrg.setLabelText((userFollow != null ? "取消关注" : "关注") + (matchInfo.getLx().equals("xh") ? "协会" : "公棚"));
        if (Build.VERSION.SDK_INT < 21) {
            menuItemOrg.setImageResource(userFollow != null ? R.mipmap.ic_favorite_white : R.mipmap.ic_favorite_white_border);
        } else {
            menuItemOrg.setImageResource(userFollow != null ? R.drawable.ic_svg_favorite_white_24dp : R.drawable.ic_svg_favorite_border_white_24dp);
        }
        menuItemOrg.setTag(userFollow);

        if (!_isJg) {
            try {
                userFollow = null;
                userFollow = db.selector(UserFollow.class)
                        .where("uid", "=", CpigeonData.getInstance().getUserId(this))
                        .and("ftype", "=", "比赛")
                        .and("rela", "=", matchInfo.getSsid())
                        .findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
            menuItemRace.setLabelText(userFollow != null ? "取消关注比赛" : "关注比赛");
            if (Build.VERSION.SDK_INT < 21) {
                menuItemRace.setImageResource(userFollow != null ? R.mipmap.ic_favorite_white : R.mipmap.ic_favorite_white_border);
            } else {
                menuItemRace.setImageResource(userFollow != null ? R.drawable.ic_svg_favorite_white_24dp : R.drawable.ic_svg_favorite_border_white_24dp);
            }
            menuItemRace.setTag(userFollow);
        } else {
            menuItemRace.setVisibility(View.GONE);
        }
    }

    private void initInfo() {
        if (bulletin != null && TextUtils.isEmpty(bulletin.getContent()))
            listHeaderRaceDetialGg.setText(bulletin.getContent());
        listHeaderRaceDetialTableHeader1.setText("名次");
        searchEditText.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                //search(keyword);
                IntentBuilder.Builder()
                        .putExtra(BaseSearchResultFragment.KEY_WORD, keyword)
                        .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                        .startParentActivity(getActivity(), SearchXuFangFragment.class);
                searchEditText.setText(keyword);
            }
        });
    }

    public void initToolbar() {
        if (matchInfo != null) {
            raceDetailsMarqueetv.setText(matchInfo.getMc());
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        matchInfo = (MatchInfo) bundle.getSerializable("matchinfo");
        loadType = bundle.getString("loadType");
    }


    public void search(String keyword) {
        this.sKey = keyword;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        lastExpandItemPosition = -1;//最后一个索引
        if (searchEditText != null) {
            searchEditText.setText(this.sKey);
            searchEditText.setSelection(this.sKey.length());
        }
    }

    @NonNull
    @Override
    public String getTitleName() {
        return matchInfo.getMc();
    }

    @Override
    public int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected String getEmptyDataTips() {
        return "没有报到数据";
    }

    @Override
    public RaceXunFangAdapter getNewAdapterWithNoData() {
        RaceXunFangAdapter adapter = new RaceXunFangAdapter(matchInfo);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((RaceXunFangAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if (item instanceof RaceXunFangAdapter.MatchTitleGPItem) {
//                    if (!"bs".equals(((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getDt()))
//                        return;
                    if (((RaceXunFangAdapter.MatchTitleGPItem) item).isExpanded()) {
                        if (lastExpandItemPosition == position) {
                            lastExpandItemPosition = -1;
                        }
                        adapter.collapse(position);
//                        Logger.e("当前被关闭的项的postion" + position);
                    } else {
                        if (lastExpandItemPosition >= 0) {
                            adapter.collapse(lastExpandItemPosition);
//                            Logger.e("上一个关闭的项的postion" + lastExpandItemPosition);
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
//                            Logger.e("当前被展开的项的lastExpandItemPosition" + lastExpandItemPosition);
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
                    new SaActionSheetDialog(RaceXunFangActivity.this)
                            .builder()
                            .addSheetItem(getString(R.string.search_prompt_clear_key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    //search("");
                                }
                            })
                            .setCancelable(true)
                            .show();
                } else {
                    if ("gp".equals(getMatchType())) {
                        Object item = ((RaceXunFangAdapter) baseQuickAdapter).getData().get(i);
                        if (item instanceof RaceXunFangAdapter.MatchTitleGPItem) {
                            key = ((RaceXunFangAdapter.MatchTitleGPItem) item).getMatchReportGP().getName();
                            show = true;
                        }
                    }
                    final String finalKey = key;
                    if (show)
                        new SaActionSheetDialog(RaceXunFangActivity.this)
                                .builder()
                                .addSheetItem(String.format(getString(R.string.search_prompt_has_key), key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //search(finalKey);
                                        IntentBuilder.Builder()
                                                .putExtra(IntentBuilder.KEY_DATA, matchInfo)
                                                .putExtra(BaseSearchResultFragment.KEY_WORD, finalKey)
                                                .startParentActivity(getActivity(), SearchXuFangFragment.class);
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
        mPresenter.loadRaceData(1, 0);
    }

    @Override
    public String getMatchType() {
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
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
        return matchInfo.isMatch();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_race_details, menu);
        return true;
    }

    @Override
    public void showDefaultGCJKInfo(GeCheJianKongRace geCheJianKongRace) {
        menuItemGyt.setLabelText(geCheJianKongRace != null ? "鸽车监控" : "未开启鸽车监控");
        menuItemGyt.setEnabled(geCheJianKongRace != null);
        this.defaultgeCheJianKongRace = geCheJianKongRace;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //android.R.id.home是Android内置home按钮的id
                finish();
                break;
            case R.id.action_save:

                break;
            case R.id.action_details:
                showDialogFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogFragment() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("xunfangdialogFragment");
        if (fragment != null) {
            mFragmentTransaction.remove(fragment);
        }
        RaceDetailsXunFangFragment detailsFragment = RaceDetailsXunFangFragment.newInstance("训放数据");
        detailsFragment.show(mFragmentTransaction, "xunfangdialogFragment");
    }

    @Override
    protected void onDestroy() {
        Activity activity = AppManager.getAppManager().getActivityByClass(MainActivity.class);
        if (activity == null) {
            Intent mIntent = new Intent(this, SplashActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
        super.onDestroy();
    }

    @OnClick({R.id.menu_item_org, R.id.menu_item_race, R.id.menu_item_gyt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_item_org:
                UserFollow tag = (UserFollow) view.getTag();
                if (tag != null) {
                    mPresenter.removeFollow(tag);
                    return;
                }
                mPresenter.addRaceOrgFollow();
                break;
            case R.id.menu_item_race:
                UserFollow tag1 = (UserFollow) view.getTag();
                if (tag1 != null) {
                    mPresenter.removeFollow(tag1);
                    return;
                }
                mPresenter.addRaceFollow();
                break;
            case R.id.menu_item_gyt:
                if (this.defaultgeCheJianKongRace != null) {
                    Intent intent = new Intent(this, MapLiveActivity.class);
                    intent.putExtra("geCheJianKongRace", defaultgeCheJianKongRace);
                    Logger.e(defaultgeCheJianKongRace.getId() + "：iD");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(menu.isOpened()){
            menu.close(true);
            return;
        }
        super.onBackPressed();
    }
}
