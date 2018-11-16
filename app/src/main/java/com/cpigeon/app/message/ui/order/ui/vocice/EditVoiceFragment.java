package com.cpigeon.app.message.ui.order.ui.vocice;

import android.os.Bundle;
import android.view.MenuItem;

import com.cpigeon.app.event.VoiceListUpdateEvent;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Zhu TingYu on 2018/6/14.
 */

public class EditVoiceFragment extends BaseVoiceInfoFragment {
    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        setTitle("发票编辑");
        setIsEdit();

        toolbar.getMenu().clear();
        toolbar.getMenu().add("删除").setOnMenuItemClickListener(item -> {
            DialogUtils.createDialogWithLeft(getActivity(), "是否删除该发票信息", sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
            }, sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                showLoading();
                mPresenter.deleteVoice(s -> {
                    hideLoading();
                    finish();
                    ToastUtil.showLongToast(getActivity(), "删除成功");
                    EventBus.getDefault().post(new VoiceListUpdateEvent());
                });
            });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        lineUnitsName.setContent(mPresenter.voiceEntity.dwmc);
        lineTFN.setContent(mPresenter.voiceEntity.sh);
        lineReceiverName.setContent(mPresenter.voiceEntity.lxr);
        lineDetailsAddress.setContent(mPresenter.voiceEntity.dz);
        lineEmail.setContent(mPresenter.voiceEntity.yx);
        lineReceiverPhone.setContent(mPresenter.voiceEntity.dh);
        lineReceiverArea.setContent(mPresenter.voiceEntity.p + mPresenter.voiceEntity.c + mPresenter.voiceEntity.a);

        if (mPresenter.voiceEntity.lx.equals("电子发票")) {
            spinnerVoiceType.setSelection(0);
        } else {
            spinnerVoiceType.setSelection(1);
        }

        btnSure.setOnClickListener(v -> {
            showLoading();
            mPresenter.setVoiceInfo(s -> {
                ToastUtil.showShortToast(getActivity(), s);
                hideLoading();
                EventBus.getDefault().post(new VoiceListUpdateEvent());
                finish();
            });
        });

    }

    private void setIsEdit() {
        lineUnitsName.getContent().clearFocus();
        lineTFN.getContent().clearFocus();
        lineReceiverName.getContent().clearFocus();
        lineDetailsAddress.getContent().clearFocus();
        lineEmail.getContent().clearFocus();
        lineReceiverPhone.getContent().clearFocus();
    }
}
