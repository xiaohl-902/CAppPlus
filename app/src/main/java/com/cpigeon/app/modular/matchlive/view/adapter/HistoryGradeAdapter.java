package com.cpigeon.app.modular.matchlive.view.adapter;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.LineTextView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/17.
 */

public class HistoryGradeAdapter extends BaseQuickAdapter<HistoryGradeInfo, BaseViewHolder> {

    public HistoryGradeAdapter() {
        super(R.layout.item_histroy_grade_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryGradeInfo item) {
        LineTextView time = helper.getView(R.id.time);
        LineTextView entity_name = helper.getView(R.id.entity_name);
        LineTextView rank = helper.getView(R.id.rank);
        LineTextView pigeon_number = helper.getView(R.id.pigeon_number);
        LineTextView foot_id = helper.getView(R.id.foot_id);
        LineTextView displacement = helper.getView(R.id.displacement);

        time.setContent(item.getBssj());
        entity_name.setContent(item.getXmmc());
        rank.setContent(item.getBsmc());
        pigeon_number.setContent(item.getBsgm());
        foot_id.setContent(item.getFoot());
        displacement.setContent(String.valueOf(item.getBskj()) + "KM");

        helper.setText(R.id.order, (helper.getAdapterPosition() - getHeaderLayoutCount() + 1) + "");


    }

    @Override
    protected String getEmptyViewText() {
        return "暂时没有历次赛绩";
    }
}
