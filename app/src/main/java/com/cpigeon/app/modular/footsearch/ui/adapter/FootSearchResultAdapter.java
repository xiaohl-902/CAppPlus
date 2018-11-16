package com.cpigeon.app.modular.footsearch.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.entity.FootInfoEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/22.
 */

public class FootSearchResultAdapter extends BaseQuickAdapter<FootInfoEntity, BaseViewHolder>{

    public FootSearchResultAdapter() {
        super(R.layout.item_foot_search_result_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, FootInfoEntity item) {
        holder.setText(R.id.text1_content, item.foot);
        holder.setText(R.id.text2_content, item.xmmc);
        holder.setText(R.id.text3_content, item.orgname);
        holder.setText(R.id.text4_content, item.bskj + "公里");
        holder.setText(R.id.text5_content, item.st);
        holder.setText(R.id.text6_content, Integer.valueOf(item.csys) == 0 ? "训放无" : item.csys);
        holder.setText(R.id.text7_content, item.mc);
        holder.setText(R.id.text8_content, item.speed + "M");

        holder.setText(R.id.order, String.valueOf(holder.getAdapterPosition() + 1));

        if(StringValid.isStringValid(item.name)){
            holder.setVisible(R.id.rl_name, true);
            holder.setText(R.id.name, item.name);
        }else {
            holder.setVisible(R.id.rl_name, false);
        }
    }
}
