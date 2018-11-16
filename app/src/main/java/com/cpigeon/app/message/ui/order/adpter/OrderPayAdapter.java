package com.cpigeon.app.message.ui.order.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class OrderPayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private List<Integer> icons;

    public OrderPayAdapter() {
        super(R.layout.item_line_text_with_icon_layout, Lists.newArrayList());
        icons = Lists.newArrayList(
                R.drawable.svg_ic_pay_balance,
                R.drawable.svg_ic_pay_weixin

        );
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setImageResource(R.id.icon, icons.get(holder.getAdapterPosition()));
        holder.setText(R.id.title,item);
    }
}
