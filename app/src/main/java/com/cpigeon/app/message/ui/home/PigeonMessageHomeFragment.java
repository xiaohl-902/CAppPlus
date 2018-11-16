package com.cpigeon.app.message.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.event.GXTUserInfoEvent;
import com.cpigeon.app.message.adapter.PigeonMessageHomeAdapter;
import com.cpigeon.app.base.BaseWebViewActivity;
import com.cpigeon.app.message.ui.order.ui.CreateMessageOrderFragment;
import com.cpigeon.app.message.ui.order.ui.OrderPayFragment;
import com.cpigeon.app.message.ui.userAgreement.UserAgreementActivity;
import com.cpigeon.app.message.ui.common.CommonMessageFragment;
import com.cpigeon.app.message.ui.contacts.TelephoneBookFragment;
import com.cpigeon.app.message.ui.history.MessageHistoryFragment;
import com.cpigeon.app.message.ui.modifysign.ModifySignFragment;
import com.cpigeon.app.message.ui.person.PersonInfoFragment;
import com.cpigeon.app.message.ui.sendmessage.SendMessageFragment;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Zhu TingYu on 2017/11/17.
 */

public class PigeonMessageHomeFragment extends BaseMVPFragment<PigeonHomePre> {

    private static final int CODE_AGREEMENT = 0x123;
    private static final int CODE_SEND_MESSAGE = 0x234;



    RecyclerView recyclerView;

    PigeonMessageHomeAdapter adapter;

    private List<String> titleList;
    UserGXTEntity userGXTEntity;
    SweetAlertDialog dialogAgreement;

    @Override
    public PigeonHomePre initPresenter() {
        return new PigeonHomePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }


    private void initView() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("充值短信")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), CreateMessageOrderFragment.class);
                    return true;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new PigeonMessageHomeAdapter(getActivity(), titleList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (0 == position) {
                //发送消息
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_DATA, mPresenter.userGXTEntity)
                        .startParentActivity(getActivity(), SendMessageFragment.class, CODE_SEND_MESSAGE);
            } else if (1 == position) {
                //电话簿
                IntentBuilder.Builder().startParentActivity(getActivity(), TelephoneBookFragment.class);
            } else if (2 == position) {
                //常用短语
                IntentBuilder.Builder().startParentActivity(getActivity(), CommonMessageFragment.class);
            } else if (3 == position) {
                //发送记录
                IntentBuilder.Builder().startParentActivity(getActivity(), MessageHistoryFragment.class);
            } else if (4 == position) {
                //修改签名
                IntentBuilder.Builder().startParentActivity(getActivity(), ModifySignFragment.class);
            } else if (5 == position) {
                //使用帮助
                IntentBuilder.Builder(getSupportActivity(), BaseWebViewActivity.class)
                        .putExtra(IntentBuilder.KEY_TITLE, "使用帮助")
                        .putExtra(IntentBuilder.KEY_DATA, CPigeonApiUrl.getInstance().getServer() + getString(R.string.api_user_help))
                        .startActivity();
            } else if (6 == position) {
                //个人信息
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_LOOK)
                        .startParentActivity(getActivity(), PersonInfoFragment.class);
            } else if (7 == position) {
                //用户协议
                UserAgreementActivity.startActivity(getSupportActivity(), true);
            }
        });

    }

    @Override
    public void finishCreateView(Bundle state) {
        titleList = Lists.newArrayList("发送短信", "电话薄", "短语库", "发送记录"
                , "修改签名", "使用帮助", "个人信息", "用户协议");
        userGXTEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);


        setTitle("鸽信通");

        EventBus.getDefault().register(this);

        mPresenter.userId = CpigeonData.getInstance().getUserId(getContext());

        getUserData();

        /*if (userGXTEntity.tyxy == 0) { //为0是未同意协议
            DialogUtils.createDialogWithLeft(getActivity(), "你已经开通鸽信通，阅读并同意后即可使用", sweetAlertDialog -> {
                UserAgreementActivity.startActivity(getActivity(), false, CODE_AGREEMENT);
                sweetAlertDialog.dismiss();
            });
        } else {
            if (userGXTEntity.syts < 1000) {
                showTips(getString(R.string.message_pigeon_message_count_not_enough), TipType.Dialog);
            }
            initView();
        }*/

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pigeon_message_home_layout;
    }

    public void getUserData() {
        showLoading();
        mPresenter.getUserInfo(r -> {
            hideLoading();
            if (r.status) {
                userGXTEntity = r.data;
                if (userGXTEntity.tyxy == 0) { //为0是未同意协议
                    DialogUtils.createDialogWithLeft(getActivity(), "你已经开通鸽信通，阅读并同意后即可使用"
                            , sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                            , sweetAlertDialog -> {
                                dialogAgreement = sweetAlertDialog;
                                UserAgreementActivity.startActivity(getActivity(), false, CODE_AGREEMENT);
                            });
                } else {
                    if (userGXTEntity.syts < 1000) {
                        DialogUtils.createDialog(getContext(), getString(R.string.message_pigeon_message_count_not_enough), sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            IntentBuilder.Builder().startParentActivity(getActivity(), CreateMessageOrderFragment.class);
                        });
                    }
                    initView();
                }
            } else {
                if (r.errorCode == PigeonHomePre.STATE_NO_OPEN) {
                    DialogUtils.createDialogWithLeft(getContext()
                            , getString(R.string.message_not_open_pigeon_message)
                            , sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                            , sweetAlertDialog -> {
                                IntentBuilder.Builder()
                                        .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                                        .startParentActivity(getActivity(), PersonInfoFragment.class);
                                sweetAlertDialog.dismiss();
                                finish();
                            });

                } else {
                    if (r.errorCode == PigeonHomePre.STATE_ID_CARD_NOT_NORMAL ||
                            r.errorCode == PigeonHomePre.STATE_PERSON_INFO_NOT_NORMAL) {

                        DialogUtils.createDialogWithLeft(getActivity(), r.msg
                                , sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                                , sweetAlertDialog -> {
                                    IntentBuilder.Builder()
                                            .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_UPLOAD_INFO)
                                            .putExtra(PersonInfoFragment.TYPE_UPLOAD_INFO_HAVE_DATE, true)
                                            .putExtra(IntentBuilder.KEY_DATA, r.msg)
                                            .startParentActivity(getActivity(), PersonInfoFragment.class);
                                    sweetAlertDialog.dismiss();
                                    finish();
                                });

                    } else if (r.errorCode == PigeonHomePre.STATE_NOT_PAY) {

                        DialogUtils.createDialogWithLeft(getContext(), r.msg
                                , sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                                , sweetAlertDialog -> {
                                    showLoading("正在创建订单");
                                    mPresenter.getGXTOrder(order -> {
                                        hideLoading();
                                        if (order.status) {
                                            IntentBuilder.Builder()
                                                    .putExtra(IntentBuilder.KEY_DATA, order.data)
                                                    .startParentActivity(getActivity(), OrderPayFragment.class);
                                            sweetAlertDialog.dismiss();
                                            finish();
                                        } else {
                                            error(order.msg);
                                        }

                                    });
                                });


                    } else {
                        DialogUtils.createDialog(getContext(), r.msg, sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            finish();
                        });
                    }

                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GXTUserInfoEvent event) {
        getUserData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_AGREEMENT) {
            if (data != null) {
                initView();
                dialogAgreement.dismiss();
            }
        } else if (requestCode == CODE_SEND_MESSAGE) {
            if (data != null) {
                getUserData();
            }
        }
    }
}
