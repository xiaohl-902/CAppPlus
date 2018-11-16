package com.cpigeon.app.pigeonnews.adpter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.NewsCommentEntity;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.StringUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class ReplyAdapter extends BaseAdapter {

    List<NewsCommentEntity> data;
    LayoutInflater inflater;
    OnItemReplyClickListener listener;
    Context context;

    public ReplyAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NewsCommentEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_news_reply_layout,null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.reply);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(setColor(StringUtil.toUpperCase(data.get(position).nicheng) +":"+ data.get(position).content));

        convertView.setOnClickListener(v -> {
            InputCommentDialog dialog = new InputCommentDialog();
            dialog.setHint("回复 "+ getNickName(data.get(position).nicheng)+"：");
            dialog.setPushClickListener(editText -> {
                listener.reply(data.get(position),position, editText.getText().toString(),dialog);
            });
            dialog.show(((Activity)context).getFragmentManager(), "");
        });

        return convertView;
    }

    public void setData(List<NewsCommentEntity> data) {
        this.data = data;
    }


    public interface OnItemReplyClickListener{
        void reply(NewsCommentEntity entity, int position, String content,InputCommentDialog dialog);
    }

    public void setOnItemReplyClickListenerListener(OnItemReplyClickListener listener) {
        this.listener = listener;
    }

    public NewsCommentEntity getNewEntity(int position, String content){
        NewsCommentEntity entity = new NewsCommentEntity();
        entity.nicheng = CpigeonData.getInstance().getUserInfo().getNickname()
                +" 回复 "+ getNickName(data.get(position).nicheng);
        entity.userid = String.valueOf(CpigeonData.getInstance().getUserId(context));
        entity.content = content;
        return entity;
    }

    public String getNickName(String s){
        s =  StringUtil.removeAllSpace(s);
        return StringUtil.splitString(s,"回复").get(0);
    }

    private String setColor(String s){
        SpannableStringBuilder spanStr = new SpannableStringBuilder(s);
        List<String> list = StringUtil.splitString(s," 回复 ");
        for (int i = 0,len = list.size(); i < len; i++) {
            String name = list.get(i);
            int start = s.indexOf(name);
            spanStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);
                    ds.setUnderlineText(false);
                }
            },start,start + name.length(),0);
        }

        return spanStr.toString();
    }
}


class ViewHolder{
    TextView textView;
}
