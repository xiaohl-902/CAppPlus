package com.cpigeon.app.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class SocialSnsViewHolder extends BaseViewHolder{

    AppCompatImageView thumb;
    AppCompatImageView comment;
    AppCompatImageView share;

    TextView thumbNumber;
    TextView commentNumber;
    View view_line;

    OnSocialListener listener;


    Activity activity;

    String hint;
    Animation animation;

    LinearLayout ll_thumb;
    LinearLayout ll_comment;


    public interface OnSocialListener{
        void thumb(View imageView);
        void comment(EditText editText, InputCommentDialog dialog);
        void share(View imageView);
    }

    public SocialSnsViewHolder(Activity activity, View itemView, String hint) {
        super(itemView);
        this.activity = activity;
        this.hint = hint;
        thumb = getView(R.id.thumb);
        comment = getView(R.id.comment);
        share = getView(R.id.share);
        thumbNumber = getView(R.id.thumbNumber);
        commentNumber = getView(R.id.commentNumber);
        view_line = getView(R.id.view_line);
        ll_thumb = getView(R.id.ll_thumb);
        ll_comment = getView(R.id.ll_comment);
        animation = AnimationUtils.loadAnimation(MyApp.getInstance().getBaseContext(), R.anim.anim_sign_box_rock);

        bindUi();
    }

    private void bindUi() {
        ll_thumb.setOnClickListener(v -> {
            listener.thumb(v);
        });

        ll_comment.setOnClickListener(v -> {
            InputCommentDialog dialog = new InputCommentDialog();
            dialog.setHint(hint);
            dialog.setPushClickListener(content -> {
                listener.comment(content, dialog);
            });
            dialog.show(activity.getFragmentManager(), "InputComment");
        });

        share.setOnClickListener(v -> {
            listener.share(v);
        });
    }

    public void bindData(CircleMessageEntity entity){
        thumbNumber.setText(String.valueOf(entity.getPraiseList().size()));
        commentNumber.setText(String.valueOf(entity.getCommentList().size()));
        if(entity.getCommentList().size() == 0){
            view_line.setVisibility(View.GONE);
        }else {
            view_line.setVisibility(View.VISIBLE);
        }
    }

    public void setOnSocialListener(OnSocialListener listener) {
        this.listener = listener;
    }

    public void setThumb(boolean isThumb){
        setImageDrawable(R.id.thumb, isThumb ? getDrawable(R.mipmap.ic_thumbs_up_new) : getDrawable(R.mipmap.ic_thumbs_not_up_new));
    }

    public void setThumbAnimation(boolean isThumb){
        if(isThumb){
            thumb.startAnimation(animation);
        }
    }

    public void setComment(boolean isComment){
        setImageDrawable(R.id.thumb, isComment ? getDrawable(R.mipmap.ic_new_comment_select) : getDrawable(R.mipmap.ic_new_comment));
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
