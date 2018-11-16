package com.cpigeon.app.circle.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.cpigeon.app.R;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.adpter.CircleDetailsImageAdapter;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageDetailsEvent;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.view.BottomSheetAdapter;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.viewholder.CircleMessageCommentViewHolder;
import com.cpigeon.app.viewholder.CircleMessageHeadViewHolder;
import com.cpigeon.app.viewholder.NewsCommentViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/22.
 */

public class CircleMessageDetailsNewActivity extends BaseActivity<CircleMessagePre> {

    public static final int CIRCLE_MESSAGE_DETAILS_CODE = 0x123;
    public static final String CIRCLE_MESSAGE_POSITION = "CIRCLE_MESSAGE_POSITION";

    List<String> titles;
    ViewPager view_pager;
    MagicIndicator magicIndicator;

    CircleMessageHeadViewHolder headViewHolder;
    CircleMessageCommentViewHolder commentViewHolder;

    DialogHideCircleFragment dialogHideCircleFragment;
    DialogUserDeleteFragment dialogUserDeleteFragment;

    public static void startActivity(Activity activity, int messageId, String messageType, int position){
        IntentBuilder.Builder(activity, CircleMessageDetailsNewActivity.class)
                .putExtra(IntentBuilder.KEY_DATA, messageId)
                .putExtra(IntentBuilder.KEY_TYPE, messageType)
                .putExtra(CIRCLE_MESSAGE_POSITION, position)

                .startActivity(activity, CIRCLE_MESSAGE_DETAILS_CODE);
    }

    public static void startActivity(Context context, int messageId, String messageType, int position){
        IntentBuilder.Builder(context, CircleMessageDetailsNewActivity.class)
                .putExtra(IntentBuilder.KEY_DATA, messageId)
                .putExtra(IntentBuilder.KEY_TYPE, messageType)
                .putExtra(CIRCLE_MESSAGE_POSITION, position)
                .startActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_message_details_new_layout;
    }

