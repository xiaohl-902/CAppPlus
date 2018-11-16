package com.cpigeon.app.circle.adpter;

import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.presenter.HideMessageListPre;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.entity.HideMessageEntity;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class ShieldDynamicAdapter extends BaseQuickAdapter<HideMessageEntity, BaseViewHolder> {

    HideMessageListPre mPre;

    public ShieldDynamicAdapter(HideMessageListPre mPre) {
        super(R.layout.item_shield_dynamic_layout, Lists.newArrayList());
        this.mPre = mPre;
    }

    @Override
    protected void convert(BaseViewHolder holder, HideMessageEntity item) {
        holder.setSimpleImageView(R.id.head_img, item.getMsgInfo().getUserinfo().getHeadimgurl());
        holder.setText(R.id.user_name, item.getMsgInfo().getUserinfo().getNickname());
        holder.setText(R.id.content,item.getMsgInfo().getMsg());

        TextView statue = holder.getView(R.id.state);
        statue.setText("取消屏蔽");
        statue.setOnClickListener(v -> {
            DialogUtils.createDialogWithLeft(mContext,"是否取消屏蔽消息", sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                mPre.messageId = item.getMsgInfo().getMid();
                mPre.setIsHide(false);
                mPre.hideMessage(apiResponse -> {
                    if(apiResponse.status){
                        remove(holder.getAdapterPosition());
                    }else ((BaseActivity) mContext).error(apiResponse.msg);
                });
            });
        });
    }

    @Override
    protected String getEmptyViewText() {
        return "暂时没有数据";
    }
}
