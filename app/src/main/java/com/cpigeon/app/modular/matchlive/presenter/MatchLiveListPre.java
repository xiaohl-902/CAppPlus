package com.cpigeon.app.modular.matchlive.presenter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.matchlive.model.MatchModel;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/6/12.
 */

public class MatchLiveListPre extends BasePresenter {

    /**
     * 直播时间天数,公棚
     */
    public static final int LIVE_DAYS_LOFT = 3;
    /**
     * 直播时间天数,协会
     */
    public static final int LIVE_DAYS_ASSOCIATION = 3;

    public String type;




    public MatchLiveListPre(BaseFragment fragment) {
        super(fragment);
        type = fragment.getArguments().getString(IntentBuilder.KEY_TYPE);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getMatchList(Consumer<List<MatchInfo>> consumer){
        submitRequestThrowError(MatchModel.getMatchList(getLiveTypeCode(), getLiveDays()).map(r -> {
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
        }),consumer);
    }

    private int getLiveDays(){
        if(getString(R.string.string_loft).equals(type)){
            return LIVE_DAYS_LOFT;
        }else {
            return LIVE_DAYS_ASSOCIATION;
        }
    }

    private CallAPI.DATATYPE.MATCH getLiveTypeCode(){
        if(getString(R.string.string_loft).equals(type)){
            return CallAPI.DATATYPE.MATCH.GP;
        }else {
            return CallAPI.DATATYPE.MATCH.XH;
        }
    }

    /**
     * 检查是否是欠费平台的直播
     *
     * @param matchInfo
     * @return
     */
    public boolean checkArrearage(MatchInfo matchInfo) {
        if (matchInfo != null && matchInfo.getRuid() != 0) {
            SweetAlertDialog dialog = new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.WARNING_TYPE);
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
