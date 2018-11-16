package com.cpigeon.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class LineInputView extends LinearLayout {

    TextView title;
    EditText content;

    String titleString;
    boolean isSingle;
    int contentPadding;
    int inputType;

    public LineInputView(Context context) {
        super(context);
        initView(context);
    }

    public LineInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView(context);
    }

    public LineInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView(context);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.InputTextView);
        titleString = array.getString(R.styleable.InputTextView_title);
        isSingle = array.getBoolean(R.styleable.InputTextView_isSingle, true);
        contentPadding = (int) array.getDimension(R.styleable.InputTextView_editPadding, 40f);
        inputType = (int) array.getInt(R.styleable.InputTextView_input_text_InputType, 0);
    }

    private void initView(Context context) {
        View view;

        view = LayoutInflater.from(context).inflate(R.layout.view_input_text_view_layout, this, true);

        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);

        if(isSingle){
            content.setSingleLine(isSingle);
        }

        if(inputType != 0){
            content.setInputType(inputType);
        }


        content.setPadding(0,contentPadding, 0, contentPadding);

        title.setText(titleString);
    }

    public EditText getContent(){
        return content;
    }

    public void setContent(String content){
        this.content.setText(content);
    }

}
