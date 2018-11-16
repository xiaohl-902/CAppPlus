package com.cpigeon.app.circle.adpter;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.entity.CircleDetailsReplayEntity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.textspan.StringSpanUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/26.
 */

public class CircleDetailsReplayAdapter extends BaseQuickAdapter<CircleDetailsReplayEntity, BaseViewHolder> {

    private OnReplayContentClickListener onMessageContentClickListener;

    public CircleDetailsReplayAdapter() {
        super(R.layout.item_news_reply_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, CircleDetailsReplayEntity item) {

        helper.itemView.setPadding(0, 0, 0, ScreenTool.dip2px(15));

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(StringSpanUtil.addClickPart(item.hfuser.hfnickname, R.color.colorPrimary, view -> {
            FriendsCircleHomeFragment.start((Activity) mContext, item.hfuser.hfuid, "xxxx");
        }));

        if(item.hftouser == null){
            builder.append(":");
        }else {
            builder.append(" 回复 ");
            builder.append(StringSpanUtil.addClickPart(item.hftouser.nickname, R.color.colorPrimary, view -> {
                FriendsCircleHomeFragment.start((Activity) mContext, item.hftouser.uid, "xxxx");
            }));
            builder.append(" : ");
        }
        builder.append(StringSpanUtil.addClickPart(item.hfcontent, R.color.black, view -> {
            if(onMessageContentClickListener != null){
                onMessageContentClickListener.click(item, helper.getAdapterPosition());
            }
        }));


        TextView reply = helper.getView(R.id.reply);
        reply.setMovementMethod(LinkMovementMethod.getInstance());
        reply.setText(builder);
    }

    public interface OnReplayContentClickListener{
        void click(CircleDetailsReplayEntity commentListBean, int position);
    }

    public void setOnMessageContentClickListener(OnReplayContentClickListener listener) {
        this.onMessageContentClickListener = listener;
    }
}
