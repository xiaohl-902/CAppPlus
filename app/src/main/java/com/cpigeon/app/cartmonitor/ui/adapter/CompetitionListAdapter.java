package com.cpigeon.app.cartmonitor.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.Lists;


/**
 * Created by chenshuai on 2017/11/7.
 */

public class CompetitionListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final int TYPE_LEVEL_0 = 0;
    private static final int TYPE_LEVEL_1 = 1;

    public CompetitionListAdapter() {
        super(Lists.newArrayList());
        addItemType(TYPE_LEVEL_0, R.layout.item_competition_list_title_layout);
        addItemType(TYPE_LEVEL_1, R.layout.item_competition_list_race_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

    }
}
