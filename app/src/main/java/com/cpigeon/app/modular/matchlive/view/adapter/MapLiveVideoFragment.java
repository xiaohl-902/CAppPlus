package com.cpigeon.app.modular.matchlive.view.adapter;

import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.VideoEntity;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/8.
 */

public class MapLiveVideoFragment extends BaseQuickAdapter<VideoEntity, BaseViewHolder>
{
    public MapLiveVideoFragment(int layoutResId, List<VideoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoEntity item) {

    }
}
