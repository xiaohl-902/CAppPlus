package com.cpigeon.app.modular.guide.view.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.guide.model.bean.SignRuleEntity;

import java.util.List;

/**
 * 签到规则
 * Created by Administrator on 2018/5/28.
 */

public class SignRuleAdapter extends BaseQuickAdapter<SignRuleEntity, BaseViewHolder> {

    public SignRuleAdapter(List<SignRuleEntity> data) {
        super(R.layout.item_sign_rule, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SignRuleEntity item) {
        helper.setText(R.id.rule_content, helper.getAdapterPosition() + 1 + "." + item.getGbgz());
    }
}
