package com.cpigeon.app.message.ui.order.ui.vocice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.message.ui.order.ui.presenter.SetVoiceInfoPre;
import com.cpigeon.app.utils.AddressPickTask;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.view.LineInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class BaseVoiceInfoFragment extends BaseMVPFragment<SetVoiceInfoPre> {
    @BindView(R.id.lineUnitsName)
    LineInputView lineUnitsName;
    @BindView(R.id.lineTFN)
    LineInputView lineTFN;
    @BindView(R.id.spinnerVoiceType)
    Spinner spinnerVoiceType;
    @BindView(R.id.lineReceiverName)
    LineInputView lineReceiverName;
    @BindView(R.id.lineReceiverPhone)
    LineInputView lineReceiverPhone;
    @BindView(R.id.llPaperInvoice)
    LinearLayout llPaperInvoice;
    @BindView(R.id.lineEmail)
    LineInputView lineEmail;
    @BindView(R.id.llElectronicInvoice)
    LinearLayout llElectronicInvoice;
    @BindView(R.id.btnSure)
    AppCompatButton btnSure;
    @BindView(R.id.lineReceiverArea)
    LineInputView lineReceiverArea;
    @BindView(R.id.lineDetailsAddress)
    LineInputView lineDetailsAddress;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_set_invoice_info_layout;
    }

    @Override
    protected SetVoiceInfoPre initPresenter() {
        return new SetVoiceInfoPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item
                , Lists.newArrayList("电子发票", "纸质发票"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVoiceType.setAdapter(adapter);
        spinnerVoiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    llPaperInvoice.setVisibility(View.VISIBLE);
                    llElectronicInvoice.setVisibility(View.GONE);
                    mPresenter.voiceType = "纸质发票";

                } else {
                    llPaperInvoice.setVisibility(View.GONE);
                    llElectronicInvoice.setVisibility(View.VISIBLE);
                    mPresenter.voiceType = "电子发票";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bindUi(RxUtils.textChanges(lineUnitsName.getContent()), mPresenter.setUnitName());
        bindUi(RxUtils.textChanges(lineTFN.getContent()), mPresenter.setTFN());
        bindUi(RxUtils.textChanges(lineReceiverName.getContent()), mPresenter.setName());
        bindUi(RxUtils.textChanges(lineDetailsAddress.getContent()), mPresenter.setAddress());
        bindUi(RxUtils.textChanges(lineEmail.getContent()), mPresenter.setEmail());
        bindUi(RxUtils.textChanges(lineReceiverPhone.getContent()), mPresenter.setPhoneNumber());

        lineReceiverArea.getContent().setFocusableInTouchMode(false);

        lineReceiverArea.getContent().setOnClickListener(v -> {
            onAddress3Picker(getActivity(), new AddressPickTask.Callback() {
                @Override
                public void onAddressInitFailed() {

                }

                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    lineReceiverArea.setContent(province.getName() + city.getName() + county.getName());
                    mPresenter.province = province.getName();
                    mPresenter.city = province.getName();
                    mPresenter.county = county.getName();
                }
            });
        });

    }

    protected void onAddress3Picker(Activity activity, AddressPickTask.Callback callback) {
        AddressPickTask task = new AddressPickTask(activity);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(callback);
        task.execute("四川", "成都", "高新区");
    }

}