    @Override
    public CircleMessagePre initPresenter() {
        return new CircleMessagePre(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        toolbar.getMenu().clear();

        toolbar.getMenu().add("").setIcon(R.mipmap.ic_circle_user_info_share)
                .setOnMenuItemClickListener(item -> {
                    ShareDialogFragment share = new ShareDialogFragment();
                    share.setDescription("");
                    share.setShareType(ShareDialogFragment.TYPE_URL);
                    share.setShareUrl(mContext.getString(R.string.string_share_circle) + mPresenter.circleList.get(0).getMid());
                    share.show(getFragmentManager(), "share");
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        toolbar.getMenu().add("").setIcon(R.mipmap.ic_circle_user_info_menu)
                .setOnMenuItemClickListener(item -> {
                    /**
                     * 屏蔽取消
                     */
                    if (mPresenter.circleList.get(0).getUid() != CpigeonData.getInstance().getUserId(getActivity())) {
                        dialogHideCircleFragment = new DialogHideCircleFragment();
                        dialogHideCircleFragment.setCircleMessageEntity(mPresenter.circleList.get(0));
                        dialogHideCircleFragment.setListener(new DialogHideCircleFragment.OnDialogClickListener() {
                            @Override
                            public void hideMessage() {
                                dialogHideCircleFragment.dismiss();
                                CircleUpdateManager.get().hideMessage(mPresenter.circleList.get(0));
                                ToastUtil.showShortToast(getActivity(), "成功屏蔽该条信息");
                            }

                            @Override
                            public void hideHisMessage() {
                                dialogHideCircleFragment.dismiss();
                                CircleUpdateManager.get().hideMessage(mPresenter.circleList.get(0).getUid());
                                ToastUtil.showShortToast(getActivity(), "成功屏蔽他的信息");
                            }

                            @Override
                            public void black() {
                                dialogHideCircleFragment.dismiss();
                                CircleUpdateManager.get().hideMessage(mPresenter.circleList.get(0).getUid());
                                ToastUtil.showShortToast(getActivity(), "成功加入黑名单");
                            }

                            @Override
                            public void report() {
                                ReportCircleMessageFragment.startReportCircleMessageFragment(getActivity(), mPresenter.circleList.get(0).getMid());
                            }

                            @Override
                            public void cancelFollow() {

                            }
                        });
                        dialogHideCircleFragment.show(getActivity().getFragmentManager(), "DialogHideCircleFragment");
                    } else {
                            dialogUserDeleteFragment = new DialogUserDeleteFragment();
                            dialogUserDeleteFragment.setCircleMessageId(mPresenter.circleList.get(0).getMid());
                            dialogUserDeleteFragment.setListener(new DialogUserDeleteFragment.OnDialogClickListener() {
                                @Override
                                public void delete() {
                                    dialogUserDeleteFragment.dismiss();
                                    DialogUtils.createDialogWithLeft(getActivity(), "删除成功", sweetAlertDialog -> {
                                        EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_ALL));
                                        RxUtils.delayed(200, aLong -> {
                                            EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_MY));
                                        });
                                        sweetAlertDialog.dismiss();
                                        finish();
                                    });
                                }

                                @Override
                                public void forAll() {
                                    dialogUserDeleteFragment.dismiss();
                                    DialogUtils.createHintDialog(getActivity(), "修改成功");
                                }

                                @Override
                                public void forFriend() {
                                    dialogUserDeleteFragment.dismiss();
                                    DialogUtils.createHintDialog(getActivity(), "修改成功");
                                }

                                @Override
                                public void forSelf() {
                                    dialogHideCircleFragment.dismiss();
                                    DialogUtils.createHintDialog(getActivity(), "修改成功");
                                }
                            });
                            dialogUserDeleteFragment.show(getActivity().getFragmentManager(), "delete");
                    }
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        titles = Lists.newArrayList("全部评论", "点赞");
        view_pager = findViewById(R.id.view_pager);
        magicIndicator = findViewById(R.id.magic_indicator1);
        initMagicIndicator1();
        View head = findViewById(R.id.circleMessageHead);
        head.setPadding(ScreenTool.dip2px(10), 0, ScreenTool.dip2px(10), ScreenTool.dip2px(16));


        headViewHolder = new CircleMessageHeadViewHolder(getActivity(), head);
        headViewHolder.details_location.setVisibility(View.VISIBLE);

        headViewHolder.setOnCircleHeadClickListener(new CircleMessageHeadViewHolder.OnCircleHeadClickListener() {
            @Override
            public void itemClick() {

            }

            @Override
            public void followClick(int userId) {
                mPresenter.followId = userId;
                mPresenter.setIsFollow(true);
                mPresenter.setFollow(s -> {
                    headViewHolder.follow.setVisibility(View.GONE);
                    ToastUtil.showLongToast(getActivity(), s);
                    EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_FOLLOW));
                    CircleUpdateManager.get().updateFollow(mPresenter.circleList.get(0), true);
                });
            }
        });

