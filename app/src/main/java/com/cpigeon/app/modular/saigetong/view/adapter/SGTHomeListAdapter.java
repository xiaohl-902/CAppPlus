//package com.cpigeon.app.modular.saigetong.view.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.View;
//import android.widget.ImageButton;
//
//import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.chad.library.adapter.base.entity.AbstractExpandableItem;
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.cpigeon.cpigeonhelper.R;
//import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
//import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
//import com.cpigeon.cpigeonhelper.utils.ImgChooseDialogUtil;
//import com.cpigeon.cpigeonhelper.utils.RequestCodeService;
//import com.cpigeon.cpigeonhelper.video.RecordedActivity;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 赛格通主页列表的adapter
// * Created by Administrator on 2017/12/1.
// */
//public class SGTHomeListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
//
//    public static final int TYPE_TITLE = 1;
//    public static final int TYPE_CONTENT = 2;
//
//    public static final int TYPE_ORG = 1;
//    public static final int TYPE_RACE = 2;
//
//    private Intent intent;
//
//    private String TAG = "SGTHomeListAdapter";
//
//    private int mPisition;
//
//    /**
//     * Same as QuickAdapter#QuickAdapter(Context,int) but with
//     * some initialization data.
//     *
//     * @param data A new list is created out of this one to avoid mutable list
//     */
//    public SGTHomeListAdapter(List<MultiItemEntity> data) {
//        super(data);
//        addItemType(TYPE_ORG, R.layout.item_list_title);
////        addItemType(TYPE_CONTENT, R.layout.item_list_content);
//        addItemType(TYPE_RACE, R.layout.item_list_con_content);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
//        switch (helper.getItemViewType()) {
//            case TYPE_ORG:
////                Log.d(TAG, "convert: 1");
//                if (((OrgItem) item).orgInfo.getTag() == 1) {
//                    ((ImageButton) helper.getView(R.id.it_sgt_z_l_btn)).setImageResource(R.mipmap.right_img);
//                    helper.getView(R.id.item_dividerline).setVisibility(View.VISIBLE);
//                } else {
//                    ((ImageButton) helper.getView(R.id.it_sgt_z_l_btn)).setImageResource(R.mipmap.zhankai_img);
//                    helper.getView(R.id.item_dividerline).setVisibility(View.GONE);
//                }
////                final MatchTitleItem titleItem = (MatchTitleItem) item;
//                helper.setText(R.id.it_sgt_z_name, ((OrgItem) item).orgInfo.getXingming());
//                helper.setText(R.id.it_sgt_z_num, ((OrgItem) item).orgInfo.getCount() + "羽");
////                helper.setText(R.id.it_sgt_z_num, titleItem.getMatchInfo().getCount() + "羽");
//
//                mPisition = helper.getPosition();
//
//                helper.getView(R.id.it_sgt_z_r_btn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ImgChooseDialogUtil.createImgChonseDialog((Activity) mContext, new SaActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                switch (which) {
//                                    case 1:
//                                        SGTHomeListEntity mSGTHomeListEntity = ((OrgItem) item).orgInfo;
//                                        mSGTHomeListEntity.setImgTag(1);//主页列表数据返回
//                                        EventBus.getDefault().post(mSGTHomeListEntity);
//                                        //跳转到相册
//                                        ImgChooseDialogUtil.jumpAlbum((Activity) mContext, PictureMimeType.ofImage(), 1, PictureConfig.LUBAN_COMPRESS_MODE, RequestCodeService.SGT_IMG_CHOOSE_HOME);
//                                        break;
//                                    case 2:
//                                        //跳转到相机
//                                        intent = new Intent(mContext, RecordedActivity.class);
//                                        intent.putExtra("type", "sgt");
//                                        intent.putExtra("code", 2);//1：图片选择  2：拍摄图片
//                                        intent.putExtra("uplodType", 2);//1：单个足环  2：鸽主
//                                        intent.putExtra("SGTHomeListEntity", ((OrgItem) item).orgInfo);
//                                        mContext.startActivity(intent);
//                                        break;
//                                }
//                            }
//                        });
//                    }
//                });
//
//                break;
//            case TYPE_RACE:
//                helper.getView(R.id.ll_z).setClickable(false);
////                Log.d(TAG, "convert: 2");
//                helper.setText(R.id.it_sgt_name, ((RaceItem) item).race.getFoot());
//                helper.setText(R.id.it_sgt_num, ((RaceItem) item).race.getId());
//
//                break;
//        }
//    }
//
//    public static List<MultiItemEntity> get(List<SGTHomeListEntity> data) {
//        List<MultiItemEntity> result = new ArrayList<>();
//        if (data == null) {
//            return result;
//        }
//        OrgItem orgItem;
//        RaceItem raceItem;
//        if (data.size() > 0) {
//            for (SGTHomeListEntity orginfo : data) {
//                orgItem = new OrgItem(orginfo);
//                if (orginfo.getData() != null)
//                    for (SGTHomeListEntity.DataBean race : orginfo.getData()) {
//                        raceItem = new RaceItem(race);
//                        orgItem.addSubItem(raceItem);
//                    }
//
//                result.add(orgItem);
//            }
//        }
//
//        return result;
//    }
//
//
//    public static class OrgItem extends AbstractExpandableItem<RaceItem> implements MultiItemEntity {
//
//        SGTHomeListEntity orgInfo;
//
//        public OrgItem(SGTHomeListEntity orgInfo) {
//            this.orgInfo = orgInfo;
//        }
//
//        @Override
//        public int getItemType() {
//            return TYPE_ORG;
//        }
//
//        @Override
//        public int getLevel() {
//            return 0;
//        }
//
//        public SGTHomeListEntity getOrgInfo() {
//            return orgInfo;
//        }
//    }
//
//    public static class RaceItem extends AbstractExpandableItem<SGTHomeListEntity> implements MultiItemEntity {
//
//        public SGTHomeListEntity.DataBean getRace() {
//            return race;
//        }
//
//        SGTHomeListEntity.DataBean race;
//
//        public RaceItem(SGTHomeListEntity.DataBean race) {
//            this.race = race;
//        }
//
//        @Override
//        public int getItemType() {
//            return TYPE_RACE;
//        }
//
//        @Override
//        public int getLevel() {
//            return 1;
//        }
//    }
//}
