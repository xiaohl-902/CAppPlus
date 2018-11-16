package com.cpigeon.app.modular.matchlive.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.ImageView;
import com.cpigeon.app.viewholder.PigeonCarMonitorViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshuai on 2017/7/11.
 */

public class GeCheJianKongExpandListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_ORG = 1;
    public static final int TYPE_RACE = 2;
    List<Integer> icons;
    public int orgItemPosition;


    public GeCheJianKongExpandListAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_ORG, R.layout.listitem_gechejiankong_title);
        addItemType(TYPE_RACE, R.layout.listitem_gechejiankong_race);
        icons = Lists.newArrayList(R.mipmap.ic_vertical_yellow, R.mipmap.ic_vertical_blue
                , R.mipmap.ic_vertical_withe);
        setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.item_adpter_load_more_layout;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.load_more;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.load_more;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_more;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemEntity multiItemEntity) {
        switch (holder.getItemViewType()) {
            case TYPE_ORG:
                OrgItem item = (OrgItem) multiItemEntity;
                GeCheJianKongOrgInfo data = item.orgInfo;
                /*holder.bindData(orgInfo);
                holder.setItemColor(orgInfo.isRace());*/
                holder.setText(R.id.match_count, String.valueOf(data.getRaces().size()));
                holder.setText(R.id.monitoring_count, String.valueOf(data.getMonitoringCount()));
                holder.setText(R.id.end_count, String.valueOf(data.getEndMonitorCount()));
                holder.setText(R.id.not_start_count, String.valueOf(data.weikaiqi));
                holder.setText(R.id.title, data.getOrgName());

                if (data.getMonitoringCount() != 0) {
                    holder.setText(R.id.state, "监控中");

                } else {
                    if(data.getEndMonitorCount() != 0){
                        holder.setText(R.id.state, "已结束");
                    }else {
                        holder.setText(R.id.state, "未开启");
                    }

                }


                if (((OrgItem) multiItemEntity).getPosition() % 2 == 0) {
                    holder.setImageResource(R.id.icon_image, icons.get(0));
                    holder.setVisible(R.id.line_blue, false);
                    holder.setVisible(R.id.line_yellow, true);
                } else {
                    holder.setImageResource(R.id.icon_image, icons.get(1));
                    holder.setVisible(R.id.line_blue, true);
                    holder.setVisible(R.id.line_yellow, false);
                }

                // holder.setTextColor(R.id.state, item.isExpanded() ? R.color.white : R.color.black);

                if (item.isExpanded()) {
                    holder.setTextColor(R.id.state, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.title, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.text1, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.text2, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.text3, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.text4, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.monitoring_count, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.match_count, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.end_count, mContext.getResources().getColor(R.color.white));
                    holder.setTextColor(R.id.not_start_count, mContext.getResources().getColor(R.color.white));
                    holder.setImageResource(R.id.icon_image, icons.get(2));
                    if (holder.getAdapterPosition() % 2 == 0) {
                        holder.setBackgroundColor(R.id.ll_parent, mContext.getResources().getColor(R.color.color_yellow_f49562));
                    } else {
                        holder.setBackgroundColor(R.id.ll_parent, mContext.getResources().getColor(R.color.color_blue_57bbdfa));
                    }
                } else {
                    holder.setTextColor(R.id.state, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setTextColor(R.id.title, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setTextColor(R.id.text1, mContext.getResources().getColor(R.color.gray_m));
                    holder.setTextColor(R.id.text2, mContext.getResources().getColor(R.color.gray_m));
                    holder.setTextColor(R.id.text3, mContext.getResources().getColor(R.color.gray_m));
                    holder.setTextColor(R.id.text4, mContext.getResources().getColor(R.color.gray_m));
                    holder.setTextColor(R.id.monitoring_count, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setTextColor(R.id.match_count, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setTextColor(R.id.end_count, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setTextColor(R.id.not_start_count, mContext.getResources().getColor(R.color.text_color_4c4c4c));
                    holder.setBackgroundColor(R.id.ll_parent, mContext.getResources().getColor(R.color.white));
                }


                break;
            case TYPE_RACE:
                TextView state = holder.getView(R.id.state);
                TextView title = holder.getView(R.id.title);
                holder.setText(R.id.title, ((RaceItem) multiItemEntity).race.getRaceName());
                holder.setText(R.id.state, ((RaceItem) multiItemEntity).race.getState());

                if(((RaceItem) multiItemEntity).race.getStateCode() == GeCheJianKongOrgInfo.STATE_MONITORING){
                    title.setTextColor(mContext.getResources().getColor(R.color.black));
                    state.setTextColor(mContext.getResources().getColor(R.color.black));

                }else {
                    title.setTextColor(mContext.getResources().getColor(R.color.text_color_b2b2b));
                    state.setTextColor(mContext.getResources().getColor(R.color.text_color_b2b2b));
                }

                if(((RaceItem) multiItemEntity).race.getPosition() == getRaceItemCount() - 1){
                    holder.setVisible(R.id.line, false);
                }else holder.setVisible(R.id.line, true);
                break;
        }
    }

    public static List<MultiItemEntity> get(List<GeCheJianKongOrgInfo> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        OrgItem orgItem;
        RaceItem raceItem;
        if (data.size() > 0) {
            for (int i = 0, len = data.size(); i < len; i++) {
                GeCheJianKongOrgInfo orginfo = data.get(i);
                orgItem = new OrgItem(orginfo);
                orgItem.setPosition(i);
                if (orginfo.getRaces() != null)
                    for (int j = 0, len2 = orginfo.getRaces().size(); j < len2 ; j++) {
                        GeCheJianKongRace race = orginfo.getRaces().get(j);
                        race.setPosition(j);
                        race.setParentsPosition(i);
                        raceItem = new RaceItem(race);
                        orgItem.addSubItem(raceItem);
                    }

                result.add(orgItem);
            }
        }
        return result;
    }


    public static class OrgItem extends AbstractExpandableItem<RaceItem> implements MultiItemEntity {

        GeCheJianKongOrgInfo orgInfo;
        int position;

        public OrgItem(GeCheJianKongOrgInfo orgInfo) {
            this.orgInfo = orgInfo;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public int getItemType() {
            return TYPE_ORG;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        public GeCheJianKongOrgInfo getOrgInfo() {
            return orgInfo;
        }
    }

    public static class RaceItem implements MultiItemEntity {
        public GeCheJianKongRace getRace() {
            return race;
        }

        GeCheJianKongRace race;

        public RaceItem(GeCheJianKongRace race) {
            this.race = race;
        }

        @Override
        public int getItemType() {
            return TYPE_RACE;
        }
    }

    private int getRaceItemCount() {
        int count = 0;
        for (int i = 0, len = mData.size(); i < len; i++) {
            if(mData.get(i) instanceof RaceItem){
                count++;
            }
        }
        return count;
    }
}