        commentViewHolder = new CircleMessageCommentViewHolder(getActivity(), findViewById(R.id.circleMessageComment));
        commentViewHolder.setOnCircleMessageCommentListener(new CircleMessageCommentViewHolder.OnCircleMessageCommentListener() {
            @Override
            public void thumb(View imageView) {
                mPresenter.messageId = mPresenter.circleList.get(0).getMid();
                mPresenter.setIsThumb(!mPresenter.circleList.get(0).isThumb());
                mPresenter.setThumb(s -> {
                    if (mPresenter.circleList.get(0).isThumb()) {
                        int position = mPresenter.getUserThumbPosition(mPresenter.circleList.get(0).getPraiseList(), CpigeonData.getInstance().getUserId(getActivity()));
                        if (position != -1) {
                            mPresenter.circleList.get(0).getPraiseList().remove(position);
                        }
                        commentViewHolder.setThumb(false);
                        commentViewHolder.setThumbAnimation(false);
                        mPresenter.circleList.get(0).setCancelThumb();
                    } else {
                        CircleMessageEntity.PraiseListBean bean = new CircleMessageEntity.PraiseListBean();
                        bean.setIsPraise(1);
                        bean.setUid(CpigeonData.getInstance().getUserId(getActivity()));
                        bean.setNickname(CpigeonData.getInstance().getUserInfo().getNickname());
                        bean.setHeadimgurl(CpigeonData.getInstance().getUserInfo().getHeadimg());
                        mPresenter.circleList.get(0).getPraiseList().add(0, bean);
                        commentViewHolder.setThumb(true);
                        commentViewHolder.setThumbAnimation(true);
                        mPresenter.circleList.get(0).setThumb();
                    }
                    postCircleEvent();
                });
            }

            @Override
            public void comment(EditText editText, InputCommentDialog dialog) {
                mPresenter.messageId = mPresenter.circleList.get(0).getMid();
                mPresenter.commentContent = editText.getText().toString();
                mPresenter.commentId = 0;
                mPresenter.previousId = 0;
                showLoading();
                mPresenter.addComment(newComment -> {
                    hideLoading();
                    mPresenter.circleList.get(0).getCommentList().add(mPresenter.circleList.get(0).getCommentList().size(), newComment);
                    dialog.closeDialog();
                    postCircleEvent();
                });
            }
        });

        bindData();
    }

    private void bindData() {
        showLoading();
        mPresenter.type = "xxxx";
        mPresenter.getMessageList(circleMessageEntities -> {
            hideLoading();
            if(circleMessageEntities.isEmpty()){
                error("该条信息不存在");
                return;
            }

            headViewHolder.setMessageType(mPresenter.type);
            headViewHolder.bindData(circleMessageEntities.get(0));

            commentViewHolder.bindData(circleMessageEntities.get(0));
            commentViewHolder.setHint("回复 " + mPresenter.circleList.get(0).getUserinfo().getNickname());

            RecyclerView recyclerView = headViewHolder.getImgsList();

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, ScreenTool.dip2px(10),0,0);
            recyclerView.setPadding(ScreenTool.dip2px(10), 0, ScreenTool.dip2px(10),0);
            recyclerView.setLayoutParams(layoutParams);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.content_text);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            CircleDetailsImageAdapter imageAdapter = new CircleDetailsImageAdapter();
            imageAdapter.setOnItemClickListener((adapter, view, position) -> {
                ChooseImageManager.showImagePhoto(this, imageAdapter.getImageUrls(), position);
            });
            recyclerView.setAdapter(imageAdapter);
            imageAdapter.setNewData(mPresenter.circleList.get(0).getPicture());

            addItemDecorationLine(recyclerView, R.color.white, 10);

            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager()
                    , FragmentPagerItems.with(this)
                    .add("全部评论", CircleMessageDetailsCommentFragment.class)
                    .add("点赞", CircleMessageDetailsThumbFragment.class)
                    .create());

            view_pager.setAdapter(adapter);
        });
    }

    private void initMagicIndicator1() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titles.get(index));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.color_666666));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.color_circle_tab_yellow));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_pager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.color_circle_tab_yellow));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        ViewPagerHelper.bind(magicIndicator, view_pager);
    }

    public CircleMessagePre getPre() {
        return mPresenter;
    }

    public void postCircleEvent(){
        if(!mPresenter.homeMessageType.equals(BaseCircleMessageFragment.TYPE_FRIEND)){
            CircleUpdateManager.get().updateAllCircleList(mPresenter.type, mPresenter.circleList.get(0));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleUpdateManager.CircleUpdateEvent event) {
        CircleMessageEntity entity = event.getEntity();
        if(mPresenter.circleList.get(0).getMid() == entity.getMid()){

            mPresenter.circleList.set(0, entity);

            EventBus.getDefault().post(new CircleMessageDetailsEvent(CircleMessageDetailsEvent.TYPE_COMMENT));
            EventBus.getDefault().post(new CircleMessageDetailsEvent(CircleMessageDetailsEvent.TYPE_THUMB));

            headViewHolder.bindData(mPresenter.circleList.get(0));

            commentViewHolder.bindData(mPresenter.circleList.get(0));
        }
    }
}
