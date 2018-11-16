package com.cpigeon.app.circle.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.base.videoplay.SampleListener;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.adpter.CircleMessageAdapter;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.entity.JPushCircleEntity;
import com.cpigeon.app.event.CircleMessageDetailsEvent;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.event.FriendFollowEvent;
import com.cpigeon.app.event.JPushEvent;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;

import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.view.ImageView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.viewholder.MapVideoViewHolder;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Zhu TingYu on 2018/1/15.
 */

public class BaseCircleMessageFragment extends BaseMVPFragment<CircleMessagePre> {

    public static final String TYPE_ALL = "gclb";
    public static final String TYPE_FOLLOW = "gzlb";
    public static final String TYPE_MY = "wdfb";
    public static final String TYPE_FRIEND = "hyfb";

    RecyclerView recyclerView;
    CircleMessageAdapter adapter;
    ShareDialogFragment shareDialogFragment;
    int lastVisibleItem;
    int firstVisibleItem;
    LinearLayoutManager linearLayoutManager;


    public static SmallVideoHelper smallVideoHelper;
    SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected CircleMessagePre initPresenter() {
        return new CircleMessagePre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);

        shareDialogFragment = new ShareDialogFragment();

        recyclerView = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setBackgroundColor(getResources().getColor(R.color.color_e6e6e6));
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CircleMessageAdapter(getActivity(), mPresenter.type, mPresenter.homeMessageType);
        adapter.setShare(shareDialogFragment);
        adapter.setmPre(mPresenter);
        adapter.setOnNotNetClickListener(v -> {
            bindData();
        });
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.getMessageList(data -> {
                if (data.isEmpty()) {
                    adapter.setLoadMore(true);
                } else {
                    adapter.setLoadMore(false);
                    adapter.addData(data);
                }
            });
        }, recyclerView);

        recyclerView.requestFocus();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });

        smallVideoHelper = new SmallVideoHelper(getActivity(), new NormalGSYVideoPlayer(getActivity()));
        smallVideoHelper.setFullViewContainer(MainActivity.videoFullContainer);

        gsySmallVideoHelperBuilder = new SmallVideoHelper.GSYSmallVideoHelperBuilder();
        gsySmallVideoHelperBuilder
                .setHideActionBar(true)
                .setHideStatusBar(true)
                .setNeedLockFull(false)
                .setCacheWithPlay(true)
                .setShowFullAnimation(true)
                .setLockLand(true).setVideoAllCallBack(new SampleListener() {

            //退出全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
            @Override
            public void onQuitFullscreen(String url, Object... objects) {

//                Log.d("myxiaohl", "onQuitFullscreen: 退出全屏");
//                PigeonMonitorFragment.ff_tag_z.setVisibility(View.VISIBLE);
//                PigeonMonitorFragment.mViewPager.setScanScroll(true);
                if (((BaseActivity) getActivity()).toolbar != null) {
                    ((BaseActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
                }
                if (MapLiveActivity.viewpagertab != null) {
                    MapLiveActivity.viewpagertab.setVisibility(View.VISIBLE);
                }

                if (MapLiveActivity.viewPager != null) {
                    MapLiveActivity.viewPager.setScanScroll(true);
                }

                if (MainActivity.mBottomNavigationBar != null) {
                    MainActivity.mBottomNavigationBar.setVisibility(View.VISIBLE);
                }

                GSYVideoPlayer.releaseAllVideos();

                super.onQuitFullscreen(url, objects);
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(MapVideoViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        smallVideoHelper.releaseVideoPlayer();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });

        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);

        adapter.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);

        bindData();

        setRefreshListener(() -> {
            mPresenter.page = 1;
            bindData();
        });

    }

    private void bindData() {
        showLoading();
        mPresenter.getMessageList(s -> {
            adapter.setNewData(s);
            hideLoading();
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleMessageEvent event) {
        if (event.type.equals(mPresenter.type)) {
            mPresenter.page = 1;
            bindData();
        }

        if(CircleMessageEvent.TYPE_CLEAN_NEW_MESSAGE.equals(event.type)){
            adapter.removeAllHeaderView();
            mPresenter.jPushCircleEntities.clear();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleUpdateManager.CircleUpdateEvent event) {
        if (event.getUpdateType().equals(mPresenter.type)) {
            mPresenter.searchCircleMessage(adapter.getData(), event.getEntity(), true);
            mPresenter.setOnSearchCircleMessageLisenter(new CircleMessagePre.OnSearchCircleMessageListener() {
                @Override
                public void search(int position) {
                    adapter.setData(position, event.getEntity());
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleUpdateManager.CircleFollowUpdateEvent event) {

        if (TYPE_FRIEND.equals(mPresenter.type)) {
            return;
        }

        mPresenter.searchCircleMessage(adapter.getData(), event.getEntity(), false);
        mPresenter.setOnSearchCircleMessageLisenter(position -> {
            if(event.getType().equals(CircleUpdateManager.CircleFollowUpdateEvent.TYPE_FOLLOW)){
                adapter.getItem(position).setIsAttention(true);
            }else {
                adapter.getItem(position).setIsAttention(false);
            }
            adapter.notifyItemChanged(position);
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JPushEvent<JPushCircleEntity> event) {
        if (event.getType().equals(JPushEvent.TYPE_CIRCLE)) {
            JPushCircleEntity data = event.getData();
            mPresenter.addJPushCircleEntity(data);

            View messageHead = LayoutInflater.from(getActivity()).inflate(R.layout.item_circle_list_new_message_layout, recyclerView, false);
            AppCompatImageView headImage = messageHead.findViewById(R.id.headImage);
            TextView messageCount = messageHead.findViewById(R.id.messageCount);

            Glide.with(getActivity()).load(data.getHeadimgurl()).into(headImage);
            messageCount.setText(getString(R.string.str_new_circle_message_count, String.valueOf(mPresenter.jPushCircleEntities.size())));
            if(adapter.getHeaderLayoutCount() > 0){
                adapter.setHeaderView(messageHead, 0);
            }else {
                adapter.addHeaderView(messageHead);
            }
            if(linearLayoutManager.findFirstVisibleItemPosition() < 3){
                recyclerView.smoothScrollToPosition(0);
            }

            adapter.getHeaderLayout().setOnClickListener(v -> {
                IntentBuilder.Builder()
                        .putParcelableArrayListExtra(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) mPresenter.jPushCircleEntities)
                        .startParentActivity(getActivity(), CircleNewMessageFragment.class);
                EventBus.getDefault().post(new CircleMessageEvent(CircleMessageEvent.TYPE_CLEAN_NEW_MESSAGE));
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
