package com.cpigeon.app.viewholder;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/26.
 */

public class PigeonCarMonitorViewHolder extends BaseViewHolder {

    TextView tvState;
    TextView tvTitle;
    TextView tvCountMatch;
    TextView tvCountMonitoring;
    TextView tvCountEndMonitor;
    TextView tvCountNotMonitor;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    ImageView line;
    ImageView icon;
    Context context;

    public PigeonCarMonitorViewHolder(View view) {
        super(view);
        this.context = view.getContext();
        tvState = getView(R.id.state);
        tvTitle = getView(R.id.title);
        tvCountMatch = getView(R.id.match_count);
        tvCountMonitoring = getView(R.id.monitoring_count);
        tvCountEndMonitor = getView(R.id.end_count);
        tvCountNotMonitor = getView(R.id.not_start_count);
        tv1 = getView(R.id.text1);
        tv2 = getView(R.id.text2);
        tv3 = getView(R.id.text3);
        tv4 = getView(R.id.text4);
        line = getView(R.id.line1);
        icon = getView(R.id.icon_image);

    }

    public void bindData(GeCheJianKongOrgInfo data) {
        tvCountMatch.setText(String.valueOf(data.getRaces().size()));
        tvCountMonitoring.setText(String.valueOf(data.getMonitoringCount()));
        tvCountEndMonitor.setText(String.valueOf(data.getEndMonitorCount()));
        tvCountNotMonitor.setText(String.valueOf(data.getNotMonitorCount()));
        tvTitle.setText(data.getOrgName());

        if (data.getMonitoringCount() != 0) {
            tvState.setText("监控中");
            setBackgroundColor(R.id.line1, R.color.white);
            setBackgroundColor(R.id.line1, R.color.color_blue_57bbdfa);
        } else {
            tvState.setText("监控结束");
            setBackgroundColor(R.id.line1, R.color.white);
            setBackgroundColor(R.id.line1, R.color.color_yellow_f49562);
        }

       /* if (getAdapterPosition() % 2 == 0) {
            setImageResource(R.id.icon_image, icons.get(0));
        } else setImageResource(R.id.icon_image, icons.get(1));*/
    }

    public void setItemColor(boolean isRace){
        if(isRace){
            tvState.setTextColor(getColor(R.color.white));
            tvTitle.setTextColor(getColor(R.color.white));
            tv1.setTextColor(getColor(R.color.white));
            tv2.setTextColor(getColor(R.color.white));
            tv3.setTextColor(getColor(R.color.white));
            tv4.setTextColor(getColor(R.color.white));
            tvCountMatch.setTextColor(getColor(R.color.white));
            tvCountMonitoring.setTextColor(getColor(R.color.white));
            tvCountEndMonitor.setTextColor(getColor(R.color.white));
            tvCountNotMonitor.setTextColor(getColor(R.color.white));
            //setImageResource(R.id.icon_image, icons.get(2));
            if(getAdapterPosition() % 2 == 0){
                itemView.setBackgroundColor(context.getResources().getColor(R.color.color_blue_57bbdfa));
            }else itemView.setBackgroundColor(context.getResources().getColor(R.color.color_yellow_f49562));
        }else {
            tvState.setTextColor(getColor(R.color.black));
            tvTitle.setTextColor(getColor(R.color.gray_m));
            tv1.setTextColor(getColor(R.color.gray_m));
            tv2.setTextColor(getColor(R.color.gray_m));
            tv3.setTextColor(getColor(R.color.gray_m));
            tv4.setTextColor(getColor(R.color.gray_m));
            tvCountMatch.setTextColor(getColor(R.color.black));
            tvCountMonitoring.setTextColor(getColor(R.color.black));
            tvCountEndMonitor.setTextColor(getColor(R.color.black));
            tvCountNotMonitor.setTextColor(getColor(R.color.black));
            /*if(getAdapterPosition() % 2 == 0){
                setImageResource(R.id.icon_image, icons.get(0));
            }else setImageResource(R.id.icon_image, icons.get(1));*/
        }
    }

    private int getColor (@ColorRes int resId){
        return context.getResources().getColor(resId);
    }
}
