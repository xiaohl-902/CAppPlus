package com.cpigeon.app.utils.textspan;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.SpannableClickable;
import com.cpigeon.app.utils.ToastUtil;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class StringSpanUtil {
    public static SpannableString setClickableSpan(String textStr, @ColorRes int resId) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(MyApp.getInstance().getBaseContext().getResources().getColor(resId)){
                                    @Override
                                    public void onClick(View widget) {
                                        ToastUtil.showShortToast(widget.getContext(),"1231");
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static SpannableString setClickableNewSpan(String textStr, @ColorRes int resId, OnStringSpanClick click) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ClickableSpan(){
                                    @Override
                                    public void onClick(View widget) {
                                        click.onClick(widget);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        //删除下划线，设置字体颜色为蓝色
                                        ds.setColor(MyApp.getInstance().getBaseContext().getResources().getColor(resId));
                                        ds.setUnderlineText(false);
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    //定义一个点击每个部分文字的处理方法
    public static SpannableString addClickPart(String str, @ColorRes int resId , OnStringSpanClick click) {
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        click.onClick(widget);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色为蓝色
                        ds.setColor(MyApp.getInstance().getBaseContext().getResources().getColor(resId));
                        ds.setUnderlineText(false);
                    }
                },0,str.length(),0);
        return spanStr;
    }
}
