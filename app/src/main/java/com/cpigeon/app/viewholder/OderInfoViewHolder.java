package com.cpigeon.app.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.utils.StringUtil;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class OderInfoViewHolder extends BaseViewHolder {

    public TextView orderId;
    public TextView orderContent;
    public TextView orderTime;
    public TextView orderPrice;
    public LinearLayout llBottom;
    public TextView orderPayText;

    public CheckBox checkBox;
    public TextView tvProtocol;

    public OderInfoViewHolder(View itemView) {
        super(itemView);
        orderId = getView(R.id.tv_order_number_content);
        orderContent = getView(R.id.tv_order_name_content);
        orderTime = getView(R.id.tv_order_time_content);
        orderPrice = getView(R.id.tv_order_price_content);
        llBottom = getView(R.id.ll1);
        orderPayText = getView(R.id.text1);

        checkBox = getView(R.id.cb_order_protocol);
        tvProtocol = getView(R.id.tv_order_protocol);
    }

    public void bindData(OrderInfoEntity entity){
        orderId.setText(entity.number);
        orderContent.setText(entity.item);
        orderTime.setText(entity.time);
        orderPrice.setText(entity.price + "元" + "   (微信手续费)" + StringUtil.twoPoint(Double.parseDouble(entity.price) * 0.01)+"元");
    }

    public void visibleBottom(){
        llBottom.setVisibility(View.VISIBLE);
        orderPayText.setVisibility(View.VISIBLE);
    }

}
