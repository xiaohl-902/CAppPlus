package com.cpigeon.app.message.ui.order.adpter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.message.ui.order.ui.vocice.EditVoiceFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.LineTextView;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class VoiceListAdapter extends BaseQuickAdapter<VoiceEntity, BaseViewHolder> {

    public VoiceListAdapter() {
        super(R.layout.item_voice_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceEntity item) {
        LineTextView name = helper.getView(R.id.lineTextUnit);
        LineTextView number = helper.getView(R.id.lineTextTFN);
        LineTextView type = helper.getView(R.id.lineVoiceType);

        type.setContent(item.lx);
        name.setContent(item.dwmc);
        number.setContent(item.sh);

        helper.getView(R.id.tvEditVoice).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, item)
                    .startParentActivity((Activity) mContext, EditVoiceFragment.class);
        });
    }

    @Override
    protected String getEmptyViewText() {
        return "暂无发票信息";
    }
}
