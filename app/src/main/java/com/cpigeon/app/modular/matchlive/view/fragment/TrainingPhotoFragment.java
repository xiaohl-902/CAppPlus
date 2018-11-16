package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.matchlive.model.bean.RPImages;
import com.cpigeon.app.modular.saigetong.view.adapter.ZHNumAdapter;
import com.cpigeon.app.utils.IntentBuilder;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class TrainingPhotoFragment extends BaseMVPFragment {

    RecyclerView recyclerView;
    ZHNumAdapter adapter;


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        List<RPImages> data = getArguments().getParcelableArrayList(IntentBuilder.KEY_DATA);

        recyclerView  = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ZHNumAdapter(getContext());
        adapter.bindToRecyclerView(recyclerView);

        adapter.setNewData(data);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }
}
