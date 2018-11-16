package com.cpigeon.app.modular.saigetong.view.adapter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.saigetong.model.bead.SGTRpRecordEntity;
import com.cpigeon.app.modular.saigetong.view.fragment.SGTGzFragment;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SGTRpRecordAdapter extends BaseQuickAdapter<SGTRpRecordEntity.ListBean, BaseViewHolder> {

    private SGTRpRecordEntity mSGTRpRecordEntity;
    private Activity mActivity;

    public SGTRpRecordAdapter() {
        super(R.layout.item_sgt_home_user_layou, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder helper, SGTRpRecordEntity.ListBean item) {

        helper.setText(R.id.tv_content_title, item.getTitle());
        helper.setText(R.id.item_num, item.getTpcount() > 9999 ? "9999+" : String.valueOf(item.getTpcount()));
        helper.getView(R.id.ll_sgt_home).setOnClickListener(view -> {
            if (mSGTRpRecordEntity != null) {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_DATA, mSGTRpRecordEntity.getGuid())
                        .putExtra(IntentBuilder.KEY_TYPE, item.getTid())
                        .putExtra(IntentBuilder.KEY_TITLE, item.getTitle())
                        .startParentActivity((Activity) mContext, SGTGzFragment.class);
            }

//            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA,
//                    String.valueOf(item.getTid()))
//                    .startParentActivity((Activity) mContext, SGTGzFragment.class);
        });
    }

    @Override
    protected String getEmptyViewText() {
        return "暂无数据";
    }

    public void setSGTRpRecordEntity(SGTRpRecordEntity mSGTRpRecordEntity, Activity mActivity) {
        this.mSGTRpRecordEntity = mSGTRpRecordEntity;
        this.mActivity = mActivity;
    }
}
