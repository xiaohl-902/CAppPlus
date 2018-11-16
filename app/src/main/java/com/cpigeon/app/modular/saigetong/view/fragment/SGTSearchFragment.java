package com.cpigeon.app.modular.saigetong.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.app.modular.saigetong.view.adapter.SGTGZAdapter;
import com.cpigeon.app.modular.saigetong.view.adapter.SGTSearchAdapter;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.customview.SearchEditText;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/22.
 */

public class SGTSearchFragment extends BaseMVPFragment<SGTPresenter> implements SearchEditText.OnSearchClickListener {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.widget_title_bar_search)
    SearchEditText mSearchEditText;

    private SGTSearchAdapter mAdapter;

    @Override
    protected SGTPresenter initPresenter() {
        return new SGTPresenter(getActivity());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_seach_layout;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }


    @Override
    public void finishCreateView(Bundle state) {
        setTitle("公棚赛鸽搜索");

        mSearchEditText.setOnSearchClickListener(this);
        bindUi(RxUtils.textChanges(mSearchEditText),mPresenter.setKeyWord());



        mAdapter = new SGTSearchAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecorationLine(mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TITLE, mAdapter.getItem(position).getFoot())
                    .putExtra(IntentBuilder.KEY_DATA, mAdapter.getItem(position).getId())
                    .putExtra(IntentBuilder.KEY_TYPE, mPresenter.guid)
                    .startParentActivity(getActivity(), SGTDetailsFragment.class);
        });
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    /**
     * 文本框输入监听
     *
     * @param view
     * @param keyword
     */
    @Override
    public void onSearchClick(View view, String keyword) {
        showLoading();
        mPresenter.getSGTSearchFootListData(data -> {
            hideLoading();
            mAdapter.setNewData(data);
        });
    }
}
