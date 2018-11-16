package com.cpigeon.app.modular.footsearch.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/21.
 */

public class FootSearchHelpAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public FootSearchHelpAdapter() {
        super(R.layout.item_foot_search_help_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.content,item);
        holder.setText(R.id.order, "0" + (holder.getAdapterPosition()+1));
    }
}
