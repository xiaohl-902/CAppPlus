package com.cpigeon.app.circle.adpter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amap.api.maps.model.LatLng;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.circle.ui.CircleMessageLocationFragment;
import com.cpigeon.app.circle.ui.DialogHideCircleFragment;
import com.cpigeon.app.circle.ui.DialogUserDeleteFragment;
import com.cpigeon.app.circle.ui.FriendsCircleHomeFragment;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.event.FriendFollowEvent;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.view.ExpandTextView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.view.playvideo.VideoPlayActivity;
import com.cpigeon.app.viewholder.SocialSnsViewHolder;
import com.wx.goodview.GoodView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/15.
 */

public class CircleMessageAdapter extends BaseQuickAdapter<CircleMessageEntity, BaseViewHolder> {
    GoodView goodView;
    BaseActivity activity;
    CircleMessagePre mPre;
    DialogHideCircleFragment dialogHideCircleFragment;
    DialogUserDeleteFragment dialogUserDeleteFragment;
    private ShareDialogFragment share;
    private SmallVideoHelper smallVideoHelper;

    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;


    int dataType;
    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_VIDEO = 1;

    private String messageType;
    private String homeMessageType;

    public CircleMessageAdapter(Activity activity, String type, String homeMessageType) {
        super(R.layout.item_circle_message_2_layout, Lists.newArrayList());
        this.goodView = new GoodView(activity);
        this.activity = (BaseActivity) activity;
        dialogHideCircleFragment = new DialogHideCircleFragment();
        dialogUserDeleteFragment = new DialogUserDeleteFragment();
        this.messageType = type;
        this.homeMessageType = homeMessageType;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleMessageEntity item) {

       /* holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_UP) {
                    CircleMessageDetailsNewActivity.startActivity(activity, item.getMid(),messageType, holder.getAdapterPosition());
                }
                return false;
            }
        });*/

        holder.getView(R.id.ll1).setOnClickListener(v -> {
            CircleMessageDetailsNewActivity.startActivity(activity, item.getMid(),messageType, holder.getAdapterPosition());
        });

        holder.setText(R.id.from, StringValid.isStringValid(item.getFrom()) ? "来自" + item.getFrom() : "");

        holder.setGlideImageViewNoRound(mContext, R.id.head_img, item.getUserinfo().getHeadimgurl());

        if(!BaseCircleMessageFragment.TYPE_FRIEND.equals(messageType)){
            holder.getView(R.id.head_img).setOnClickListener(v -> {
                FriendsCircleHomeFragment.start(activity, item.getUserinfo().getUid(), messageType);
            });
        }

        holder.setText(R.id.user_name, item.getUserinfo().getNickname());

        Date sendTime = DateTool.strToDateTime(item.getTime());

        try {
            if(DateTool.isToYear(sendTime)){
                if(DateTool.isToday(sendTime)){
                    holder.setText(R.id.time, "今天");
                }else if(DateTool.isYesterday(sendTime)){
                    holder.setText(R.id.time, "昨天");
                }else {
                    holder.setText(R.id.time, DateTool.format(sendTime.getTime(), DateTool.FORMAT_MM_DD_HH_MM));
                }
            }else {
                holder.setText(R.id.time, DateTool.format(sendTime.getTime(), DateTool.FORMAT_YYYY_MM_DD_HH_MM));
            }
        } catch (ParseException e) {
            holder.setText(R.id.time, item.getTime());
        }


        ExpandTextView content = holder.getView(R.id.content_text);
        content.setText(item.getMsg());

        content.setOnClickListener(v -> {
            CircleMessageDetailsNewActivity.startActivity(activity, item.getMid(),messageType, holder.getAdapterPosition());
        });



        /**
         * 地址
         */
        if (StringValid.isStringValid(item.getLoabs())) {
            holder.getView(R.id.ll_loaction).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_message_location, item.getLoabs());
            holder.setOnclick(R.id.ll_loaction, v -> {
                List<String> locations = Lists.split(item.getLo(), ",");
                if(Double.valueOf(locations.get(0)) == 0 || Double.valueOf(locations.get(1)) == 0){
                    return;
                }
                CircleMessageLocationFragment.start(activity, new LatLng(Double.valueOf(locations.get(1))
                        , Double.valueOf(locations.get(0))));
            });
        } else {
            holder.getView(R.id.ll_loaction).setVisibility(View.GONE);
        }
        /**
         * 关注
         */

