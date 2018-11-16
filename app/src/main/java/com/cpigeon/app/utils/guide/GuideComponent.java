package com.cpigeon.app.utils.guide;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.view.guideview.Component;

/**
 * Created by Zhu TingYu on 2018/6/6.
 */

public class GuideComponent implements Component {

    private int xOffset = 0;
    private int yOffset = 0;
    private int guideLocation = Component.ANCHOR_BOTTOM;
    private int viewLocation = Component.FIT_CENTER;
    private TextView textView;
    private String guideHint;
    private TextView tvOk;

    public interface OnOkClickListener{
        void onclick();
    }

    private OnOkClickListener onOkClickListener;

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll;
        if (guideLocation == Component.ANCHOR_BOTTOM) {
            ll = (LinearLayout) inflater.inflate(R.layout.view_defalut_arrow_up_guide_layout, null);
        } else {
            ll = (LinearLayout) inflater.inflate(R.layout.view_defalut_arrow_down_guide_layout, null);
        }

        textView = ll.findViewById(R.id.text);
        textView.setText(guideHint);

        tvOk = ll.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(v -> {
            if(onOkClickListener != null){
                onOkClickListener.onclick();
            }
        });

        return ll;
    }


    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    @Override
    public int getAnchor() {
        return guideLocation;
    }

    @Override
    public int getFitPosition() {
        return viewLocation;
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setGuideLocation(int guideLocation) {
        this.guideLocation = guideLocation;
    }

    public void setViewLocation(int viewLocation) {
        this.viewLocation = viewLocation;
    }

    public void setGuideHint(String guideHint) {
        this.guideHint = guideHint;
    }
}
