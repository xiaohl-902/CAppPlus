package com.cpigeon.app.pigeonnews.adpter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.NewsCommentEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.pigeonnews.presenter.NewsCommentsPre;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringUtil;
import com.cpigeon.app.view.LinearLayoutForRecyclerView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class NewsCommentAdapter extends BaseQuickAdapter<NewsCommentEntity, BaseViewHolder> {

    private OnCommunicationListener listener;

    NewsCommentsPre mPresenter;

    public NewsCommentAdapter(NewsCommentsPre commentsPre) {
        super(R.layout.item_news_comment_layout, Lists.newArrayList());
        mPresenter = commentsPre;
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsCommentEntity item) {
        holder.setSimpleImageView(R.id.icon, item.headurl);
        holder.setText(R.id.name, StringUtil.toUpperCase(item.nicheng));
        holder.setText(R.id.time, DateTool.format(item.time, DateTool.FORMAT_DATETIME));

        TextView comment = holder.getView(R.id.comment);
        TextView thumb = holder.getView(R.id.thumb);

        comment.setOnClickListener(v -> {
            listener.comment(item, holder.getAdapterPosition());
        });

        holder.getView(R.id.ll_thumb).setOnClickListener(v -> {
            listener.thumb(item, holder.getAdapterPosition());
        });

        comment.setText(String.valueOf(item.replycount));
        thumb.setText(String.valueOf(item.dianzan));


        if(item.isThumb()){
            holder.setImageResource(R.id.image_thumb, R.mipmap.ic_thumbs_up_new);
        }else {
            holder.setImageResource(R.id.image_thumb, R.mipmap.ic_thumbs_not_up_new);
        }

        holder.setText(R.id.content, item.content);

        LinearLayoutForRecyclerView list = holder.getView(R.id.reply_list);
        TextView textExpand = holder.getView(R.id.expand);

        ReplyAdapter replyAdapter = new ReplyAdapter(mContext);

        if(item.reply != null && !item.reply.isEmpty()){
            holder.setViewVisible(R.id.img1,View.VISIBLE);
            holder.getView(R.id.rl_reply_list).setVisibility(View.VISIBLE);

            if(item.reply.size() > 4){
                textExpand.setVisibility(View.VISIBLE);
                textExpand.setOnClickListener(v -> {
                    item.isExpand = !item.isExpand;
                    textExpand.setText(item.isExpand ? "展开查看更多" : "收起更多评论");
                    notifyItemChanged(holder.getAdapterPosition());
                });
            }else {
                textExpand.setVisibility(View.GONE);
            }

            replyAdapter.setData(item.isExpand ? item.reply : getTopX(item.reply));
            replyAdapter.setOnItemReplyClickListenerListener((entity, position, content,dialog) -> {
                mPresenter.commentId = item.id;
                mPresenter.content = content;
                mPresenter.replyId = entity.cid;
                mPresenter.replyComment(s -> {
                    item.reply.add(position + 1, replyAdapter.getNewEntity(position, content));
                    dialog.closeDialog();
                    item.replycount += 1;
                    notifyItemChanged(holder.getAdapterPosition());
                });
            });

            list.setAdapter(replyAdapter);
        }else {
            holder.setViewVisible(R.id.img1,View.GONE);
            holder.setViewVisible(R.id.rl_reply_list, View.GONE);
        }

    }

    private List<NewsCommentEntity> getTopX(List<NewsCommentEntity> data){

        List<NewsCommentEntity> rdata = data;

        if(data.size() > 4){
            rdata = data.subList(0, 3);
        }
        return rdata;
    }

    @Override
    protected String getEmptyViewText() {
        return "当前暂无评论，快快抢沙发";
    }

    @Override
    protected int getEmptyViewImage() {
        return R.drawable.ic_comment_empty_view;
    }

    public interface OnCommunicationListener{
        void thumb(NewsCommentEntity entity, int position);
        void comment(NewsCommentEntity entity, int position);
    }

    public void setListener(OnCommunicationListener listener) {
        this.listener = listener;
    }
}
