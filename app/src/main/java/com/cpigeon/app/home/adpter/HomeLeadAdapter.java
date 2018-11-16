package com.cpigeon.app.home.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/2.
 */

public class HomeLeadAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    List<Integer> icons = Lists.newArrayList(
            R.mipmap.ic_loft_match_pigeon,
            R.mipmap.ic_match_control,
            R.mipmap.ic_pigeon_message,
            R.mipmap.ic_foot_ring_search,
            R.mipmap.ic_line_weather,
            R.mipmap.ic_suixingp1,
            R.mipmap.ic_loft_dynamic,
            R.mipmap.ic_ass_dynamic
    );

    int imgSize;

    private int llHight;//控件宽度

    public HomeLeadAdapter(Context mContext) {
        super(R.layout.item_pigeon_message_home_layout
                , Lists.newArrayList("公棚赛鸽", "比赛监控", "鸽信通", "足环查询", "赛线天气", "信鸽随拍", "公棚动态", "协会动态")
        );
        imgSize = ScreenTool.dip2px(32);
        llHight = ScreenTool.dip2px(76);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {

        LinearLayout item_llz = holder.getView(R.id.item_llz);
        item_llz.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, llHight));
        TextView title = holder.getView(R.id.title);
        title.setTextSize(12);
        title.setTextColor(mContext.getResources().getColor(R.color.black));

        holder.setText(R.id.title, item);

        holder.setViewSize(R.id.icon, imgSize, imgSize);


        if (item.equals("信鸽随拍")) {
            try {
                AppCompatImageView mAppCompatImageView = holder.getView(R.id.icon);
                AnimationDrawable frameAnim = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.animation_suixingp);
                mAppCompatImageView.setBackgroundDrawable(frameAnim);

                frameAnim.start();
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        } else {
            holder.setIconView(R.id.icon, icons.get(holder.getAdapterPosition()));
        }

    }
}
