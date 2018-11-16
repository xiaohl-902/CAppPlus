package com.cpigeon.app.circle.adpter;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseMultiSelectAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.MultiSelectEntity;
import com.cpigeon.app.entity.ReportCircleContentEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class ReportCircleMessageAdapter extends BaseMultiSelectAdapter <ReportCircleContentEntity, BaseViewHolder>{
    public ReportCircleMessageAdapter() {
        super(R.layout.item_reprot_circle_message_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ReportCircleContentEntity item) {
        super.convert(holder, item);
        holder.setText(R.id.content, item.content);
    }
}
