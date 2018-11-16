package com.cpigeon.app.circle.adpter;

import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/15.
 */

public class CircleMessageImagesAdapter extends BaseQuickAdapter<CircleMessageEntity.PictureBean, BaseViewHolder> {

    int size;
    int bigSizeW;
    int bigSizeH;
    int margin;

    public CircleMessageImagesAdapter() {
        super(R.layout.item_one_image_layout, Lists.newArrayList());
        int screenW =  ScreenTool.getScreenWidth(MyApp.getInstance().getBaseContext());
        size = ((screenW - ScreenTool.dip2px(40)) / 3);
        bigSizeW = (int) (((screenW - ScreenTool.dip2px(20)) / 3f) * 2);
        bigSizeH = (int) (((screenW - ScreenTool.dip2px(20)) / 9f) * 4);
        margin = ScreenTool.dip2px(5);
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleMessageEntity.PictureBean item) {
        AppCompatImageView view = holder.getView(R.id.icon);
        LinearLayout.LayoutParams itemParams;
        LinearLayout.LayoutParams imgParams;
        if (mData.size() == 1) {
            itemParams = new LinearLayout.LayoutParams(bigSizeW, bigSizeH);
        } else {
            itemParams = new LinearLayout.LayoutParams(size, size);
        }
        holder.itemView.setLayoutParams(itemParams);


        imgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if((holder.getAdapterPosition() % 3) == 0){
            imgParams.setMargins(0, margin, margin,margin);
        }else if((holder.getAdapterPosition() % 3) == 1){
            imgParams.setMargins(margin, margin, margin,margin);
        }else {
            imgParams.setMargins(margin, margin, 0,margin);
        }

        view.setLayoutParams(imgParams);

        holder.setGlideImageView(mContext, R.id.icon, item.getUrl());
    }

    public List<String> getImagesUrl() {
        List<String> list = Lists.newArrayList();
        for (CircleMessageEntity.PictureBean bean : mData) {
            list.add(bean.getUrl());
        }
        return list;
    }

}
