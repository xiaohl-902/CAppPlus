package com.cpigeon.app.modular.saigetong.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.GeCheJianKongExpandListAdapter;
import com.cpigeon.app.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.app.modular.saigetong.view.adapter.SGTGZAdapter;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.http.LogUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTGzFragment extends BaseMVPFragment<SGTPresenter> {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    int currentPosition = -1;

    private SGTGZAdapter mAdapter;//适配器

    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            Object item = ((SGTGZAdapter) adapter).getData().get(position);
//            Log.d(TAG, "onItemClick:postion:--> " + position);
//            Log.d(TAG, "onItemClick:getItemViewType--> " + adapter.getItemViewType(position));
            if (item instanceof SGTGZAdapter.OrgItem) {


                if (currentPosition == -1) { //当前没有展开项

                    if (((SGTGZAdapter) adapter).getData().get(position) instanceof SGTGZAdapter.OrgItem) {
                        ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(position))).getOrgInfo().setTag(2);
                    }

                    adapter.expand(position);
                    currentPosition = position;
                } else {
                    if (currentPosition == position) {
                        if (((SGTGZAdapter) adapter).getData().get(position) instanceof SGTGZAdapter.OrgItem) {
                            ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(position))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(position);

                        currentPosition = -1;
                    } else if (currentPosition > position) {

                        if (((SGTGZAdapter) adapter).getData().get(currentPosition) instanceof SGTGZAdapter.OrgItem) {
                            ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(currentPosition))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(currentPosition);

                        if (((SGTGZAdapter) adapter).getData().get(position) instanceof SGTGZAdapter.OrgItem) {
                            ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(position))).getOrgInfo().setTag(2);
                        }

                        adapter.expand(position);
                        currentPosition = position;
                    } else {
                        if (((SGTGZAdapter) adapter).getData().get(currentPosition) instanceof SGTGZAdapter.OrgItem) {
                            ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(currentPosition))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(currentPosition);


                        SGTGZAdapter.OrgItem orgItem = (SGTGZAdapter.OrgItem) mAdapter.getItem(currentPosition);
                        if (orgItem.getOrgInfo().getData() != null) {
                            int dataSize = orgItem.getOrgInfo().getData().size();
                            int expandPosition = position - dataSize;


                            if (((SGTGZAdapter) adapter).getData().get(expandPosition) instanceof SGTGZAdapter.OrgItem) {
                                ((SGTGZAdapter.OrgItem) (((SGTGZAdapter) adapter).getData().get(expandPosition))).getOrgInfo().setTag(2);
                            }

                            adapter.expand(expandPosition);

                            currentPosition = expandPosition;
                        }


                    }
                }

            } else if (item instanceof SGTGZAdapter.RaceItem) {//展开的item
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_TITLE, ((SGTGZAdapter.RaceItem) item).getRace().getFoot())
                        .putExtra(IntentBuilder.KEY_DATA, ((SGTGZAdapter.RaceItem) item).getRace().getId())
                        .putExtra(IntentBuilder.KEY_TYPE, mPresenter.guid)
                        .startParentActivity(getActivity(), SGTDetailsFragment.class);

            }
        }
    };


    @Override
    protected SGTPresenter initPresenter() {
        return new SGTPresenter(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_layout;
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

        mAdapter = new SGTGZAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.bindToRecyclerView(mRecyclerView);
        showLoading();
        mPresenter.getSGTGzListData(data -> {
            hideLoading();
            mAdapter.addData(SGTGZAdapter.get(data));
        });
    }
}
