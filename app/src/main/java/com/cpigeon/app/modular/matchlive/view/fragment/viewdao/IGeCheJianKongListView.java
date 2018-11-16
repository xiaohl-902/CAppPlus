package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.commonstandard.view.activity.IPageTurnView;

/**
 * Created by chenshuai on 2017/7/11.
 */

public interface IGeCheJianKongListView extends IPageTurnView<MultiItemEntity> {
    /**
     * 获取组织类型 1：公棚 2：协会
     *
     * @return
     */
    String getOrgType();

    /**
     * 获取搜索Key
     *
     * @return
     */
    String getSearchKey();
}
