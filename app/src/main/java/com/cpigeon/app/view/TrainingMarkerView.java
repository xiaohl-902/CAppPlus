package com.cpigeon.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.TrainingDataEntity;
import com.cpigeon.app.utils.ScreenTool;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/4/20.
 */

public class TrainingMarkerView extends MarkerView {

    List<HistoryGradeInfo> data;
    LineTextView lineTextView1;
    LineTextView lineTextView2;
    LineTextView lineTextView3;
    LineTextView lineTextView4;

    double position;

    public TrainingMarkerView(Context context, List<HistoryGradeInfo> data) {
        super(context, R.layout.item_training_marker_layout);
        this.data = data;
        lineTextView1 = findViewById(R.id.line_text_1);
        lineTextView2 = findViewById(R.id.line_text_2);
        lineTextView3 = findViewById(R.id.line_text_3);
        lineTextView4 = findViewById(R.id.line_text_4);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        position = e.getX();
        if (e.getX() == 0) {
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(1), ScreenTool.dip2px(1)));
        } else {
            setLayoutParams(new LayoutParams(ScreenTool.dip2px(240), ViewGroup.LayoutParams.WRAP_CONTENT));
            HistoryGradeInfo entity = data.get((int) e.getX() - 1);
            lineTextView1.setContent(entity.getXmmc());
            lineTextView2.setContent(entity.getBsmc());
            lineTextView3.setContent(entity.getSpeed() + "M");
            lineTextView4.setContent(entity.getFirstSpeed() + "M");
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
