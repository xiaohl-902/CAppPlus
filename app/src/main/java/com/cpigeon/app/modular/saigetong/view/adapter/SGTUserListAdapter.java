package com.cpigeon.app.modular.saigetong.view.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.saigetong.model.bead.SGTUserListEntity;
import com.cpigeon.app.modular.saigetong.view.fragment.SGTRpRecordFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTUserListAdapter extends BaseQuickAdapter<SGTUserListEntity, BaseViewHolder> {

    public SGTUserListAdapter() {
        super(R.layout.item_sgt_home_user_layou, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, SGTUserListEntity item) {

        helper.setText(R.id.tv_content_title, item.getGpmc());
        helper.setText(R.id.item_num,item.getTpcount() > 9000 ? "9999+" : String.valueOf(item.getTpcount()));
        helper.getView(R.id.ll_sgt_home).setOnClickListener(view -> {
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA, item.getUserid())
                    .putExtra(IntentBuilder.KEY_TITLE, item.getGpmc())
                    .startParentActivity((Activity) mContext, SGTRpRecordFragment.class);
        });
    }

    @Override
    protected String getEmptyViewText() {
        return "暂无数据";
    }

}
