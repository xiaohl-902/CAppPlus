package com.cpigeon.app.circle.ui;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.circle.presenter.CirclePre;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.event.FansAddEvent;
import com.cpigeon.app.event.FriendFollowEvent;
import com.cpigeon.app.modular.usercenter.view.activity.UserInfoActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;
import com.cpigeon.app.utils.http.GlideRoundTransform;
import com.cpigeon.app.view.MyViewPager;
import com.melnykov.fab.FloatingActionButton;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zhu TingYu on 2018/1/4.
 */

public class CircleHomeFragment extends BaseMVPFragment<CirclePre>{

    CircleImageView imgHeadIcon;
    TextView tvFans;
    TextView tvFocus;

    SmartTabLayout tabLayout;
    MyViewPager viewPager;

    ImageView floatingActionButton;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvFocus.setText(String.valueOf(mPresenter.circleUserInfoEntity.gzCount));
            return false;
        }
    });

    private CpigeonData.OnDataChangedListener onDataChangedListenerWeakReference = new CpigeonData.OnDataChangedListener() {
        @Override
        public void OnDataChanged(CpigeonData cpigeonData) {
            if(cpigeonData.getUserInfo() != null){
                Glide.with(getContext())
                        .load(cpigeonData.getUserInfo().getHeadimg())
                        .into(imgHeadIcon);
            }
        }
    };

    @Override
    protected CirclePre initPresenter() {
        return new CirclePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pigeon_circle_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        CpigeonData.getInstance().addOnDataChangedListener(onDataChangedListenerWeakReference);

        mPresenter.userId = CpigeonData.getInstance().getUserId(getActivity());
        mPresenter.type = CirclePre.TYPE_USER;

        toolbar.setNavigationIcon(R.mipmap.ic_cricle_my);
        toolbar.setNavigationOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), CircleFriendFragment.class);
        });

        toolbar.getMenu().clear();
        toolbar.getMenu().add("附近").setOnMenuItemClickListener(item -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), CircleNearbyFragment.class);
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        imgHeadIcon = findViewById(R.id.head_img);
        tvFans = findViewById(R.id.tv_fans);
        tvFocus = findViewById(R.id.tv_focus);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), PushCircleMessageFragment.class);
        });

        findViewById(R.id.llFollow).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), CircleFriendFragment.class);
        });

        findViewById(R.id.llFans).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, 1)
                    .startParentActivity(getActivity(), CircleFriendFragment.class);
        });

        tabLayout = findViewById(R.id.tab_view);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setScanScroll(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                GSYVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        initHeadView();

        Bundle all = new Bundle();
        Bundle follow = new Bundle();
        Bundle my = new Bundle();

        all.putString(IntentBuilder.KEY_TYPE, BaseCircleMessageFragment.TYPE_ALL);
        all.putBoolean(IntentBuilder.KEY_BOOLEAN, false);
        all.putString(CircleMessagePre.HOME_MESSAGE_TYPE, BaseCircleMessageFragment.TYPE_ALL);

        follow.putString(IntentBuilder.KEY_TYPE, BaseCircleMessageFragment.TYPE_FOLLOW);
        follow.putBoolean(IntentBuilder.KEY_BOOLEAN, false);
        follow.putString(CircleMessagePre.HOME_MESSAGE_TYPE, BaseCircleMessageFragment.TYPE_FOLLOW);

        my.putString(IntentBuilder.KEY_TYPE, BaseCircleMessageFragment.TYPE_MY);
        my.putString(CircleMessagePre.HOME_MESSAGE_TYPE, BaseCircleMessageFragment.TYPE_MY);
        my.putBoolean(IntentBuilder.KEY_BOOLEAN, false);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getContext())
                .add("全部动态", BaseCircleMessageFragment.class, all)
                .add("我的关注", BaseCircleMessageFragment.class, follow)
                .add("我的发布", BaseCircleMessageFragment.class, my)
                .create());

        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabClickListener(position -> {
            viewPager.setCurrentItem(position, false);
        });

    }

    private void initHeadView() {
        mPresenter.getUserInfo(data -> {
            if(StringValid.isStringValid(data.headimgurl)){
                Glide.with(getContext())
                        .load(data.headimgurl)
                        .into(imgHeadIcon);
            }
            setTitle(mPresenter.circleUserInfoEntity.nickname);
            tvFans.setText(String.valueOf(data.fsCount));
            tvFocus.setText(String.valueOf(data.gzCount));
            imgHeadIcon.setOnClickListener(v -> {
                IntentBuilder.Builder(getActivity(), UserInfoActivity.class).startActivity();
            });
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleMessageEvent event) {
        if(event.type.equals(BaseCircleMessageFragment.TYPE_FOLLOW)){
            mPresenter.circleUserInfoEntity.gzCount ++;
            tvFocus.setText(mPresenter.circleUserInfoEntity.gzCount);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FriendFollowEvent event) {
        if(event.isFollow()){
            mPresenter.circleUserInfoEntity.gzCount ++;
        }else {
            mPresenter.circleUserInfoEntity.gzCount --;
        }
        //handler.sendEmptyMessage(0);
        tvFocus.setText(String.valueOf(mPresenter.circleUserInfoEntity.gzCount));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FansAddEvent event) {
       initHeadView();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CpigeonData.getInstance().removeOnDataChangedListener(onDataChangedListenerWeakReference);
    }
}
