package com.cpigeon.app.modular.saigetong.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.app.modular.saigetong.view.adapter.SGTRpRecordAdapter;
import com.cpigeon.app.utils.IntentBuilder;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTRpRecordFragment extends BaseMVPFragment<SGTPresenter> {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_sgt_info1)
    TextView tv1;

    @BindView(R.id.tv_sgt_info2)
    TextView tv2;

    private SGTRpRecordAdapter mAdapter;
    LinearLayout top;


    @Override
    protected SGTPresenter initPresenter() {
        return new SGTPresenter(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }


    @Override
    public void finishCreateView(Bundle state) {

        setTitle(getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TITLE));

        toolbar.getMenu().clear();
        toolbar.getMenu().add("").setIcon(R.mipmap.sgt_sousuo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA, mPresenter.guid)
                        .startParentActivity(getActivity(), SGTSearchFragment.class);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        top = findViewById(R.id.top_ll);

        mAdapter = new SGTRpRecordAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecorationLine(mRecyclerView);
        mAdapter.bindToRecyclerView(mRecyclerView);
        showLoading();
        mPresenter.getSGTRpRecoudData(data -> {
            hideLoading();
            mAdapter.setSGTRpRecordEntity(data, getActivity());
            if(data.getAllgzcount() != 0 && data.getAlltpcount() != 0){
                top.setVisibility(View.VISIBLE);
                tv1.setText(String.valueOf(data.getAllgzcount() + "羽  "));
                tv2.setText(String.valueOf(data.getAlltpcount()) +"张照片");
            }else top.setVisibility(View.GONE);

            if (data.getList() != null) {
                mAdapter.setNewData(data.getList());
            } else {
                mAdapter.setEmptyView();
            }
        });

        //下拉刷新
        setRefreshListener(() -> {
            mPresenter.pi2 = 1;
            mPresenter.getSGTRpRecoudData(data -> {
                mAdapter.setSGTRpRecordEntity(data, getActivity());
                if(data.getList() != null){
                    mAdapter.setNewData(data.getList());
                }
                hideLoading();
            });
        });

        //设置加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.pi2++;
            mPresenter.getSGTRpRecoudData(data -> {
                mAdapter.setSGTRpRecordEntity(data, getActivity());
                if (data.getList() == null) {
                    mAdapter.setLoadMore(true);
                } else {
                    mAdapter.setLoadMore(false);
                    mAdapter.addData(data.getList());
                }
            });
        }, mRecyclerView);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_sgt_info;
    }
}
