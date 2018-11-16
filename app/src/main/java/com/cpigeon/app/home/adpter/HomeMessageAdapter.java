package com.cpigeon.app.home.adpter;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.HomeMessageEntity;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/30.
 */

public class HomeMessageAdapter extends BaseQuickAdapter<HomeMessageEntity, BaseViewHolder>{

    public HomeMessageAdapter() {
        super(R.layout.item_home_message_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeMessageEntity item) {
        holder.setText(R.id.title,item.getTitle());
        holder.setText(R.id.time, DateTool.format(item.getTime().getTime(),DateTool.FORMAT_DATETIME));
        WebView webView = holder.getView(R.id.content);
        webView.loadDataWithBaseURL(null, item.getContent(), "text/html", "utf-8", null);
    }

    @Override
    protected String getEmptyViewText() {
        return "暂时没有公告";
    }
}
