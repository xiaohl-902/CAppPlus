package com.cpigeon.app.circle.adpter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.entity.JPushCircleEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/31.
 */

public class CircleNewMessageAdapter extends BaseQuickAdapter<JPushCircleEntity, BaseViewHolder> {

    public CircleNewMessageAdapter() {
        super(R.layout.item_circle_new_message_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, JPushCircleEntity item) {
        helper.setGlideImageView(mContext, R.id.headImage, item.getHeadimgurl());
        helper.setText(R.id.name, item.getPlnc());
        helper.setText(R.id.commentContent, item.getPlnr());
        helper.setText(R.id.time, item.getPlsj());
        helper.setText(R.id.commentedContent, item.getBplnr());

        helper.itemView.setOnClickListener(v -> {
            CircleMessageDetailsNewActivity.startActivity((Activity) mContext, item.getMid()
                    , BaseCircleMessageFragment.TYPE_FOLLOW, helper.getAdapterPosition());
        });
    }
}
