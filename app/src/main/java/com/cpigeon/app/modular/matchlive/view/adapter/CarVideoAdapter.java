package com.cpigeon.app.modular.matchlive.view.adapter;


import android.net.Uri;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.RaceImageOrVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


/**
 * 鸽运通视频
 * Created by Administrator on 2017/6/16.
 */

public class CarVideoAdapter extends BaseQuickAdapter<RaceImageOrVideo,BaseViewHolder> {
    public CarVideoAdapter(List<RaceImageOrVideo> data) {
        super(R.layout.item_car_video,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RaceImageOrVideo item) {
        JZVideoPlayerStandard videoPlayer =  helper.getView(R.id.videoplayer);
        /*if(helper.getAdapterPosition() == 1){
            videoPlayer.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4", JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, item.getTag());
        }else {
        }*/
        videoPlayer.setUp(item.getUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, item.getTag());
        Picasso.with(mContext).load(item.getThumburl())
                .into(((JZVideoPlayerStandard)helper.getView(R.id.videoplayer)).thumbImageView);
        JZVideoPlayer.clearSavedProgress(mContext,item.getUrl());

       /* EasyVideoPlayer player = helper.getView(R.id.player);
        if(helper.getAdapterPosition() == 1){
            player.setSource(Uri.parse("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"));
        }else {
            player.setSource(Uri.parse(item.getUrl()));
        }

        player.setCallback(new EasyVideoCallback() {
            @Override
            public void onStarted(EasyVideoPlayer easyVideoPlayer) {
                *//*for (int i = 0; i < mData.size(); i++) {
                    if(i != helper.getAdapterPosition()){
                        EasyVideoPlayer player1 = (EasyVideoPlayer) getViewByPosition(i,R.id.player);
                        player1.stop();
                    }
                }*//*

            }

            @Override
            public void onPaused(EasyVideoPlayer easyVideoPlayer) {

                player.stop();
            }

            @Override
            public void onPreparing(EasyVideoPlayer easyVideoPlayer) {

            }

            @Override
            public void onPrepared(EasyVideoPlayer easyVideoPlayer) {

            }

            @Override
            public void onBuffering(int i) {

            }

            @Override
            public void onError(EasyVideoPlayer easyVideoPlayer, Exception e) {

            }

            @Override
            public void onCompletion(EasyVideoPlayer easyVideoPlayer) {

            }

            @Override
            public void onRetry(EasyVideoPlayer easyVideoPlayer, Uri uri) {

            }

            @Override
            public void onSubmit(EasyVideoPlayer easyVideoPlayer, Uri uri) {

            }
        });*/


    }
}
