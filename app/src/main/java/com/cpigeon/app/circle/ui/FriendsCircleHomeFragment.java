package com.cpigeon.app.circle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.circle.presenter.CirclePre;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.event.FriendFollowEvent;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zhu TingYu on 2018/5/24.
 */

public class FriendsCircleHomeFragment extends BaseMVPFragment<CirclePre> {

    CircleImageView imgHeadIcon;
    TextView tvFans;
    TextView tvFocus;

    SmartTabLayout tabLayout;
    ViewPager viewPager;

    RelativeLayout rlFollow;
    RelativeLayout rlUserHeadMessage;

    public static void start(Activity activity, int userId, String messageType){
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, userId)
                .putExtra(IntentBuilder.KEY_TYPE, messageType)
                .startParentActivity(activity, FriendsCircleHomeFragment.class, false);
    }

    @Override
    protected CirclePre initPresenter() {
        return new CirclePre(getActivity());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_friends_home_layout;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mPresenter.userId = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_DATA, 0);
        mPresenter.type = CirclePre.TYPE_HIS;

        toolbar.getMenu().clear();
        toolbar.getMenu().add("").setIcon(R.mipmap.ic_circle_user_info_menu)
                .setOnMenuItemClickListener(item -> {
                    DialogHideCircleFragment dialogHideCircleFragment = new DialogHideCircleFragment();
                    dialogHideCircleFragment.setUserinfoBean(mPresenter.circleUserInfoEntity);
                    dialogHideCircleFragment.setListener(new DialogHideCircleFragment.OnDialogClickListener() {
                        @Override
                        public void hideMessage() {
                            dialogHideCircleFragment.dismiss();
                            ToastUtil.showShortToast(getActivity(), "成功屏蔽信息");
                        }

                        @Override
                        public void hideHisMessage() {
                            dialogHideCircleFragment.dismiss();
                            CircleUpdateManager.get().hideMessage(mPresenter.userId);
                            ToastUtil.showShortToast(getActivity(), "成功屏蔽他的信息");

                        }

                        @Override
                        public void black() {
                            dialogHideCircleFragment.dismiss();
                            CircleUpdateManager.get().hideMessage(mPresenter.userId);
                            ToastUtil.showShortToast(getActivity(), "成功加入黑名单");
                        }

                        @Override
                        public void report() {
                        }

                        @Override
                        public void cancelFollow() {
                            mPresenter.setIsFollow(false);
                            showLoading();
                            mPresenter.setFollow(s -> {
                                hideLoading();
                                CircleMessageEntity entity = new CircleMessageEntity();
                                entity.setUid(mPresenter.userId);
                                CircleUpdateManager.get().updateFollow(entity, false);
                                rlFollow.setVisibility(View.VISIBLE);
                                mPresenter.circleUserInfoEntity.setFollow(false);
                                ToastUtil.showShortToast(getActivity(), "成功取消关注");
                            });
                        }
                    });
                    dialogHideCircleFragment.show(getActivity().getFragmentManager(), "DialogHideCircleFragment");
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        imgHeadIcon = findViewById(R.id.head_img);
        tvFans = findViewById(R.id.tv_fans);
        tvFocus = findViewById(R.id.tv_focus);

        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        rlFollow = findViewById(R.id.rlFollow);
        rlUserHeadMessage = findViewById(R.id.rlUserHeadMessage);
        rlUserHeadMessage.setBackground(getResources().getDrawable(R.drawable.svg_friend_circle_home_bg));

        initHeadView();


    }

    private void initHeadView() {
        showLoading();
        mPresenter.getUserInfo(data -> {
            hideLoading();
            if(StringValid.isStringValid(data.headimgurl)){
                Glide.with(getContext())
                        .load(data.headimgurl)
                        .into(imgHeadIcon);
            }
            setTitle(mPresenter.circleUserInfoEntity.nickname);
            tvFans.setText(String.valueOf(data.fsCount));
            tvFocus.setText(String.valueOf(data.gzCount));
            imgHeadIcon.setOnClickListener(v -> {
                ChooseImageManager.showImagePhoto(getActivity(), Lists.newArrayList(data.headimgurl),0);
            });

            Bundle message = new Bundle();
            Bundle info = new Bundle();

            message.putString(IntentBuilder.KEY_TYPE, BaseCircleMessageFragment.TYPE_FRIEND);
            message.putInt(IntentBuilder.KEY_DATA, mPresenter.userId);
            message.putString(CircleMessagePre.HOME_MESSAGE_TYPE, mPresenter.homeMessageType);

            info.putParcelable(IntentBuilder.KEY_DATA, data);

            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getContext())
                    .add("全部动态", BaseCircleMessageFragment.class, message)
                    .add("个人信息", FriendsInfoFragment.class, info)
                    .create());

            viewPager.setAdapter(adapter);
            tabLayout.setViewPager(viewPager);

            if(data.isFollow()){
                rlFollow.setVisibility(View.GONE);
            }

            rlFollow.setOnClickListener(v -> {
                showLoading();
                mPresenter.setIsFollow(true);
                mPresenter.setFollow(s -> {
                    hideLoading();
                    ToastUtil.showShortToast(getActivity(),s);
                    rlFollow.setVisibility(View.GONE);
                    CircleMessageEntity entity  = new CircleMessageEntity();
                    entity.setUid(mPresenter.userId);
                    CircleUpdateManager.get().updateFollow(entity, true);
                    mPresenter.circleUserInfoEntity.setFollow(true);
                    EventBus.getDefault().post(new FriendFollowEvent(true));
                });
            });
        });
    }
}
