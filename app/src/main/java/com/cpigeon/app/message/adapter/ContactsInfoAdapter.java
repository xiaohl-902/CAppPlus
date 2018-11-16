package com.cpigeon.app.message.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseMultiSelectAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.ContactsEntity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class ContactsInfoAdapter extends BaseMultiSelectAdapter<ContactsEntity,BaseViewHolder> {

    public ContactsInfoAdapter() {
        super(R.layout.item_contacts_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactsEntity item) {
        super.convert(holder, item);
        holder.setText(R.id.title, item.xingming);
        holder.setText(R.id.number,item.sjhm);
    }

    public void setLoadMore(boolean isEnd) {
            if (isEnd) this.loadMoreEnd();
            else
                this.loadMoreComplete();
    }

    @Override
    public void setNewData(List<ContactsEntity> data) {
        getRecyclerView().getRecycledViewPool().clear();
        notifyDataSetChanged();
        if(data.isEmpty()){
            CommonTool.setEmptyView(this, "暂时没有联系人");
        }
        super.setNewData(data);
    }
}
