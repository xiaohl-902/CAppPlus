package com.cpigeon.app.circle.adpter;

import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.presenter.HideFriendPre;
import com.cpigeon.app.circle.ui.ShieldFriendFragment;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.entity.CircleFriendEntity;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShieldFriendAdapter extends BaseQuickAdapter<CircleFriendEntity, BaseViewHolder> {

    String type;
    HideFriendPre mPre;

    public ShieldFriendAdapter(String type, HideFriendPre mPre) {
        super(R.layout.item_shield_and_blacklist_layout, Lists.newArrayList());
        this.type = type;
        this.mPre = mPre;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleFriendEntity item) {
        holder.setSimpleImageView(R.id.head_img, item.getUserinfo().getHeadimgurl());
        holder.setText(R.id.user_name, item.getUserinfo().getNickname());
        TextView statue = holder.getView(R.id.state);

        if(type.equals(ShieldFriendFragment.TYPE_SHIELD)){
            statue.setText("取消屏蔽");
            statue.setOnClickListener(v -> {
                DialogUtils.createDialogWithLeft(mContext, "是否取消屏蔽",sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    mPre.setIsHide(false);
                    mPre.hideUserId = item.getUserinfo().getUid();
                    mPre.hideUser(apiResponse -> {
                        if(apiResponse.status){
                            remove(holder.getAdapterPosition());
                        }else ((BaseActivity) mContext).error(apiResponse.msg);
                    });
                });

            });
        }else {
            statue.setText("取消拉黑");
            statue.setOnClickListener(v -> {
                DialogUtils.createDialogWithLeft(mContext, "是否取消拉黑",sweetAlertDialog -> {
                  sweetAlertDialog.dismiss();
                  mPre.setIsHide(false);
                  mPre.blackUserId = item.getUserinfo().getUid();
                  mPre.addBlackList(apiResponse -> {
                      if(apiResponse.status){
                          remove(holder.getAdapterPosition());
                      }else ((BaseActivity) mContext).error(apiResponse.msg);
                  });
                });
            });
        }


    }

    @Override
    protected String getEmptyViewText() {
        if(type.equals(ShieldFriendFragment.TYPE_SHIELD)){
            return "暂时没有屏蔽好友";
        }else {
            return "暂时没有拉黑好友";
        }
    }
}
