package com.cpigeon.app.circle.adpter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleMessageDetailsThumbAdapter extends BaseQuickAdapter<CircleMessageEntity.PraiseListBean, BaseViewHolder> {

    public CircleMessageDetailsThumbAdapter() {
        super(R.layout.item_circle_message_details_thumb_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleMessageEntity.PraiseListBean item) {

        if(mData.size() - 1 == holder.getAdapterPosition()){
            holder.setVisible(R.id.line, false);
        }else {
            holder.setVisible(R.id.line, true);
        }
        holder.setGlideImageViewNoRound(mContext,R.id.head_img, item.getHeadimgurl());
        holder.getView(R.id.head_img).setOnClickListener(v -> {
            FriendsCircleHomeFragment.start((Activity) mContext, item.getUid(), BaseCircleMessageFragment.TYPE_FOLLOW);
        });
        holder.setText(R.id.user_name, item.getNickname());
        holder.setText(R.id.time,item.getPraisetime());
    }

    @Override
    protected String getEmptyViewText() {
        return "暂无点赞";

    }

    @Override
    protected int getEmptyViewImage() {
        return R.drawable.ic_circle_messag_emptye;
    }
}
