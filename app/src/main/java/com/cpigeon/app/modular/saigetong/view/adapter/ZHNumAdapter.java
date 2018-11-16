package com.cpigeon.app.modular.saigetong.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.SGTDetailsInfoEntity;
import com.cpigeon.app.modular.matchlive.model.bean.RPImages;
import com.cpigeon.app.modular.saigetong.model.bead.SGTImgEntity;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.DateUtils;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ZHNumAdapter extends BaseQuickAdapter<RPImages, BaseViewHolder> {

    public ZHNumAdapter(Context context) {
        super(R.layout.item_zh_num_2, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, RPImages item) {

        ImageView rePhotoTag = holder.getView(R.id.change_tag);

        if(item.getBupai() == 1){
            rePhotoTag.setVisibility(View.VISIBLE);
        }else {
            rePhotoTag.setVisibility(View.INVISIBLE);
        }

        if(holder.getAdapterPosition() != getHeaderLayoutCount()){
            holder.setViewVisible(R.id.time_line_top, View.VISIBLE);
        }else {
            holder.setViewVisible(R.id.time_line_top, View.INVISIBLE);
        }

        if(holder.getAdapterPosition() - getHeaderLayoutCount() != mData.size()  - 1){
            holder.setViewVisible(R.id.time_line_btm, View.VISIBLE);
        }else {
            holder.setViewVisible(R.id.time_line_btm, View.INVISIBLE);
        }

        TextView tvDay = holder.getView(R.id.tv_day);

        /*holder.setText(R.id.tv_day, DateTool.format(item.getSj(), DateTool.FORMAT_DD));
        holder.setText(R.id.tv_year, DateTool.format(item.getSj(), DateTool.FORMAT_YYYY_MM2));*/
        tvDay.setText(item.getDay());
        holder.setText(R.id.tv_year, item.getYearmonth());
        holder.setGlideImageViewNoRound(mContext, R.id.image, item.getImgurl());
        holder.setText(R.id.image_type,item.getTag());

        if(StringValid.isStringValid(item.getUpdatefootinfo())){
            holder.setText(R.id.remark,item.getUpdatefootinfo());
            holder.setViewVisible(R.id.remark, View.VISIBLE);
        }else {
            holder.setViewVisible(R.id.remark, View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            ChooseImageManager.showImagePhoto((Activity) mContext, getImages(), holder.getAdapterPosition() - getHeaderLayoutCount());
        });
    }

    public List<String> getImages(){
        List<String> images = Lists.newArrayList();
        for(RPImages rpImages : mData){
            images.add(rpImages.getImgurl());
        }
        return images;
    }

    @Override
    protected String getEmptyViewText() {
        return "公棚未上传";
    }
}
