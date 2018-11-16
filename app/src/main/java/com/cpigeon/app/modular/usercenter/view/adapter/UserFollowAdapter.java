package com.cpigeon.app.modular.usercenter.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserFollowAdapter extends BaseQuickAdapter<UserFollow, BaseViewHolder> {

//    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public UserFollowAdapter() {
        super(R.layout.listitem_user_follow, null);
    }

    public UserFollowAdapter(List<UserFollow> data) {
        super(R.layout.listitem_user_follow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFollow item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_item_follow_display, item.getDisplay());
        helper.setText(R.id.tv_item_follow_time, item.getTime());
        helper.addOnClickListener(R.id.layout_item_follow_status);
//        helper.setText(R.id.tv_item_name, item.getItem().replace("积分", "鸽币"));
//        helper.setText(R.id.tv_item_time, item.getTime());
//        String str = String.format(item.getScore() > 0 ? "+%d" : "-%d", Math.abs(item.getScore()));
//        helper.setText(R.id.tv_item_score, str);
    }
}
