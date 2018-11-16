package com.cpigeon.app.message.adapter;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.MessageEntity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class MessageHistoryAdapter extends BaseQuickAdapter<MessageEntity, BaseViewHolder> {

    public MessageHistoryAdapter() {
        super(R.layout.item_message_history_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, MessageEntity item) {
        holder.setText(R.id.number, mContext.getString(R.string.string_text_message_addressee_number, String.valueOf(item.fscount)));
        holder.setText(R.id.date,item.fssj);
        holder.setText(R.id.content, item.dxnr);
    }

    @Override
    public void setNewData(List<MessageEntity> data) {
        super.setNewData(data);
    }

    @Override
    protected String getEmptyViewText() {
        return "历史记录为空";
    }
}
