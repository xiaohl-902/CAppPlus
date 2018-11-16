package com.cpigeon.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;


/**
 * Created by Zhu TingYu on 2018/4/4.
 */

public class LineTextView extends LinearLayout {

    TextView title;
    TextView content;

    boolean isLarge;

    String titleString;

    Drawable right;

    int color;
    int leftColor;
    int rightColor;

    public LineTextView(Context context) {
        super(context);
        initView(context);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView(context);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView(context);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LineTextView);
        titleString = array.getString(R.styleable.LineTextView_line_text_title);
        isLarge = array.getBoolean(R.styleable.LineTextView_isLargeText, false);
        right = array.getDrawable(R.styleable.LineTextView_line_text_imageRight);
        color = array.getColor(R.styleable.LineTextView_line_text_color, Color.BLACK);
        leftColor = array.getColor(R.styleable.LineTextView_line_left_text_color, color);
        rightColor = array.getColor(R.styleable.LineTextView_line_right_text_color, color);
    }

    private void initView(Context context) {
        View view = null;
        if(isLarge){
            view = LayoutInflater.from(context).inflate(R.layout.view_line_text_view_large_layout, this, true);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.view_line_text_view_layout, this, true);
        }

        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);

        title.setText(titleString);

        if(right != null){
            content.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        }
        title.setTextColor(leftColor);
        content.setTextColor(rightColor);

    }

    public void setContent(String content){
        this.content.setText(content);
    }
    public void setTitle(String content){
        this.title.setText(content);
    }

    public TextView getContent(){
        return content;
    }

    public String getContentString(){
        return content.getText().toString();
    }

    public TextView getTitle(){
        return title;
    }

    public String getTitleString(){
        return title.getText().toString();
    }


}
