package com.cpigeon.app.modular.usercenter.view.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.usercenter.model.bean.FeedBackResult;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;

import java.util.List;

/**
 * Created by chenshuai on 2017/6/22.
 */

public class FeedBackResultListAdapter extends BaseQuickAdapter<FeedBackResult, BaseViewHolder> {
    public FeedBackResultListAdapter() {
        this(null);
    }

    public FeedBackResultListAdapter(List<FeedBackResult> data) {
        super(R.layout.listitem_feedback_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackResult item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_item_content, item.getContent().replace("\n", " "));
        helper.setText(R.id.tv_item_time, item.getTime());
        helper.setText(R.id.tv_item_state, item.getState());
        helper.setTextColor(R.id.tv_item_state, Color.parseColor(item.getState().equals("已处理") ? "#fe7226" : item.getState().equals("已回复") ? "#369d32" : "#ff3a3a"));
        helper.setVisible(R.id.aciv_enter, item.getState().equals("已回复"));
    }
}
