package com.cpigeon.app.circle.adpter;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.circle.ui.UserInfoFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.textspan.StringSpanUtil;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class MessageReplayAdapter extends BaseQuickAdapter<CircleMessageEntity.CommentListBean, BaseViewHolder> {

    String messageType;

    private OnMessageContentClickListener onMessageContentClickListener;

    public MessageReplayAdapter(String messageType) {
        super(R.layout.item_news_reply_layout, Lists.newArrayList());
        this.messageType = messageType;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleMessageEntity.CommentListBean item) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(StringSpanUtil.addClickPart(item.getUser().getNickname(), R.color.colorPrimary,view -> {
            FriendsCircleHomeFragment.start((Activity) mContext, item.getUser().getUid(), messageType);
        }));

        if(item.getTouser() == null){
            builder.append(":");
        }else {
            builder.append(" 回复 ");
            builder.append(StringSpanUtil.addClickPart(item.getTouser().getNickname(), R.color.colorPrimary, view -> {
                FriendsCircleHomeFragment.start((Activity) mContext, item.getTouser().getUid(), messageType);
            }));
            builder.append(" : ");
        }
        builder.append(StringSpanUtil.addClickPart(item.getContent(), R.color.black, view -> {
            if(onMessageContentClickListener != null){
                onMessageContentClickListener.click(item, holder.getAdapterPosition());
            }
        }));


        TextView reply = holder.getView(R.id.reply);
        reply.setMovementMethod(LinkMovementMethod.getInstance());
        reply.setText(builder);
    }

    public interface OnMessageContentClickListener{
        void click(CircleMessageEntity.CommentListBean commentListBean, int position);
    }

    public void setOnMessageContentClickListener(OnMessageContentClickListener listener) {
        this.onMessageContentClickListener = listener;
    }
}
