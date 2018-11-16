package com.cpigeon.app.viewholder;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleMessageLocationFragment;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.view.ExpandTextView;
import com.cpigeon.app.view.playvideo.VideoPlayActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * Created by Zhu TingYu on 2018/5/22.
 */

public class CircleMessageHeadViewHolder extends BaseViewHolder {

    public AppCompatImageView follow;
    ExpandTextView content_text;
    public RelativeLayout rr_video;
    public RecyclerView imgsList;
    Activity activity;

    private OnCircleHeadClickListener onCircleHeadClickListener;
    String messageType;
    public LinearLayout details_location;

    public CircleMessageHeadViewHolder(Activity activity,  View itemView) {
        super(itemView);
        this.activity = activity;
        content_text = getView(R.id.content_text);
        follow = getView(R.id.follow);
        rr_video = getView(R.id.rr_video);
        imgsList = getView(R.id.imgsList);
        details_location = getView(R.id.details_location);
    }

    public void bindData(CircleMessageEntity item){
        /**
         * 个人信息
         */

        details_location.setOnClickListener(v -> {
            List<String> locations = Lists.split(item.getLo(), ",");
            if(Double.valueOf(locations.get(0)) == 0 || Double.valueOf(locations.get(1)) == 0){
                return;
            }
            CircleMessageLocationFragment.start(activity, new LatLng(Double.valueOf(locations.get(1))
                    , Double.valueOf(locations.get(0))));
        });

        if(StringValid.isStringValid(item.getLoabs())){
            setText(R.id.tv_details_location, item.getLoabs());
            details_location.setVisibility(View.VISIBLE);
        }else {
            details_location.setVisibility(View.GONE);
        }

        setText(R.id.from, StringValid.isStringValid(item.getFrom()) ? "来自" + item.getFrom() : "");

        setGlideImageViewNoRound(getContext(), R.id.head_img, item.getUserinfo().getHeadimgurl());
        getView(R.id.head_img).setOnClickListener(v -> {
            FriendsCircleHomeFragment.start(activity, item.getUserinfo().getUid(), BaseCircleMessageFragment.TYPE_ALL);
        });
        setText(R.id.user_name, item.getUserinfo().getNickname());


        /**
         * 时间
         */

        Date sendTime = DateTool.strToDateTime(item.getTime());
        try {
            if(DateTool.isToYear(sendTime)){
                if(DateTool.isToday(sendTime)){
                    setText(R.id.time, "今天");
                }else if(DateTool.isYesterday(sendTime)){
                    setText(R.id.time, "昨天");
                }else {
                    setText(R.id.time, DateTool.format(sendTime.getTime(), DateTool.FORMAT_MM_DD_HH_MM));
                }
            }else {
                setText(R.id.time, DateTool.format(sendTime.getTime(), DateTool.FORMAT_YYYY_MM_DD_HH_MM));
            }
        } catch (ParseException e) {
            setText(R.id.time, item.getTime());
        }


        content_text.setText(item.getMsg());


        if (item.isIsAttention() || item.getUserinfo().getUid() == CpigeonData.getInstance().getUserId(getContext())
                || messageType.equals(BaseCircleMessageFragment.TYPE_FOLLOW)) {
            follow.setVisibility(View.GONE);
        } else {
            follow.setVisibility(View.VISIBLE);
            follow.setOnClickListener(v -> {
                if(onCircleHeadClickListener != null){
                    onCircleHeadClickListener.followClick(item.getUserinfo().getUid());
                }
            });
        }


        if (!item.getVideo().isEmpty()) {

            rr_video.setVisibility(View.VISIBLE);
            setGlideImageViewNoRound(getContext(), R.id.video_thumb, item.getVideo().get(0).getThumburl());
            getView(R.id.rr_video).setOnClickListener(view -> {
                VideoPlayActivity.startActivity(activity, getView(R.id.rr_video),item.getVideo().get(0).getUrl());
            });
        } else {
            rr_video.setVisibility(View.GONE);
        }

        if(item.getPicture().isEmpty()){
            imgsList.setVisibility(View.GONE);
        }else {
            imgsList.setVisibility(View.VISIBLE);
        }

        itemView.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP){
                if(onCircleHeadClickListener != null){
                    onCircleHeadClickListener.itemClick();
                }
            }
            return false;
        });
    }

    public interface OnCircleHeadClickListener{
        void itemClick();
        void followClick(int userId);
    }

    public void setOnCircleHeadClickListener(OnCircleHeadClickListener onCircleHeadClickListener) {
        this.onCircleHeadClickListener = onCircleHeadClickListener;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public RecyclerView getImgsList() {
        return imgsList;
    }
}
