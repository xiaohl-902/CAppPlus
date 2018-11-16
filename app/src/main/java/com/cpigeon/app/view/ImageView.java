package com.cpigeon.app.view;

import android.content.Context;

import android.util.AttributeSet;

import com.cpigeon.app.R;


/**
 * Created by Zhu TingYu on 2018/1/24.
 */

public class ImageView extends android.widget.ImageView {
    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        setScaleType(ScaleType.FIT_XY);
    }
}
