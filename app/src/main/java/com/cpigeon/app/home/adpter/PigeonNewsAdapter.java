package com.cpigeon.app.home.adpter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.HomeNewsEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.viewholder.NewsViewHolder;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/2.
 */

public class PigeonNewsAdapter extends BaseQuickAdapter<HomeNewsEntity, NewsViewHolder> {

    public static final int TYPE_HOME = 0;

    private int type = 1;

    public PigeonNewsAdapter(int type) {
        super(R.layout.item_news_layout,Lists.newArrayList());
        this.type = type;
    }

    public PigeonNewsAdapter() {
        super(R.layout.item_news_layout,Lists.newArrayList());
    }

    @Override
    protected void convert(NewsViewHolder holder, HomeNewsEntity item) {
        if (type == TYPE_HOME){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0,0,0);
            holder.itemView.setLayoutParams(params);
            /*holder.itemView.setPadding(ScreenTool.dip2px(mContext.getResources().getDimension(R.dimen.large_horizontal_margin_15))
                    ,ScreenTool.dip2px(mContext.getResources().getDimension(R.dimen.medium_vertical_margin))
                    ,ScreenTool.dip2px(mContext.getResources().getDimension(R.dimen.large_horizontal_margin_15))
                    ,ScreenTool.dip2px(mContext.getResources().getDimension(R.dimen.medium_vertical_margin)));*/
        }
        holder.bindData(item);
        holder.setListener(mContext);
    }

    public void setType(int type) {
        this.type = type;
    }
}
