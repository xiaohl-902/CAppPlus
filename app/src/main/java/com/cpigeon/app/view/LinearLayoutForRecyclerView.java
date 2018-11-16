package com.cpigeon.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by TingYu Zhu on 2017/9/8.
 */

public class LinearLayoutForRecyclerView extends LinearLayout {

    public BaseAdapter adapter;

    public LinearLayoutForRecyclerView(Context context) {
        super(context);
    }

    public LinearLayoutForRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    public LinearLayoutForRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        addView();
    }

    public void addView(){
        removeAllViews();
        for (int i = 0, len = adapter.getCount(); i < len; i++){
            View view = adapter.getView(i, null, this);
            addView(view);
        }
    }

    public void addFootView(View view){
        addView(view);
    }

}
