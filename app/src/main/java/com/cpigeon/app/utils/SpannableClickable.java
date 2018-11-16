package com.cpigeon.app.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;

/**
 * Created by Administrator on 2017/3/21.
 */

public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_8290AF;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = MyApp.getInstance().getBaseContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}