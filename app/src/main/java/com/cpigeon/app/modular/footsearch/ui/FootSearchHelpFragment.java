package com.cpigeon.app.modular.footsearch.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.modular.footsearch.ui.adapter.FootSearchHelpAdapter;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/21.
 */

public class FootSearchHelpFragment extends BaseMVPFragment {

    RecyclerView recyclerView;
    FootSearchHelpAdapter adapter;
    List<String> help;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_foot_search_help_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        help = Lists.newArrayList(getString(R.string.string_foot_search_help1)
                ,getString(R.string.string_foot_search_help2)
                ,getString(R.string.string_foot_search_help3));
        setTitle("足环查询规则");

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FootSearchHelpAdapter();
        recyclerView.setAdapter(adapter);
        initTopImg();

        adapter.setNewData(help);
    }

    private void initTopImg() {
        AppCompatImageView imageView = findViewById(R.id.icon);

        int w = ScreenTool.getScreenWidth(getContext());
        int h = (int) (w * 0.46f);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w,h);
        imageView.setLayoutParams(layoutParams);
    }


}