        ImageView follow = holder.getView(R.id.follow);
        if(!BaseCircleMessageFragment.TYPE_FRIEND.equals(messageType)){
            if (item.isIsAttention() || item.getUserinfo().getUid() == CpigeonData.getInstance().getUserId(mContext)
                    || messageType.equals(BaseCircleMessageFragment.TYPE_FOLLOW)) {
                follow.setVisibility(View.INVISIBLE);
                follow.setOnClickListener(null);
            } else {
                follow.setVisibility(View.VISIBLE);
                follow.setOnClickListener(v -> {
                    getBaseActivity().showLoading();
                    mPre.followId = item.getUserinfo().getUid();
                    mPre.setIsFollow(true);
                    mPre.setFollow(s -> {
                        getBaseActivity().hideLoading();
                        item.setIsAttention(true);
                        notifyItemChanged(holder.getAdapterPosition());
                        ToastUtil.showLongToast(mContext, s);
                        //EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_FOLLOW));
                        EventBus.getDefault().post(new FriendFollowEvent(true));
                    });
                });
            }
        }else {
            follow.setVisibility(View.INVISIBLE);
        }




        /**
         * 图片
         */
        RecyclerView imgs = holder.getView(R.id.imgsList);
        imgs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_UP) {
                    CircleMessageDetailsNewActivity.startActivity(activity, item.getMid(),messageType, holder.getAdapterPosition());
                }
                return false;
            }
        });
        imgs.setLayoutManager(new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        CircleMessageImagesAdapter imagesAdapter = null;
        if (!item.getPicture().isEmpty()) {
            imgs.setVisibility(View.VISIBLE);
            if (imgs.getAdapter() == null) {
                imagesAdapter = new CircleMessageImagesAdapter();
            } else imagesAdapter = (CircleMessageImagesAdapter) imgs.getAdapter();
            imagesAdapter.setNewData(item.getPicture());
            CircleMessageImagesAdapter finalImagesAdapter = imagesAdapter;
            imagesAdapter.setOnItemClickListener((adapter1, view, position) -> {
                ChooseImageManager.showImagePhoto(activity, finalImagesAdapter.getImagesUrl(), position);
            });
            imgs.setAdapter(imagesAdapter);
            imgs.setFocusableInTouchMode(false);
            dataType = TYPE_IMAGE;
        } else {
            imgs.setVisibility(View.GONE);
        }

        /**
         * 视频
         */
        RelativeLayout videoPlayer = holder.getView(R.id.rr_video);
        if (!item.getVideo().isEmpty()) {
            holder.setViewVisible(R.id.rr_video, View.VISIBLE);
            holder.setGlideImageView(mContext, R.id.video_thumb, item.getVideo().get(0).getThumburl());
            holder.getView(R.id.rr_video).setOnClickListener(view -> {
                VideoPlayActivity.startActivity((Activity) mContext, holder.getView(R.id.rr_video),item.getVideo().get(0).getUrl());
            });
            dataType = TYPE_VIDEO;
        } else {
            videoPlayer.setVisibility(View.GONE);
        }

        /**
         * 评论
         */
        RecyclerView comments = holder.getView(R.id.comments);
        comments.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        if (!item.getCommentList().isEmpty()) {
            comments.setVisibility(View.VISIBLE);
            MessageReplayAdapter replayAdapter;
            if (comments.getAdapter() != null) {
                replayAdapter = (MessageReplayAdapter) comments.getAdapter();
            } else replayAdapter = new MessageReplayAdapter(messageType);
            comments.setAdapter(replayAdapter);
            replayAdapter.setOnMessageContentClickListener((commentListBean, position) -> {

                CircleMessageDetailsNewActivity.startActivity(activity, item.getMid(),messageType, holder.getAdapterPosition());
            });

            if(item.getCommentList().size() > 3){
                replayAdapter.setNewData(item.getCommentList().subList(0,3));
            }else {
                replayAdapter.setNewData(item.getCommentList());
            }

            comments.setFocusableInTouchMode(false);
        } else {
            comments.setVisibility(View.GONE);
        }

        /**
         * 点赞评论
         */
        SocialSnsViewHolder socialSnsviewHolder = new SocialSnsViewHolder(activity, holder.getView(R.id.social_sns), "回复:" + item.getUserinfo().getNickname());
        socialSnsviewHolder.bindData(item);
        CircleMessageImagesAdapter finalImagesAdapter1 = imagesAdapter;
        socialSnsviewHolder.setOnSocialListener(new SocialSnsViewHolder.OnSocialListener() {
            @Override
            public void thumb(View view) {
                mPre.messageId = item.getMid();
                mPre.setIsThumb(!item.isThumb());
                getBaseActivity().showLoading();
                mPre.setThumb(s -> {
                    getBaseActivity().hideLoading();
                    if (item.isThumb()) {
                        int position = mPre.getUserThumbPosition(item.getPraiseList(), CpigeonData.getInstance().getUserId(mContext));
                        if (position != -1) {
                            item.getPraiseList().remove(position);
                        }
                        socialSnsviewHolder.setThumb(false);
                    } else {
                        if (CpigeonData.getInstance().getUserInfo() == null) {
                            CpigeonData.DataHelper.getInstance().updateUserInfo(new CpigeonData.DataHelper.OnDataHelperUpdateLisenter<UserInfo.DataBean>() {
                                @Override
                                public void onUpdated(UserInfo.DataBean data) {
                                    CircleMessageEntity.PraiseListBean bean = new CircleMessageEntity.PraiseListBean();
                                    bean.setIsPraise(1);
                                    bean.setUid(CpigeonData.getInstance().getUserId(mContext));
                                    bean.setNickname(CpigeonData.getInstance().getUserInfo().getNickname());
                                     item.getPraiseList().add(0, bean);
                                    socialSnsviewHolder.setThumb(true);
                                    socialSnsviewHolder.setThumbAnimation(true);
                                }

                                @Override
                                public void onError(int errortype, String msg) {
                                    getBaseActivity().error("点赞失败，请稍后重试");
                                }
                            });
                        } else {
                            CircleMessageEntity.PraiseListBean bean = new CircleMessageEntity.PraiseListBean();
                            bean.setIsPraise(1);
                            bean.setUid(CpigeonData.getInstance().getUserId(mContext));
                            bean.setNickname(CpigeonData.getInstance().getUserInfo().getNickname());
                            item.getPraiseList().add(0, bean);
                            socialSnsviewHolder.setThumb(true);
                            socialSnsviewHolder.setThumbAnimation(true);
                        }

                    }
                    notifyItemChanged(holder.getAdapterPosition());
                    postEvent(item);
                });
            }

            @Override
            public void comment(EditText view, InputCommentDialog dialog) {
                activity.showTips("", IView.TipType.LoadingShow);
                mPre.messageId = item.getMid();
                mPre.commentContent = view.getText().toString();
                mPre.addComment(newComment -> {
                    activity.showTips("", IView.TipType.LoadingHide);
                    item.getCommentList().add(0, newComment);
                    dialog.closeDialog();
                    notifyItemChanged(holder.getAdapterPosition());
                    postEvent(item);
                });
            }

            @Override
            public void share(View view) {
                share.setDescription(item.getMsg());
                share.setShareType(ShareDialogFragment.TYPE_URL);
                share.setDescription("鸽迷圈分享：" + item.getMsg());
                share.setShareUrl(mContext.getString(R.string.string_share_circle) + item.getMid());
                share.show(activity.getFragmentManager(), "share");
            }
        });

        if (item.getPraiseList() != null && item.getPraiseList().size() > 0) {

            for (CircleMessageEntity.PraiseListBean praiseListBean : item.getPraiseList()) {

                if (praiseListBean.getUid() == CpigeonData.getInstance().getUserId(mContext) && praiseListBean.getIsPraise() == 1) {
                    socialSnsviewHolder.setThumb(true);
                    item.setThumb();
                    break;
                } else {
                    socialSnsviewHolder.setThumb(false);
                    item.setCancelThumb();
                }
            }
        } else {
            socialSnsviewHolder.setThumb(false);
            item.setCancelThumb();
        }
    }


    public void setmPre(CircleMessagePre mPre) {
        this.mPre = mPre;
    }

    public void setShare(ShareDialogFragment share) {
        this.share = share;
    }

    public ShareDialogFragment getShare() {
        return share;
    }

    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }

    @Override
    protected String getEmptyViewText() {
        return "暂无发布";
    }

    @Override
    protected int getEmptyViewImage() {
        return R.drawable.ic_circle_messag_emptye;
    }

    private void postEvent(CircleMessageEntity entity){
        CircleUpdateManager.get().updateAllCircleList(messageType, entity);
    }
}
