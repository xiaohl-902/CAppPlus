package com.cpigeon.app.viewholder;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.CpigeonData;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleMessageCommentViewHolder extends BaseViewHolder {

    TextView comment;
    TextView input;
    TextView thumb;
    ImageView image_thumb;

    Animation animation;
    LinearLayout ll_thumb;
    String hint;

    Activity activity;

    private OnCircleMessageCommentListener onCircleMessageCommentListener;

    public CircleMessageCommentViewHolder(Activity activity,  View itemView) {
        super(itemView);
        this.activity = activity;
        comment = getView(R.id.comment);
        input = getView(R.id.input);
        thumb = getView(R.id.thumb);
        ll_thumb = getView(R.id.ll_thumb);
        image_thumb = getView(R.id.image_thumb);
        animation = AnimationUtils.loadAnimation(MyApp.getInstance().getBaseContext(), R.anim.anim_sign_box_rock);
        bindUi();
    }

    private void bindUi(){
        ll_thumb.setOnClickListener(v -> {
            if(onCircleMessageCommentListener != null){
                onCircleMessageCommentListener.thumb(v);
            }
        });

        input.setOnClickListener(v -> {
            if(onCircleMessageCommentListener != null){
                InputCommentDialog dialog = new InputCommentDialog();
                dialog.setHint(hint);
                dialog.setPushClickListener(content -> {
                    onCircleMessageCommentListener.comment(content, dialog);
                });
                dialog.show(activity.getFragmentManager(), "InputComment");
            }
        });
    }

    public void bindData(CircleMessageEntity entity) {
        comment.setText(String.valueOf(entity.getCommentList().size()));
        thumb.setText(String.valueOf(entity.getPraiseList().size()));
        if (entity.getPraiseList() != null && entity.getPraiseList().size() > 0) {

            for (CircleMessageEntity.PraiseListBean praiseListBean : entity.getPraiseList()) {

                if (praiseListBean.getUid() == CpigeonData.getInstance().getUserId(activity) && praiseListBean.getIsPraise() == 1) {
                    setThumb(true);
                    entity.setThumb();
                    break;
                } else {
                    setThumb(false);
                    entity.setCancelThumb();
                }
            }
        } else {
            setThumb(false);
            entity.setCancelThumb();
        }
    }

    public void setThumb(boolean isThumb) {
        setImageDrawable(R.id.image_thumb, isThumb ? getDrawable(R.mipmap.ic_thumbs_up_new) : getDrawable(R.mipmap.ic_thumbs_not_up_new));
    }

    public void setThumbAnimation(boolean isThumb) {
        if(isThumb){
            image_thumb.startAnimation(animation);
        }
    }

    public interface OnCircleMessageCommentListener{
        void thumb(View imageView);
        void comment(EditText editText, InputCommentDialog dialog);
    }

    public void setOnCircleMessageCommentListener(OnCircleMessageCommentListener onCircleMessageCommentListener) {
        this.onCircleMessageCommentListener = onCircleMessageCommentListener;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
