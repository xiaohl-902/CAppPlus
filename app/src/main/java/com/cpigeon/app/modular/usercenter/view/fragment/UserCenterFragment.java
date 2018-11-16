package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.event.GXTUserInfoEvent;
import com.cpigeon.app.message.ui.home.PigeonMessageHomeFragment;
import com.cpigeon.app.modular.guide.view.SignActivity;
import com.cpigeon.app.modular.order.view.activity.OrderActivity;
import com.cpigeon.app.modular.settings.view.activity.SettingsActivity;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserCenterPre;
import com.cpigeon.app.modular.usercenter.view.activity.AboutActivity;
import com.cpigeon.app.modular.usercenter.view.activity.BalanceActivity;
import com.cpigeon.app.modular.usercenter.view.activity.HelpActivity;
import com.cpigeon.app.modular.usercenter.view.activity.MessageActivity;
import com.cpigeon.app.modular.usercenter.view.activity.MyFollowActivity;
import com.cpigeon.app.modular.usercenter.view.activity.ScoreActivity;
import com.cpigeon.app.modular.usercenter.view.activity.UserInfoActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2017/4/6.
 */

public class UserCenterFragment extends BaseMVPFragment<UserCenterPre> {

    @BindView(R.id.fragment_user_center_userLogo)
    CircleImageView fragmentUserCenterUserLogo;
    @BindView(R.id.fragment_user_center_userName)
    TextView fragmentUserCenterUserName;
    @BindView(R.id.fragment_user_center_details)
    TextView fragmentUserCenterDetails;
    @BindView(R.id.tv_sign_status)
    TextView tvSignStatus;
    @BindView(R.id.cv_sign)
    CardView cvSign;
    @BindView(R.id.fragment_user_center_userInfo_layout)
    RelativeLayout fragmentUserCenterUserInfoLayout;
    @BindView(R.id.ll_user_center_msg)
    LinearLayout llUserCenterMsg;
    @BindView(R.id.marqueeTextView)
    MarqueeTextView marqueeTextView;
    @BindView(R.id.ll_user_center_message)
    LinearLayout llUserCenterMessage;
    @BindView(R.id.ll_user_center_focus)
    LinearLayout llUserCenterFocus;
    @BindView(R.id.ll_user_center_order)
    LinearLayout llUserCenterOrder;
    @BindView(R.id.tv_user_money)
    TextView tvUserMoney;
    @BindView(R.id.ll_user_money)
    LinearLayout llUserMoney;
    @BindView(R.id.tv_user_jifen)
    TextView tvUserJifen;
    @BindView(R.id.ll_user_jifen)
    LinearLayout llUserJifen;
    @BindView(R.id.ll_user_center_setting)
    LinearLayout llUserCenterSetting;
    @BindView(R.id.ll_user_center_aboutus)
    LinearLayout llUserCenterAboutus;
    @BindView(R.id.ll_user_center_help)
    LinearLayout llUserCenterHelp;
    long lastUpdateViewTime = -1;

    UserGXTEntity userGXTEntity;

