package com.cpigeon.app.modular.saigetong.view.adapter;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseQuickAdapter;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.modular.saigetong.model.bead.SGTFootSearchEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class SGTSearchAdapter extends BaseQuickAdapter<SGTFootSearchEntity, BaseViewHolder> {

    public SGTSearchAdapter(List<SGTFootSearchEntity> data) {
        super(R.layout.item_sgt_search_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SGTFootSearchEntity item) {

        helper.setText(R.id.item_centent1,item.getXingming());
        helper.setText(R.id.item_centent2,item.getCskh());
    }


}
