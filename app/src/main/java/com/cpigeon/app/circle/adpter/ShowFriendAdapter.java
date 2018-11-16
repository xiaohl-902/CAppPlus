package com.cpigeon.app.circle.adpter;

import android.support.v7.widget.AppCompatImageView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.presenter.FriendPre;
import com.cpigeon.app.circle.ui.BaseShowFriendFragment;
import com.cpigeon.app.entity.CircleFriendEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShowFriendAdapter extends BaseQuickAdapter<CircleFriendEntity, BaseViewHolder> {

    String type;

    public ShowFriendAdapter(String type) {
        super(R.layout.item_show_friend_layout, Lists.newArrayList());
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleFriendEntity item) {
        holder.setText(R.id.time, item.getTime());
        holder.setSimpleImageView(R.id.head_img, item.getUserinfo().getHeadimgurl());
        holder.setText(R.id.user_name, item.getUserinfo().getNickname());

        AppCompatImageView isFollow = holder.getView(R.id.isFollow);

        if(type.equals(BaseShowFriendFragment.TYPE_FANS)){
            if(item.isIsmutual()){
                 isFollow.setImageResource(R.mipmap.ic_circle_followed);
            }else {
                isFollow.setImageResource(R.mipmap.ic_circle_follow);
            }
        }

    }

    @Override
    protected String getEmptyViewText() {
        return "暂时没有朋友";
    }
}
