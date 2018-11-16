package com.cpigeon.app.message.ui.order.ui.vocice;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.event.VoiceListUpdateEvent;
import com.cpigeon.app.message.ui.order.ui.presenter.SetVoiceInfoPre;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.view.LineInputView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class SetVoiceInfoFragment extends BaseVoiceInfoFragment {

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);

        setTitle("发票设置");

        spinnerVoiceType.setSelection(0);


        btnSure.setOnClickListener(v -> {
            showLoading();
            mPresenter.setVoiceInfo(s -> {
                ToastUtil.showShortToast(getActivity(), s);
                hideLoading();
                finish();
                EventBus.getDefault().post(new VoiceListUpdateEvent());
            });
        });

    }

}
