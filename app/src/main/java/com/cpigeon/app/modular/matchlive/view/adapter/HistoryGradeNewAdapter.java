package com.cpigeon.app.modular.matchlive.view.adapter;

import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.LineTextView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/3.
 */

public class HistoryGradeNewAdapter extends BaseQuickAdapter<HistoryGradeInfo, BaseViewHolder>{

    String type = "xh";


    public HistoryGradeNewAdapter() {
        super(R.layout.item_history_grade_new_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryGradeInfo item) {
        LineTextView rank = helper.getView(R.id.rank);
        LineTextView displacement = helper.getView(R.id.displacement);
        LineTextView speed = helper.getView(R.id.speed);
        LineTextView pigeon_number = helper.getView(R.id.pigeon_number);
        LineTextView time = helper.getView(R.id.time);
        LineTextView entity_name = helper.getView(R.id.entity_name);
        LineTextView first_speed = helper.getView(R.id.first_speed);

        if(type.equals("gp")){
            first_speed.setVisibility(View.VISIBLE);
        }else {
            first_speed.setVisibility(View.GONE);
            rank.setTitle("比赛名次");
            displacement.setTitle("比赛空距");
            pigeon_number.setTitle("参赛羽数");
            time.setTitle("比赛时间");
        }


        time.setContent(item.getBssj());
        entity_name.setContent(item.getXmmc());
        rank.setContent(item.getBsmc() + "名");
        pigeon_number.setContent(item.getBsgm() + "羽");
        first_speed.setContent(item.getFirstSpeed() + "M");
        speed.setContent(item.getSpeed() + "M");
        displacement.setContent(String.valueOf(item.getBskj()) + "KM");

        helper.setText(R.id.order, (helper.getAdapterPosition() - getHeaderLayoutCount() + 1) + "");
    }

    @Override
    protected String getEmptyViewText() {
        return "暂时没有历次赛绩";
    }

    public void setType(String type) {
        this.type = type;
    }
}
