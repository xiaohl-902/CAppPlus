package com.cpigeon.app.message.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseMultiSelectAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.CommonEntity;
import com.cpigeon.app.entity.MultiSelectEntity;
import com.cpigeon.app.message.ui.common.CommonMessageQPre;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class CommonMessageAdapter extends BaseMultiSelectAdapter<CommonEntity, BaseViewHolder> {

    private OnCheckboxClickListener listener;

    private OnEditClickListener onEditClickListener;


    public CommonMessageAdapter() {
        super(R.layout.item_common_message_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, CommonEntity item) {
        super.convert(holder,item);

        TextView content = holder.findViewById(R.id.content);

        content.setText(item.dxnr);
        content.setOnClickListener(v -> {
            DialogUtils.createDialog(mContext, "详细内容"
                    , item.dxnr, "确定");
        });

        holder.findViewById(R.id.checkbox).setOnClickListener(v -> {
            listener.OnClick(holder.getAdapterPosition());
        });

        holder.findViewById(R.id.btnEdit).setOnClickListener(v -> {
            onEditClickListener.onClick(holder.getAdapterPosition());
        });

    }

    public void closeSwipe(int position){
        SwipeMenuLayout swipeLayout = (SwipeMenuLayout) this.getViewByPosition(position, R.id.swipeLayout);
        swipeLayout.smoothClose();
    }

    public interface OnCheckboxClickListener{
        void OnClick(int position);
    }

    public interface OnEditClickListener{
        void onClick(int position);
    }

    public void setOnCheckboxClickListener(OnCheckboxClickListener listener){
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }



    @Override
    public void setNewData(List<CommonEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            CommonTool.setEmptyView(this,"常用语为空");
        }
    }
}
