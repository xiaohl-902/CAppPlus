package com.cpigeon.app.modular.matchlive.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.CommonUitls;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class SearchMatchAdapter extends BaseQuickAdapter<MatchReportXH, BaseViewHolder> {

    public SearchMatchAdapter() {
        super(R.layout.listitem_report_info, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, MatchReportXH item) {
        int mc = item.getMc();
        String name = item.getName();
        String footNumber = item.getFoot();


        switch (mc) {
            case 1:
                helper.setVisible(R.id.report_info_item_mc, false);
                helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_frist);
                break;
            case 2:
                helper.setVisible(R.id.report_info_item_mc, false);
                helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_second);
                break;
            case 3:
                helper.setVisible(R.id.report_info_item_mc, false);
                helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_thrid);
                break;
            default:

                helper.setText(R.id.report_info_item_mc, mc + "");
                break;
        }
        helper.setText(R.id.report_info_item_xm, name);
        helper.setText(R.id.report_info_item_hh, footNumber);
    }

    @Override
    public void setNewData(List<MatchReportXH> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            CommonTool.setEmptyView(this,"没有搜索到信息");
        }
    }
}
