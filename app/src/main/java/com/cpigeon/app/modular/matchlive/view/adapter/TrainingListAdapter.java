package com.cpigeon.app.modular.matchlive.view.adapter;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.TrainingDataEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.LineTextView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class TrainingListAdapter extends BaseQuickAdapter<HistoryGradeInfo, BaseViewHolder> {



    public TrainingListAdapter() {
        super(R.layout.item_traning_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryGradeInfo item) {
        LineTextView time = helper.getView(R.id.time);
        LineTextView entity_name = helper.getView(R.id.entity_name);
        LineTextView rank = helper.getView(R.id.rank);
        LineTextView pigeon_number = helper.getView(R.id.pigeon_number);
        LineTextView displacement = helper.getView(R.id.displacement);
        LineTextView speed = helper.getView(R.id.speed);
        LineTextView first_speed = helper.getView(R.id.first_speed);

        time.setContent(item.getBssj());
        entity_name.setContent(item.getXmmc());
        rank.setContent(item.getBsmc());
        pigeon_number.setContent(item.getBsgm());
        displacement.setContent(String.valueOf(item.getBskj()) + "KM");
        speed.setContent(String.valueOf(item.getSpeed()) + "M");
        first_speed.setContent(String.valueOf(item.getFirstSpeed()) + "M");

        helper.setText(R.id.order, (helper.getAdapterPosition() + 1) + "");
    }
}
