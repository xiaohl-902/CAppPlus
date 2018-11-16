package com.cpigeon.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.utils.ScreenTool;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/17.
 */

public class GradeMarkerView extends MarkerView {

    LineTextView entity_name;
    LineTextView pigeon_number;
    LineTextView foot_id;
    LineTextView time;

    List<HistoryGradeInfo> data;

    double position;


    public GradeMarkerView(Context context, List<HistoryGradeInfo> data) {
        super(context, R.layout.item_grade_marker_layout);
        entity_name = findViewById(R.id.entity_name);
        pigeon_number = findViewById(R.id.pigeon_number);
        foot_id = findViewById(R.id.foot_id);
        time = findViewById(R.id.time);
        this.data = data;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        position = e.getX();
        if(e.getX() == 0){
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(1), ScreenTool.dip2px(1)));
        }else {
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(240), ViewGroup.LayoutParams.WRAP_CONTENT));
            HistoryGradeInfo entity = data.get((int) e.getX() - 1);
            entity_name.setContent(entity.getXmmc());
            pigeon_number.setContent(entity.getBsgm());
            foot_id.setContent(entity.getFoot());
            time.setContent(entity.getBssj());
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2) * 2f, -getHeight() - 10);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        if(position == 0){
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(1), ScreenTool.dip2px(1)));
        }else {
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(240), ViewGroup.LayoutParams.WRAP_CONTENT));
            super.draw(canvas, posX, posY);
        }
    }
}
