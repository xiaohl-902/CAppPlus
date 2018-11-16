package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.adpter.CircleMessageDetailsThumbAdapter;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.event.CircleMessageDetailsEvent;
import com.cpigeon.app.event.ContactsEvent;
import com.cpigeon.app.utils.ScreenTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleMessageDetailsThumbFragment extends BaseMVPFragment<CircleMessagePre> {

    RecyclerView recyclerView;
    CircleMessageDetailsThumbAdapter adapter;

    @Override
    protected CircleMessagePre initPresenter() {
        return ((CircleMessageDetailsNewActivity)getActivity()).getPre();
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
        EventBus.getDefault().register(this);
        recyclerView = findViewById(R.id.list);
        refreshLayout.setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CircleMessageDetailsThumbAdapter();
        adapter.bindToRecyclerView(recyclerView);

        adapter.setNewData(mPresenter.getThumbs());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleMessageDetailsEvent event){
        if(event.type == CircleMessageDetailsEvent.TYPE_THUMB){
            adapter.setNewData(mPresenter.getThumbs());
        }
    }
}
