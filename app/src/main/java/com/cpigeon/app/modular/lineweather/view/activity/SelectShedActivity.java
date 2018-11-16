package com.cpigeon.app.modular.lineweather.view.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.message.ui.selectPhoneNumber.model.ContactModel;
import com.cpigeon.app.message.ui.selectPhoneNumber.pinyin.CharacterParser;
import com.cpigeon.app.message.ui.selectPhoneNumber.pinyin.PinyinComparator;
import com.cpigeon.app.modular.lineweather.model.bean.GetGongPengListEntity;
import com.cpigeon.app.modular.lineweather.presenter.LineWeatherPresenter;
import com.cpigeon.app.modular.lineweather.view.adapter.SelectShedAdapter;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.jiang.android.lib.adapter.expand.StickyRecyclerHeadersDecoration;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * 选择公棚
 * Created by Administrator on 2018/5/7.
 */
public class SelectShedActivity extends BaseActivity<LineWeatherPresenter> {

    private MarqueeTextView title;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.side_bar)
    WaveSideBar sideBar;

    @BindView(R.id.et_search)
    EditText et_search;//输入框

    @BindView(R.id.tv_search)
    TextView tv_search;//

    private SelectShedAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_shed;
    }

    @Override
    public LineWeatherPresenter initPresenter() {
        return new LineWeatherPresenter(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {


        et_search.setHint("请输入公棚名称快速查找");

        title = findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        title.setText("选择公棚");
        toolbar.setNavigationOnClickListener(v -> finish());

        mAdapter = new SelectShedAdapter(this, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerView.setAdapter(mAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        showLoading();
        mPresenter.getTool_GetGongPengInfo("", data -> {
            mSwipeLayout.setRefreshing(false);
            hideLoading();
            seperateLists(data);
        });

        //下拉刷新
        setRefreshListener(() -> {
            mPresenter.getTool_GetGongPengInfo("", data -> {
                mSwipeLayout.setRefreshing(false);
                hideLoading();
                seperateLists(data);
            });
        });

        sideBar.setIndexItems("A", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "Q", "R", "S", "T", "W", "X", "Y", "Z", "#");
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                Log.d("sousuo", index);
                for (int i = 0; i < mMembers.size(); i++) {
                    if (index.equals(mMembers.get(i).getSortLetters())) {
                        //或者
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!et_search.getText().toString().isEmpty() && et_search.getText().length() > 0) {
                        //完成自己的事件
                        showLoading();

                        mPresenter.getTool_GetGongPengInfo(et_search.getText().toString(), data -> {
                            mSwipeLayout.setRefreshing(false);
                            hideLoading();
                            seperateLists(data);
                        });
                    }
                }
                return false;
            }
        });

        tv_search.setOnClickListener(view -> {
            if (!et_search.getText().toString().isEmpty() && et_search.getText().length() > 0) {
                //完成自己的事件
                showLoading();
                mPresenter.getTool_GetGongPengInfo(et_search.getText().toString(), data -> {
                    mSwipeLayout.setRefreshing(false);
                    hideLoading();
                    seperateLists(data);
                });
            }
        });
    }

    private List<ContactModel.MembersEntity> mMembers = new ArrayList<>();
    private List<ContactModel.MembersEntity> mMembers2 = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    private List<ContactModel.MembersEntity> mAllLists = new ArrayList<>();

    private void seperateLists(List<GetGongPengListEntity> data) {

        ContactModel mModel = new ContactModel();

        mAllLists.clear();
        for (int i = 0; i < data.size(); i++) {
            ContactModel.MembersEntity myEntity = new ContactModel.MembersEntity();
            myEntity.setUsername(data.get(i).getGpmc());
            myEntity.setLo(data.get(i).getJd());
            myEntity.setLa(data.get(i).getWd());
            mAllLists.add(myEntity);
        }

        mModel.setMembers(mAllLists);

        mMembers.clear();
        mMembers2.clear();
        if (mModel.getMembers() != null && mModel.getMembers().size() > 0) {

            List<String> aaa = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                aaa.add(data.get(i).getGpmc().trim());
            }

            int dataSeze = aaa.size();

            Collections.sort(aaa, CHINA_COMPARE);

            for (int i = 0; i < dataSeze; i++) {
                for (int y = 0; y < dataSeze; y++) {
                    if (aaa.get(i).equals((mModel.getMembers().get(y).getUsername()))) {
                        ContactModel.MembersEntity temp = mModel.getMembers().get(y);
                        if (StringValid.isStringValid(temp.getUsername())) {
                            ContactModel.MembersEntity entity = temp;
                            String pinyin = characterParser.getSelling(entity.getUsername()).trim();
                            String sortString = pinyin.substring(0, 1).toUpperCase();

                            if (sortString.matches("[A-Z]")) {
                                entity.setSortLetters(sortString.toUpperCase());
                                entity.setLo(entity.getLo());
                                entity.setLa(entity.getLa());
                            } else {
                                entity.setSortLetters("#");
                            }
                            mMembers2.add(entity);
                        }
                    }
                }

                Log.d("sousuo", "seperateLists: " + aaa.get(i));
            }

            for (int i = 0; i < mMembers2.size(); i++) {
                ContactModel.MembersEntity temp = mMembers2.get(i);
                if (StringValid.isStringValid(temp.getUsername())) {
                    ContactModel.MembersEntity entity = temp;
                    String pinyin = characterParser.getSelling(entity.getUsername()).trim();
                    String sortString = pinyin.substring(0, 1).toUpperCase();

                    if (sortString.matches("[A-Z]")) {
                        entity.setSortLetters(sortString.toUpperCase());
                    } else {
                        entity.setSortLetters("#");
                    }
                    mMembers.add(entity);
                }
            }

            Collections.sort(mMembers, pinyinComparator);

            mAdapter.addAll(mMembers);
        }
    }

    private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    private void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setEnabled(true);
            mSwipeLayout.setOnRefreshListener(listener);
        }
    }
}
