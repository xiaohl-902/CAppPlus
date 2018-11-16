package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.view.adapter.CarVideoAdapter;
import com.cpigeon.app.viewholder.MapVideoViewHolder;
import com.cpigeon.app.base.videoplay.SampleListener;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.entity.VideoEntity;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.RaceImageOrVideo;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.VideoListAdapter;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.customview.CustomEmptyView;
import com.cpigeon.app.view.ShareDialogFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class MapVideoFragment extends BaseFragment {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    @BindView(R.id.videoFullContainer)
    FrameLayout videoFullContainer;


    private boolean mIsRefreshing = false;
    private GeCheJianKongRace geCheJianKongRace;
    private List<LocalMedia> list = new ArrayList<>();
    private VideoListAdapter mAdapter;
    ShareDialogFragment shareDialogFragment;


    private List<VideoEntity> urlDatas = new ArrayList<>();

    public static SmallVideoHelper smallVideoHelper;
    SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    int lastVisibleItem;
    int firstVisibleItem;

    LinearLayoutManager linearLayoutManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (geCheJianKongRace == null)
            this.geCheJianKongRace = ((MapLiveActivity) getActivity()).getGeCheJianKongRace();
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_matchlive_sub;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }

        initRefreshLayout();
        initRecyclerView();
        isPrepared = false;
        initView();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem + " lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(MapVideoViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果是小窗口就不需要处理
                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(getActivity(), 150);
                            //actionbar为true才不会掉下面去
                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (smallVideoHelper.isSmall()) {
                            smallVideoHelper.smallVideoToNormal();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        super.setEnterSharedElementCallback(callback);
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(smallVideoHelper != null){
            smallVideoHelper.releaseVideoPlayer();
        }
        GSYVideoPlayer.releaseAllVideos();
    }

    private void initView() {


        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new VideoListAdapter(getActivity(), null);
        shareDialogFragment = new ShareDialogFragment();
        mAdapter.setOnShareListener(RaceImageOrVideo -> {
            shareDialogFragment.setShareType(ShareDialogFragment.TYPE_VIDEO);
            shareDialogFragment.setShareUrl(getString(R.string.string_share_video) + RaceImageOrVideo.getFid());
            shareDialogFragment.setVideoThumb(RaceImageOrVideo.getThumburl());
            shareDialogFragment.show(getActivity().getFragmentManager(),"share");
        });
        mRecyclerView.setAdapter(mAdapter);


        smallVideoHelper = new SmallVideoHelper(getActivity(), new NormalGSYVideoPlayer(getActivity()));
        smallVideoHelper.setFullViewContainer(videoFullContainer);

        //配置
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
                if(((BaseActivity)getActivity()).toolbar != null){
                    ((BaseActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
                }
                if( MapLiveActivity.viewpagertab != null){
                    MapLiveActivity.viewpagertab.setVisibility(View.VISIBLE);
                }

                if(MapLiveActivity.viewPager != null){
                    MapLiveActivity.viewPager.setScanScroll(true);
                }

                if(MainActivity.mBottomNavigationBar != null){
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
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);

        mAdapter.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);

    }


    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(() -> {

            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            loadData();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            mIsRefreshing = true;

            loadData();
        });
    }

    @Override
    protected void loadData() {
        if (list.size() > 0) {
            list.clear();
        }
        CallAPI.getGYTRaceImgOrVideo(getActivity(), geCheJianKongRace.getId(), "video", new CallAPI.Callback<List<RaceImageOrVideo>>() {
            @Override
            public void onSuccess(List<RaceImageOrVideo> data) {
                Logger.e("图片的size：" + data.size());
                if (data.size() > 0) {
                    for (RaceImageOrVideo raceImage : data) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(raceImage.getUrl());
                        list.add(localMedia);
                    }
                    mAdapter.addData(data);


                    finishTask();
                } else {
                    initEmptyView("数据为空");
                }
            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }

    public void finishTask() {
        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        hideEmptyView();
        mAdapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {

    }

    public void initEmptyView(String tips) {

        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.drawable.ic_empty_img);
        mCustomEmptyView.setEmptyText(tips);
    }


    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
