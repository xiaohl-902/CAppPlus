package com.cpigeon.app.modular.footsearch.ui.adapter;

import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.FootSearchEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/21.
 */

public class FootSearchAdapter extends BaseQuickAdapter<FootSearchEntity, BaseViewHolder> {

    public FootSearchAdapter() {
        super(R.layout.item_line_two_text_layout,Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, FootSearchEntity item) {
        holder.setText(R.id.title, item.foot);
        holder.setText(R.id.content, item.time);
    }

    @Override
    protected String getEmptyViewText() {
        return "没有历史记录";
    }
}
