package com.cpigeon.app.circle.adpter;

import com.amap.api.services.core.PoiItem;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class ChooseLocationAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    public ChooseLocationAdapter() {
        super(R.layout.item_contacts_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, PoiItem item) {
        holder.setText(R.id.title, item.getTitle())
                .setText(R.id.number, item.getSnippet());
    }
}
