package com.cpigeon.app.pigeonnews.adpter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/15.
 */

public class NewsRelatedAdapter extends BaseQuickAdapter<NewsEntity, BaseViewHolder> {

    public NewsRelatedAdapter() {
        super(R.layout.item_line_text_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsEntity item) {
        TextView textView = holder.getView(R.id.text_content);

        textView.setText(item.title);

    }
}
