package com.cpigeon.app.home.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/2.
 */

public class HomeAdAdapter extends BaseQuickAdapter<NewsEntity, BaseViewHolder> {

    public HomeAdAdapter() {
        super(R.layout.item_home_ad_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsEntity item) {
        holder.setText(R.id.content, item.title);
    }
}
