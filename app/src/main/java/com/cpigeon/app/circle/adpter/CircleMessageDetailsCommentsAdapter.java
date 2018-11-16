package com.cpigeon.app.circle.adpter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.entity.CircleDetailsReplayEntity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.textspan.StringSpanUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class CircleMessageDetailsCommentsAdapter extends BaseQuickAdapter<CircleMessageEntity.CommentListBean, BaseViewHolder> {
    CircleMessagePre circleMessagePre;

    public CircleMessageDetailsCommentsAdapter(CircleMessagePre circleMessagePre) {
        super(R.layout.item_circle_message_details_comment_layout, Lists.newArrayList());
        this.circleMessagePre = circleMessagePre;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleMessageEntity.CommentListBean item) {


        if(mData.size() - 1 == holder.getAdapterPosition()){
            holder.setVisible(R.id.line, false);
        }else {
            holder.setVisible(R.id.line, true);
        }

        holder.setGlideImageViewNoRound(mContext, R.id.head_img, item.getUser().getHeadimgurl());
        holder.getView(R.id.head_img).setOnClickListener(v -> {
            FriendsCircleHomeFragment.start((Activity) mContext, item.getUser().getUid(), BaseCircleMessageFragment.TYPE_FOLLOW);
        });
        holder.setText(R.id.user_name, item.getUser().getNickname());
        holder.setText(R.id.time, item.getTime());

        holder.setText(R.id.content, item.getContent());

        RecyclerView commentList = holder.getView(R.id.list);


        CircleDetailsReplayAdapter adapter = (CircleDetailsReplayAdapter) commentList.getAdapter();

        if (adapter == null) {
            adapter = new CircleDetailsReplayAdapter();
            commentList.setAdapter(adapter);
        }



        adapter.setNewData(item.getHflist() == null ? Lists.newArrayList() : item.getHflist());
        CircleDetailsReplayAdapter finalAdapter2 = adapter;

        holder.setOnclick(R.id.ll1,v -> {
            if(item.getUser().getUid() == CpigeonData.getInstance().getUserId(mContext)){
                DialogUtils.createDialogWithLeft(circleMessagePre.getActivity(), "是否删除该评论",sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    circleMessagePre.commentId = item.getId();
                    circleMessagePre.getBaseActivity().showLoading();
                    circleMessagePre.deleteComment(s -> {
                        circleMessagePre.getBaseActivity().hideLoading();
                        ToastUtil.showShortToast(mContext, "删除成功");
                        remove(holder.getAdapterPosition());
                        ((CircleMessageDetailsNewActivity)circleMessagePre.getActivity()).postCircleEvent();
                    });
                });
            }else {
                InputCommentDialog dialog = new InputCommentDialog();
                dialog.setHint(CpigeonData.getInstance().getUserInfo().getNickname()
                        + " 回复 " + item.getUser().getNickname());
                dialog.setPushClickListener(editText -> {
                    circleMessagePre.messageId = circleMessagePre.circleList.get(0).getMid();
                    circleMessagePre.commentId = item.getId();
                    circleMessagePre.previousId = item.getId();
                    circleMessagePre.commentContent = editText.getText().toString();
                    circleMessagePre.addComment(newComment -> {
                        dialog.dismiss();
                        finalAdapter2.addData(finalAdapter2.getData().size(), getReplayEntity(newComment));
                        commentList.setVisibility(View.VISIBLE);
                        ((CircleMessageDetailsNewActivity)circleMessagePre.getActivity()).postCircleEvent();
                    });
                });
                dialog.show(circleMessagePre.getActivity().getFragmentManager(), "InputCommentDialog");
            }
        });




        if (item.getHflist() != null && !item.getHflist().isEmpty()) {
            commentList.setVisibility(View.VISIBLE);
            commentList.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            adapter.setOnMessageContentClickListener((commentListBean, position) -> {

                if(commentListBean.hfuser.hfuid == CpigeonData.getInstance().getUserId(mContext)){
                    DialogUtils.createDialogWithLeft(circleMessagePre.getActivity(), "是否删除该评论",sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        circleMessagePre.commentId = Integer.parseInt(commentListBean.hfid);
                        circleMessagePre.getBaseActivity().showLoading();
                        circleMessagePre.deleteComment(s -> {
                            circleMessagePre.getBaseActivity().hideLoading();
                            ToastUtil.showShortToast(mContext, "删除成功");
                            finalAdapter2.remove(position);
                        });
                    });
                }else {
                    InputCommentDialog dialog = new InputCommentDialog();
                    dialog.setHint(CpigeonData.getInstance().getUserInfo().getNickname()
                            + " 回复 " + commentListBean.hfuser.hfnickname);
                    CircleDetailsReplayAdapter finalAdapter1 = finalAdapter2;
                    dialog.setPushClickListener(editText -> {
                        ((BaseActivity)circleMessagePre.getActivity()).showLoading();
                        circleMessagePre.messageId = circleMessagePre.circleList.get(0).getMid();
                        circleMessagePre.commentId = item.getId();
                        circleMessagePre.previousId = Integer.parseInt(commentListBean.hfid);
                        circleMessagePre.commentContent = editText.getText().toString();
                        CircleDetailsReplayAdapter finalAdapter = finalAdapter1;
                        circleMessagePre.addComment(newComment -> {
                            ((BaseActivity)circleMessagePre.getActivity()).hideLoading();
                            finalAdapter.addData(finalAdapter.getData().size(), getReplayEntity(newComment));
                            ((CircleMessageDetailsNewActivity)circleMessagePre.getActivity()).postCircleEvent();
                            dialog.dismiss();
                        });
                    });
                    dialog.show(circleMessagePre.getActivity().getFragmentManager(), "InputCommentDialog");
                }

            });

            commentList.setFocusableInTouchMode(false);
        }else {
            commentList.setVisibility(View.GONE);
        }


    }

    @Override
    protected String getEmptyViewText() {
        return "暂无评论";
    }

    public CircleDetailsReplayEntity getReplayEntity(CircleMessageEntity.CommentListBean  newComment){
        CircleDetailsReplayEntity circleDetailsReplayEntity = new CircleDetailsReplayEntity();
        circleDetailsReplayEntity. hfid = String.valueOf(newComment.getId());//回复内容的ID
        circleDetailsReplayEntity. hfuid = String.valueOf(newComment.getUser().getUid());//回复人的会员ID
        circleDetailsReplayEntity.  hfcontent = newComment.getContent();//回复内容
        circleDetailsReplayEntity.  hfnickname = newComment.getUser().getNickname();//回复人昵称
        circleDetailsReplayEntity. hfheadimgurl = newComment.getUser().getHeadimgurl();//回复人头像URL
        circleDetailsReplayEntity. hftime = newComment.getTime();
        CircleDetailsReplayEntity.HFUserEntity hfuser = new CircleDetailsReplayEntity.HFUserEntity();
        hfuser.hfuid = newComment.getUser().getUid();//回复人会员ID；
        hfuser.hfnickname = newComment.getUser().getNickname();//回复人昵称；
        hfuser.hfheadimgurl = newComment.getUser().getHeadimgurl();//回复人头像URL
        circleDetailsReplayEntity.hfuser = hfuser;

        CircleDetailsReplayEntity.HFToUserEntity hftouser = new CircleDetailsReplayEntity.HFToUserEntity();
        hftouser.uid = newComment.getTouser().getUid();//回复人会员ID；
        hftouser.nickname = newComment.getTouser().getNickname();//回复人昵称；
        hftouser.headimgurl = newComment.getTouser().getHeadimgurl();//回复人头像URL
        circleDetailsReplayEntity.hftouser = hftouser;
        return circleDetailsReplayEntity;
    }

    @Override
    protected int getEmptyViewImage() {
        return R.drawable.ic_circle_messag_emptye;
    }

}
