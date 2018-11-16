package com.cpigeon.app.message.ui.order.ui.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.message.ui.order.ui.OrderModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class SetVoiceInfoPre extends BasePresenter {

    int userId;
    String voiceId;
    String unitName;
    String TFN;
    public String voiceType;
    String name;
    String phoneNumber;
    String address;
    String email;

    public String province;
    public String city;
    public String county;

    public VoiceEntity voiceEntity;

    public SetVoiceInfoPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        voiceEntity = activity.getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        if (voiceEntity != null) {
            voiceId = voiceEntity.id;
        }
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void setVoiceInfo(Consumer<String> consumer) {
        submitRequestThrowError(OrderModel.setVoiceInfo(userId, voiceId, unitName, TFN, voiceType, name
                , phoneNumber, province, city, county, address, email).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void deleteVoice(Consumer<String> consumer) {
        submitRequestThrowError(OrderModel.deleteVoice(userId, voiceId).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }


    public Consumer<String> setUnitName() {
        return s -> {
            unitName = s;
        };
    }

    public Consumer<String> setTFN() {
        return s -> {
            TFN = s;
        };
    }

    public Consumer<String> setName() {
        return s -> {
            name = s;
        };
    }

    public Consumer<String> setEmail() {
        return s -> {
            email = s;
        };
    }

    public Consumer<String> setAddress() {
        return s -> {
            address = s;
        };
    }

    public Consumer<String> setPhoneNumber() {
        return s -> {
            phoneNumber = s;
        };
    }

}
