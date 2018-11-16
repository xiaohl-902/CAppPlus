package com.cpigeon.app.message.ui.order.adpter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.entity.MessageOrderEntity;
import com.cpigeon.app.viewholder.OderInfoViewHolder;
import com.cpigeon.app.utils.CommonTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class RechargeHistoryAdapter extends BaseQuickAdapter<MessageOrderEntity, OderInfoViewHolder> {

    public RechargeHistoryAdapter() {
        super(R.layout.item_order_info_head_layout);
    }

    @Override
    protected void convert(OderInfoViewHolder holder, MessageOrderEntity item) {
        holder.orderId.setText(item.ddbh);
        holder.orderContent.setText(item.ddxm);
        holder.orderTime.setText(item.cjsj);
        holder.orderPrice.setText(String.valueOf(item.money));

    }

    @Override
    public void setNewData(List<MessageOrderEntity> data) {
        getRecyclerView().getRecycledViewPool().clear();
        notifyDataSetChanged();
        if(data.isEmpty()){
            CommonTool.setEmptyView(this, "没有充值记录");
        }
        super.setNewData(data);
     }
}
