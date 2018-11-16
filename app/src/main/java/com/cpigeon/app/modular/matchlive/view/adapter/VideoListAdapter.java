package com.cpigeon.app.modular.matchlive.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cpigeon.app.R;
import com.cpigeon.app.viewholder.MapVideoViewHolder;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.entity.VideoEntity;
import com.cpigeon.app.modular.matchlive.model.bean.RaceImageOrVideo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GUO on 2015/12/3.
 */

/**
 * Created by Nelson on 15/11/9.
 */
public class VideoListAdapter extends RecyclerView.Adapter {

    private final static String TAG = "RecyclerBaseAdapter";

    private List<RaceImageOrVideo> itemDataList = null;

    private Context context = null;

    private SmallVideoHelper smallVideoHelper;

    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    private MapVideoViewHolder.OnShareListener onShareListener;

    public VideoListAdapter(Context context, List<VideoEntity> itemDataList) {
        this.itemDataList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_video_play_layout, parent, false);
        final RecyclerView.ViewHolder holder = new MapVideoViewHolder(context, v);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MapVideoViewHolder recyclerItemViewHolder = (MapVideoViewHolder) holder;
        recyclerItemViewHolder.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
        recyclerItemViewHolder.setRecyclerBaseAdapter(this);
        recyclerItemViewHolder.setShareListener(onShareListener);
        recyclerItemViewHolder.onBind(position, itemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void addData(List<RaceImageOrVideo> data) {
        itemDataList = data;
        notifyDataSetChanged();
    }

    public SmallVideoHelper getVideoHelper() {
        return smallVideoHelper;
    }

    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }


    public List<RaceImageOrVideo> getData(){
        return itemDataList;
    }

    public void setOnShareListener(MapVideoViewHolder.OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }
}
