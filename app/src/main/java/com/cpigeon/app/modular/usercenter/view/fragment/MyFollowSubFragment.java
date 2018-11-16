package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.home.view.activity.SearchActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceXunFangActivity;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.modular.usercenter.presenter.UserFollowPresenter;
import com.cpigeon.app.modular.usercenter.view.adapter.UserFollowAdapter;
import com.cpigeon.app.modular.usercenter.view.fragment.viewdao.IUserFollowView;
import com.cpigeon.app.utils.CpigeonData;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/6/28.
 */

public class MyFollowSubFragment extends BasePageTurnFragment<UserFollowPresenter, UserFollowAdapter, UserFollow> implements IUserFollowView {
    public static final String KEY_FOLLOW_TYPE = "follow_type";
    public static final String FOLLOW_TYPE_RACE = "race";
    public static final String FOLLOW_TYPE_XIEHUI = "xiehui";
    public static final String FOLLOW_TYPE_GONGPENG = "gongpeng";
    String _followType = "all";
    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            UserFollow userFollow = (UserFollow) adapter.getData().get(position);
            if ("比赛".equals(userFollow.getFtype())) {
                mPresenter.startMatchLiveView(userFollow);
            } else if ("协会".equals(userFollow.getFtype()) || "公棚".equals(userFollow.getFtype())) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();                           //创建Bundle对象
                bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCHKEY, userFollow.getDisplay());     //装入数据
                bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCH_HINT_TEXT, "比赛名称、" + userFollow.getFtype() + "名称");     //装入数据
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int position) {
            final UserFollow userFollow = (UserFollow) baseQuickAdapter.getData().get(position);
            if (view.getId() == R.id.layout_item_follow_status) {
                SweetAlertDialog dialogPrompt = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText(getString(R.string.prompt))
                        .setContentText("确定取消关注")
                        .setConfirmText(getString(R.string.confirm))
                        .setCancelText("我要继续关注")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mPresenter.removeUserFollow(position, userFollow.getFid());
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String type = getArguments().getString(KEY_FOLLOW_TYPE);
        if (!TextUtils.isEmpty(type) &&
                (type.equals(FOLLOW_TYPE_GONGPENG) ||
                        type.equals(FOLLOW_TYPE_XIEHUI) ||
                        type.equals(FOLLOW_TYPE_RACE))) {
            _followType = type;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected UserFollowPresenter initPresenter() {
        return new UserFollowPresenter(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }

    @Override
    protected int getDefaultPageSize() {
        return 10;
    }

    @Override
    protected String getEmptyDataTips() {
        return "还没有关注" + (getFollowType().equals(FOLLOW_TYPE_RACE) ? "比赛" : getFollowType().equals(FOLLOW_TYPE_XIEHUI) ? "协会" : getFollowType().equals(FOLLOW_TYPE_GONGPENG) ? "公棚" : "") + "呢，快去看看吧";
    }

    @Override
    public UserFollowAdapter getNewAdapterWithNoData() {
        UserFollowAdapter adapter = new UserFollowAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setOnItemChildClickListener(onItemChildClickListener);
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadUserFollowNext();
    }

    @Override
    public String getFollowType() {
        return _followType;
    }

    @Override
    public void removeItem(int position) {
        mAdapter.remove(position);
    }

    @Override
    public void entryMatchDetialView(MatchInfo matchInfo) {
        if (matchInfo == null) {
            showTips("比赛未找到", TipType.DialogError);
            return;
        }
        if (checkArrearage(matchInfo)) return;
        Intent intent = new Intent(getActivity(), matchInfo.isMatch() ? RaceReportActivity.class : RaceXunFangActivity.class);
        Bundle bundle = new Bundle();                //创建Bundle对象
        bundle.putSerializable("matchinfo", matchInfo);     //装入数据
        bundle.putString("loadType", matchInfo.getLx());
        if ("jg".equals(matchInfo.getDt()))
            bundle.putString("jigesuccess", "jigesuccess");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 检查是否是欠费平台的直播
     *
     * @param matchInfo
     * @return
     */
    private boolean checkArrearage(MatchInfo matchInfo) {
        if (matchInfo != null && matchInfo.getRuid() != 0) {
            SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("提示");
            if (matchInfo.getRuid() == CpigeonData.getInstance().getUserId(getActivity())) {
                dialog.setContentText("您的直播平台已欠费\n请前往中鸽网充值缴费.");
                dialog.setCancelText("关闭");
                dialog.setConfirmText("知道了");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
            } else {
                dialog.setConfirmText("关闭");
                dialog.setContentText("该直播平台已欠费.");
            }
            dialog.setCancelable(false);
            dialog.show();
            return true;
        }
        return false;
    }
}