    private CpigeonData.OnDataChangedListener onDataChangedListenerWeakReference = new CpigeonData.OnDataChangedListener() {
        @Override
        public void OnDataChanged(CpigeonData cpigeonData) {
            Logger.e("用户数据更新回调");
            refreshUserInfo(true);
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_usercenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CpigeonData.getInstance().addOnDataChangedListener(onDataChangedListenerWeakReference);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void loadData() {
        CpigeonData.DataHelper.getInstance().updateUserBalanceAndScoreFromServer();
        CpigeonData.DataHelper.getInstance().updateUserSignStatus();
        CpigeonData.DataHelper.getInstance().updateUserInfo(null);
    }

    @Override
    public void finishCreateView(Bundle state) {

        EventBus.getDefault().register(this);
        refreshUserInfo(false);
        loadData();

        fragmentUserCenterUserLogo.setOnClickListener(v -> {
            if (!isNetworkConnected()) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("无法连接网络")
                        .setConfirmText("知道了")
                        .show();
                return;
            }
            if (checkLogin()) {
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
            }
        });
    }

    @OnClick({R.id.fragment_user_center_details, R.id.cv_sign, R.id.ll_user_center_msg, R.id.ll_user_center_focus, R.id.ll_user_center_order, R.id.ll_user_money, R.id.ll_user_jifen, R.id.ll_user_center_setting, R.id.ll_user_center_aboutus, R.id.ll_user_center_help, R.id.ll_user_center_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_user_center_details:
                if (!isNetworkConnected()) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("无法连接网络")
                            .setConfirmText("知道了")
                            .show();
                    return;
                }
                if (checkLogin()) {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
                break;
            case R.id.cv_sign:
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "我的");
//                intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_SIGN_URL + "?uid=" + CpigeonData.getInstance().getUserId(getActivity()));
//                startActivity(intent);

//                IntentBuilder.Builder().startParentActivity(getActivity(), SignFragment.class);

                //签到
                Intent intent = new Intent(getActivity(), SignActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_user_center_msg:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.ll_user_center_message:
                //IntentBuilder.Builder(getActivity(), PigeonMessageHomeFragment.class).startActivity();
                //getUserData();
                IntentBuilder.Builder().startParentActivity(getActivity(), PigeonMessageHomeFragment.class);
                // IntentBuilder.Builder().startParentActivity(getActivity(), SignFragment.class);
                break;
            case R.id.ll_user_center_focus:
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
            case R.id.ll_user_center_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.ll_user_money:
                startActivity(new Intent(getActivity(), BalanceActivity.class));
                break;
            case R.id.ll_user_jifen:
                startActivity(new Intent(getActivity(), ScoreActivity.class));
                break;
            case R.id.ll_user_center_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.ll_user_center_aboutus:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.ll_user_center_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
        }
    }
   /* public void getUserData(){
        showLoading();
        mPresenter.getUserInfo(r -> {
            hideLoading();
            if (r.status) {
                userGXTEntity = r.data;
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_DATA, r.data)
                        .startParentActivity(getActivity(),PigeonMessageHomeFragment.class);
            } else {
                if (r.errorCode == PigeonHomePre.STATE_NO_OPEN) {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                            .startParentActivity(getActivity(), PersonInfoFragment.class);

                }else if(r.errorCode == PigeonHomePre.STATE_ID_CARD_NOT_NORMAL ||
                        r.errorCode == PigeonHomePre.STATE_PERSON_INFO_NOT_NORMAL){
                    DialogUtils.createDialog(getActivity(), r.msg, sweetAlertDialog -> {
                        IntentBuilder.Builder()
                                .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                                .putExtra(IntentBuilder.KEY_DATA, r.msg)
                                .putExtra(PersonInfoFragment.TYPE_UPLOAD_INFO_HAVE_DATE, true)
                                .startParentActivity(getActivity(), PersonInfoFragment.class);

                    });
                }else {
                    DialogUtils.createHintDialog(getContext(), r.msg);
                }

            }
        });
    }*/


    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.LoadingHide || tipType == TipType.LoadingShow)
            return true;
        return super.showTips(tip, tipType);
    }


    private void refreshUserInfo(boolean isRefersh) {
        if (checkLogin()) {
            UserInfo.DataBean userInfo = CpigeonData.getInstance().getUserInfo();
            String userHeadImageURl = "";
            String nickName = "";
            if (!isRefersh) {
                Map<String, Object> map = getLoginUserInfo();
                if (map.get("touxiang") != null && (!map.get("touxiang").equals("null") || !map.get("touxiang").equals(""))) {
                    userHeadImageURl = (String) map.get("touxiangurl");
                }
                Logger.d(String.format("%s  %s", map.get("nicheng"), map.get("username")));
                if (map.get("nicheng") != null && (!TextUtils.isEmpty(map.get("nicheng").toString()))) {
                    nickName = map.get("nicheng").toString();
                } else {
                    nickName = map.get("username").toString();
                }
            }
            if (userInfo != null) {
                if (!TextUtils.isEmpty(userInfo.getHeadimg())) {
                    userHeadImageURl = userInfo.getHeadimg();
                    //余额
                    Picasso.with(getActivity())
                            .load(userHeadImageURl)
                            .error(R.mipmap.head_image_default)
                            .into(fragmentUserCenterUserLogo);
                }

                nickName = TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getUsername() : userInfo.getNickname();
//              更新用户缓存数据
                if (userHeadImageURl != null)
                    SharedPreferencesTool.Save(getActivity(), "touxiangurl", userHeadImageURl, SharedPreferencesTool.SP_FILE_LOGIN);
                if (userInfo.getNickname() != null)
                    SharedPreferencesTool.Save(getActivity(), "nicheng", userInfo.getNickname(), SharedPreferencesTool.SP_FILE_LOGIN);
            }


            final String name = nickName;
            fragmentUserCenterUserName.setText(name);
        } else {
            fragmentUserCenterUserLogo.setImageResource(R.mipmap.head_image_default);
            fragmentUserCenterDetails.setText(getString(R.string.please_login));
        }
        if (fragmentUserCenterDetails != null) {
            fragmentUserCenterDetails.setVisibility(checkLogin() ? View.VISIBLE : View.GONE);
        }

        tvUserMoney.setText(String.format("%.2f", CpigeonData.getInstance().getUserBalance()));
        tvUserMoney.setTextColor(Color.RED);
        tvUserJifen.setText(String.format("%d", CpigeonData.getInstance().getUserScore()));
        tvUserJifen.setTextColor(Color.RED);

        tvSignStatus.setText(CpigeonData.getInstance().getUserSignStatus() == CpigeonData.USER_SIGN_STATUS_SIGNED ? "已签到" : "签到");
        lastUpdateViewTime = System.currentTimeMillis();
    }

    @Override
    protected UserCenterPre initPresenter() {
        return new UserCenterPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }


    @Override
    public void onDestroyView() {
        CpigeonData.getInstance().removeOnDataChangedListener(onDataChangedListenerWeakReference);
        super.onDestroyView();
        Logger.e("监听移除了");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GXTUserInfoEvent event) {
        loadData();
    }


}
