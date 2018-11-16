package com.cpigeon.app.home.adpter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.BaseDynamicEntity;
import com.cpigeon.app.entity.DynamicEntity;
import com.cpigeon.app.entity.ImageEntity;
import com.cpigeon.app.home.HomePre;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.http.GlideRoundTransform;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/2.
 */

public class CircleDynamicAdapter extends BaseMultiItemQuickAdapter<DynamicEntity, BaseViewHolder> {

    HomePre mPre;

    public CircleDynamicAdapter(HomePre mPre) {
        super(Lists.newArrayList());
        this.mPre = mPre;
        addItemType(BaseDynamicEntity.IMAGE_0, R.layout.item_dynamic_1_img_layout);
        addItemType(BaseDynamicEntity.IMAGE_1, R.layout.item_dynamic_1_img_layout);
        addItemType(BaseDynamicEntity.IMAGE_2, R.layout.item_dynamic_2_img_layout);
        addItemType(BaseDynamicEntity.IMAGE_3, R.layout.item_dynamic_3_img_layout);
    }

    @Override
    protected void convert(BaseViewHolder holder, DynamicEntity item) {

        holder.setText(R.id.user_name,item.nicheng);
        holder.setText(R.id.content_text, item.content);
        holder.setImageResource(R.id.icon, item.guanzhu ? R.mipmap.ic_circle_followed : R.mipmap.ic_circle_follow);
        holder.getView(R.id.icon).setOnClickListener(v -> {
            mPre.firendId = Integer.parseInt(item.uid);
            mPre.followFirend(s -> {
                ToastUtil.showLongToast(mContext, s);
                item.guanzhu = true;
                notifyDataSetChanged();
            });
        });
        holder.setGlideImageView(mContext, R.id.user_icon, item.headurl);
        List<ImageEntity> list = item.imglist;


        switch (holder.getItemViewType()){
            case BaseDynamicEntity.IMAGE_0:
                holder.setViewVisible(R.id.content_img, View.GONE);
                break;
            case BaseDynamicEntity.IMAGE_1:
                holder.setGlideImageView(mContext,R.id.content_img, list.get(0).imgurl);
                break;
            case BaseDynamicEntity.IMAGE_2:
                holder.setGlideImageView(mContext,R.id.content_img1, list.get(0).imgurl);
                holder.setGlideImageView(mContext,R.id.content_img2, list.get(1).imgurl);
                break;
            case BaseDynamicEntity.IMAGE_3:
                holder.setGlideImageView(mContext,R.id.content_img1, list.get(0).imgurl);
                holder.setGlideImageView(mContext,R.id.content_img2, list.get(1).imgurl);
                holder.setGlideImageView(mContext,R.id.content_img3, list.get(2).imgurl);
                break;

        }
    }
}
