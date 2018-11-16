package com.cpigeon.app.modular.lineweather.presenter;


import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.lineweather.model.bean.GetGongPengListEntity;
import com.cpigeon.app.modular.lineweather.model.bean.GetSiFangDiEntity;
import com.cpigeon.app.modular.lineweather.model.bean.UllageToolEntity;
import com.cpigeon.app.modular.lineweather.view.viewdeo.ILineWeatherView;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/7.
 */

public class LineWeatherPresenter extends BasePresenter {

    public LineWeatherPresenter(IView mView) {
        super(mView);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取公棚坐标信息
    public void getTool_GetGongPengInfo(String str, Consumer<List<GetGongPengListEntity>> consumer) {
        submitRequestThrowError(ILineWeatherView.getTool_GetGongPengInfoData(str).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }


    //获取司放地信息
    public void getTool_GetSiFangDi(String str, Consumer<List<GetSiFangDiEntity>> consumer) {
        submitRequestThrowError(ILineWeatherView.getTool_GetSiFangDiData(str).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

    //获取司放地信息
    public void getKongJuData(Map<String, String> body, Consumer<UllageToolEntity> consumer) {
        submitRequestThrowError(ILineWeatherView.getKongju(body).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return null;
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }

}
