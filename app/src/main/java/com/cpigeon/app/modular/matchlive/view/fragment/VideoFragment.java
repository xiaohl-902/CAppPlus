//package com.cpigeon.app.modular.matchlive.view.fragment;
//
//import android.Manifest;
//import android.content.Intent;
//import android.graphics.Point;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.SharedElementCallback;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//
//import com.cpigeon.cpigeonhelper.R;
//import com.cpigeon.cpigeonhelper.common.db.MonitorData;
//import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
//import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.VideoEditActivity;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay.RecyclerBaseAdapter;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay.RecyclerItemViewHolder;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay.SampleListener;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay.SmallVideoHelper;
//import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorView;
//import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
//import com.cpigeon.cpigeonhelper.utils.CommonUitls;
//import com.shuyu.gsyvideoplayer.utils.CommonUtil;
//import com.shuyu.gsyvideoplayer.utils.Debuger;
//import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
//import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * 比赛视频
// * Created by Administrator on 2017/10/10.
// */
//public class VideoFragment extends BaseFragment implements MonitorView {
//
//    private static VideoFragment fragment;
//    @BindView(R.id.video_recyclerView)
//    RecyclerView mRecyclerView;//视频列表展示
//    @BindView(R.id.video_customEmptyView)
//    CustomEmptyView mCustomEmptyView;
//    @BindView(R.id.btn_add_video)
//    Button btnAddVideo;//添加视频按钮
//
//    @BindView(R.id.video_srl)
//    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件
//
//    private GYTMonitorPresenter presenter;//控制层
//    private RecyclerBaseAdapter mAdapter;//适配器
//
//    @BindView(R.id.videoFullContainer)
//    FrameLayout videoFullContainer;
//
//    private List<ImgOrVideoEntity> urlDatas = new ArrayList<>();
//
//    public static SmallVideoHelper smallVideoHelper;
//    SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;
//
//    int lastVisibleItem;
//    int firstVisibleItem;
//
//    LinearLayoutManager linearLayoutManager;
//
//    public static VideoFragment newInstance() {
//        if (fragment == null) {
//            fragment = new VideoFragment();
//        }
//        return fragment;
//    }
//
//
//    @Override
//    public int getLayoutResId() {
//        return R.layout.fragment_video;
//    }
//
//    @Override
//    public void finishCreateView(Bundle state) {
//
//        initRecyclerView();//初始化RecyclerView
//        presenter = new GYTMonitorPresenter(this);//初始化控制层
//
//        initRefreshLayout();//初始化刷新布局
//
//        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
//
//        // 分享 Android6.0权限适配
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
//        }
//
//        //【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
//        if (MonitorData.getMonitorStateCode() == 2 || MonitorData.getMonitorStateCode() == 3) {//监控结束，授权人查看隐藏按钮
//            btnAddVideo.setVisibility(View.GONE);
//        }
//
//
//        initView();
//
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                Debuger.printfLog("firstVisibleItem " + firstVisibleItem + " lastVisibleItem " + lastVisibleItem);
//                //大于0说明有播放,//对应的播放列表TAG
//                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
//                    //当前播放的位置
//                    int position = smallVideoHelper.getPlayPosition();
//                    //不可视的是时候
//                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
//
//                        //如果是小窗口就不需要处理
//                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
//                            //小窗口
//                            int size = CommonUtil.dip2px(getActivity(), 150);
//                            //actionbar为true才不会掉下面去
//                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
//                        }
//                    } else {
//                        if (smallVideoHelper.isSmall()) {
//                            smallVideoHelper.smallVideoToNormal();
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public void setEnterSharedElementCallback(SharedElementCallback callback) {
//        super.setEnterSharedElementCallback(callback);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        GSYVideoPlayer.releaseAllVideos();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        smallVideoHelper.releaseVideoPlayer();
//        GSYVideoPlayer.releaseAllVideos();
//    }
//
//    private void initView() {
//
//
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//
//        mAdapter = new RecyclerBaseAdapter(getActivity(), null);
//        mRecyclerView.setAdapter(mAdapter);
//
//
//        smallVideoHelper = new SmallVideoHelper(getActivity(), new NormalGSYVideoPlayer(getActivity()));
//        smallVideoHelper.setFullViewContainer(videoFullContainer);
//
//        //配置
//        gsySmallVideoHelperBuilder = new SmallVideoHelper.GSYSmallVideoHelperBuilder();
//        gsySmallVideoHelperBuilder
//                .setHideActionBar(true)
//                .setHideStatusBar(true)
//                .setNeedLockFull(false)
//                .setCacheWithPlay(true)
//                .setShowFullAnimation(true)
//                .setLockLand(true).setVideoAllCallBack(new SampleListener() {
//
//
//            @Override
//            public void onClickSeekbarFullscreen(String url, Object... objects) {
//                super.onClickSeekbarFullscreen(url, objects);
//            }
//
//
//            //进去全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
//            @Override
//            public void onEnterFullscreen(String url, Object... objects) {
//
//                super.onEnterFullscreen(url, objects);
//            }
//
//
//            //退出全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
//            @Override
//            public void onQuitFullscreen(String url, Object... objects) {
//
////                Log.d("myxiaohl", "onQuitFullscreen: 退出全屏");
//                PigeonMonitorFragment.ff_tag_z.setVisibility(View.VISIBLE);
//                PigeonMonitorFragment.mViewPager.setScanScroll(true);
//
//                super.onQuitFullscreen(url, objects);
//
//
//            }
//
//            @Override
//            public void onPrepared(String url, Object... objects) {
//                super.onPrepared(url, objects);
////                Debuger.printfLog("Duration " + smallVideoHelper.getGsyVideoPlayer().getDuration() + " CurrentPosition " + smallVideoHelper.getGsyVideoPlayer().getCurrentPositionWhenPlaying());
//            }
//
//            @Override
//            public void onQuitSmallWidget(String url, Object... objects) {
//                super.onQuitSmallWidget(url, objects);
//                //大于0说明有播放,//对应的播放列表TAG
//                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
//                    //当前播放的位置
//                    int position = smallVideoHelper.getPlayPosition();
//                    //不可视的是时候
//                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
//                        //释放掉视频
//                        smallVideoHelper.releaseVideoPlayer();
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//
//            }
//        });
//
//        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);
//
//        mAdapter.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
//
//    }
//
//
//    /**
//     * 初始化刷新布局
//     */
//    @Override
//    public void initRefreshLayout() {
//
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
//        mSwipeRefreshLayout.post(() -> {
//            mSwipeRefreshLayout.setRefreshing(true);
//            presenter.getImgOrVideo("video");//获取视频数据
//        });
//
//        mSwipeRefreshLayout.setOnRefreshListener(() -> {
//            presenter.getImgOrVideo("video");//获取视频数据
//        });
//    }
//
//    /**
//     * 其中123是requestcode，可以根据这个code判断，用户是否同意了授权。如果没有同意，可以根据回调进行相应处理：
//     * 分享权限设置
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//
//    }
//
//    @OnClick({R.id.btn_add_video})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_add_video://添加视频按钮
//
//                if (MonitorData.getMonitorStateCode() == 2) {
//                    CommonUitls.showToast(getActivity(), "已完成监控，不能上传视频");
//                } else {
//                    //跳转到
//                    startActivity(new Intent(getActivity(), VideoEditActivity.class));
//                }
//                break;
//        }
//    }
//
//    @Subscribe //订阅事件FirstEvent
//    public void onEventMainThread(String playListRefresh) {
////        Log.d("event", "视频刷新订阅返回");
//        if (playListRefresh.equals("vodeoRefresh")) {
//            mAdapter.getData().clear();
//            mAdapter.notifyDataSetChanged();
//            presenter.getImgOrVideo("video");
//        }
//
//        if (playListRefresh.equals("endPlay")) {
//            btnAddVideo.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        EventBus.getDefault().unregister(this);//取消注册
//    }
//
//
//    /**
//     * 初始化RecyclerView
//     */
//    private List<ImgOrVideoEntity> datas;
//
//
//    @Override
//    protected void initRecyclerView() {
//        datas = new ArrayList<>();
//        mAdapter = new RecyclerBaseAdapter(getActivity(), null);
////        mAdapter.setLoadMoreView(new CustomLoadMoreView());
////        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//    }
//
//    @Override
//    public void imgOrVideoData(List<ImgOrVideoEntity> data) {
//
//        mSwipeRefreshLayout.setRefreshing(false);//关闭刷新动画
//
//        if (data.size() > 0) {//请求成功有数据
//            this.datas.clear();
//            this.datas = data;
//            mCustomEmptyView.setVisibility(View.GONE);//隐藏错误视图
//            mRecyclerView.setVisibility(View.VISIBLE);//展示数据视图
//            mAdapter.getData().clear();
//            mAdapter.addData(data);
//            mAdapter.notifyDataSetChanged();
//        } else {//请求成功没有数据
//            mRecyclerView.setVisibility(View.GONE);//隐藏数据视图
//            mCustomEmptyView.setVisibility(View.VISIBLE);//展示错误视图
//            mCustomEmptyView.setEmptyImage(R.mipmap.face);
//            mCustomEmptyView.setEmptyText("没有比赛视频");
//        }
//    }
//
//    @Override
//    public void succeed() {
//
//    }
//
//    @Override
//    public void openMonitorResults(ApiResponse<Object> dataApiResponse, String msg) {
//
//    }
//
//    @Override
//    public void fail() {
//
//    }
//
//    @Override
//    public void locationInfoDatas(List<LocationInfoEntity> datas) {
//
//    }
//
//    @Override
//    public void getRaceLocation(List<RaceLocationEntity> raceLocationData) {
//
//    }
//
//    @Override
//    public boolean checkLogin() {
//        return false;
//    }
//
//    @Override
//    public boolean showTips(String tip, TipType tipType) {
//        return false;
//    }
//
//    @Override
//    public boolean showTips(String tip, TipType tipType, int tag) {
//        return false;
//    }
//}
