package com.cpigeon.app.message.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/17.
 */

public class PigeonMessageHomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int sizeH;
    private int sizeW;
    private int imgSize;
    private List<Integer> icList;


    public PigeonMessageHomeAdapter(Context context, List<String> list) {

        super(R.layout.item_pigeon_message_home_layout, list);

        icList = Lists.newArrayList(R.mipmap.ic_send_message,
                R.mipmap.ic_phone_book,
                R.mipmap.ic_common_message,
                R.mipmap.ic_send_history,
                R.mipmap.ic_modify_sign,
                R.mipmap.ic_user_helper,
                R.mipmap.ic_person_message,
                R.mipmap.ic_user_greement);


        sizeH = (ScreenTool.getScreenHeight(context) - ScreenTool.dip2px(80)) / 4 ;
        sizeW = ScreenTool.getScreenWidth(context) / 2;
        imgSize = (ScreenTool.getScreenWidth(context) - ScreenTool.dip2px(32)) / 4;
    }

    @Override
    public int getItemCount() {
        return icList.size();
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.title, item);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeW, sizeH);

        holder.itemView.setLayoutParams(params);

        AppCompatImageView imageView = holder.findViewById(R.id.icon);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(imgSize, imgSize);
        imageView.setLayoutParams(params1);


        holder.setIconView(R.id.icon, icList.get(holder.getAdapterPosition()));

    }

}
