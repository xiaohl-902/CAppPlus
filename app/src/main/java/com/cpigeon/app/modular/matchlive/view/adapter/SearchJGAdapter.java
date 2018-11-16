package com.cpigeon.app.modular.matchlive.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.MatchJGEntity;
import com.cpigeon.app.utils.Lists;


/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class SearchJGAdapter extends BaseQuickAdapter<MatchJGEntity, BaseViewHolder> {

    public SearchJGAdapter() {
        super(R.layout.listitem_report_info, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, MatchJGEntity item) {
        holder.setText(R.id.report_info_item_mc, String.valueOf(item.getOrder()));
        holder.setText(R.id.report_info_item_xm, item.getName());
        holder.setText(R.id.report_info_item_hh, item.getFoot());
    }
}
