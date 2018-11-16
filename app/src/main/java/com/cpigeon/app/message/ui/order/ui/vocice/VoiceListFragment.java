package com.cpigeon.app.message.ui.order.ui.vocice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.event.VoiceListUpdateEvent;
import com.cpigeon.app.message.ui.order.adpter.VoiceListAdapter;
import com.cpigeon.app.message.ui.order.ui.presenter.VoiceListPre;
import com.cpigeon.app.utils.IntentBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class VoiceListFragment extends BaseMVPFragment<VoiceListPre> {

    RecyclerView recyclerView;
    VoiceListAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected VoiceListPre initPresenter() {
        return new VoiceListPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("发票");

        EventBus.getDefault().register(this);

        toolbar.getMenu().clear();
        toolbar.getMenu().add("添加").setOnMenuItemClickListener(item -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), SetVoiceInfoFragment.class);
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VoiceListAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, adapter.getItem(position));
            getActivity().setResult(0, intent);
            finish();
        });

        bindData();

        setRefreshListener(() -> {
            bindData();
        });

    }

    private void bindData(){
        showLoading();
        mPresenter.getVoiceList(voiceEntities -> {
            hideLoading();
            adapter.setNewData(voiceEntities);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VoiceListUpdateEvent event){
        bindData();
    }
}
