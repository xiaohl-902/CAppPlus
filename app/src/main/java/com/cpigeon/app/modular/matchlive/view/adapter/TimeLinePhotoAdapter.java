package com.cpigeon.app.modular.matchlive.view.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.RaceImageOrVideo;
import com.cpigeon.app.utils.DateTool;
import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class TimeLinePhotoAdapter extends BaseQuickAdapter<RaceImageOrVideo, BaseViewHolder> {
    public TimeLinePhotoAdapter(List<RaceImageOrVideo> data) {
        super(R.layout.item_zh_num,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RaceImageOrVideo item) {

        if(helper.getAdapterPosition() != 0){
            helper.setViewVisible(R.id.time_line_top, View.VISIBLE);
        }else {
            helper.setViewVisible(R.id.time_line_top, View.INVISIBLE);
        }

        if(helper.getAdapterPosition() != mData.size() - 1){
            helper.setViewVisible(R.id.time_line_btm, View.VISIBLE);
        }else {
            helper.setViewVisible(R.id.time_line_btm, View.INVISIBLE);
        }
        helper.setText(R.id.tag, item.getTag());
//        helper.setText(R.id.text_timeline_date,item.getTime());
        /*helper.setText(R.id.text_timeline_title,item.getTag());
        ((TimelineView)helper.getView(R.id.time_marker)).setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));*/
        Picasso.with(mContext)
                .load(TextUtils.isEmpty(item.getThumburl())?item.getUrl():item.getThumburl())
                .placeholder(R.mipmap.head_image_default)
                .error(R.mipmap.head_image_default)
                .into((ImageView) helper.getView(R.id.image));

        Date date = DateTool.strToDateTime(item.getTime());
        DateTool.format(date.getTime(), DateTool.FORMAT_MM);

        helper.setText(R.id.tv_day, DateTool.format(date.getTime(), DateTool.FORMAT_DD));
        helper.setText(R.id.tv_year, DateTool.format(date.getTime(), DateTool.FORMAT_YYYY_MM2));
        helper.setText(R.id.tv_time, DateTool.format(date.getTime(), DateTool.FORMAT_HH_MM_SS));


    }
}
