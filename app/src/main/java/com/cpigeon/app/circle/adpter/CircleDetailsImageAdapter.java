package com.cpigeon.app.circle.adpter;

import android.widget.LinearLayout;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleDetailsImageAdapter extends BaseQuickAdapter<CircleMessageEntity.PictureBean, BaseViewHolder> {

    int sizeW;
    int sizeH;

    public CircleDetailsImageAdapter() {
        super(R.layout.item_one_image_layout, Lists.newArrayList());
        sizeW = ScreenTool.getScreenWidth(MyApp.getInstance().getBaseContext()) - ScreenTool.dip2px(40);
        sizeH = (sizeW / 16) * 9;
    }

    @Override
    protected void convert(BaseViewHolder helper, CircleMessageEntity.PictureBean item) {
        helper.setGlideImageView(mContext, R.id.icon,item.getUrl());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeW, sizeH);
        helper.itemView.setLayoutParams(params);
    }

    public List<String> getImageUrls(){
        List<String> urls = Lists.newArrayList();
        for(CircleMessageEntity.PictureBean pictureBean : mData){
            urls.add(pictureBean.getUrl());
        }
        return urls;
    }

}
