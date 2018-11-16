package com.cpigeon.app.modular.matchlive.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.model.daoimpl.MatchModel;
import com.cpigeon.app.modular.matchlive.view.fragment.BaseSearchResultFragment;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class SearchMatchPre extends BasePresenter {

    int userId;
    public MatchInfo matchInfo;
    String key;
    public int page = 1;

    public SearchMatchPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(getActivity());
        matchInfo = (MatchInfo) activity.getIntent().getSerializableExtra(IntentBuilder.KEY_DATA);
        key = activity.getIntent().getStringExtra(BaseSearchResultFragment.KEY_WORD);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getReportXH(Consumer<List<MatchReportXH>> consumer) {
        submitRequestThrowError(MatchModel.greatReportXH(userId, matchInfo.getLx(), matchInfo.getSsid(), key, page).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else return Lists.newArrayList();
            } else throw new HttpErrorException(r);
        }), consumer);
    }


    public void getReportGP(Consumer<List<MatchReportGP>> consumer) {
        submitRequestThrowError(MatchModel.greatReportGP(userId, matchInfo.getLx(), matchInfo.getSsid(), key, matchInfo.isMatch(), page).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else return Lists.newArrayList();
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void getJGMessageXH(Consumer<List<MatchPigeonsXH>> consumer) {
        submitRequestThrowError(MatchModel.getJGMessageXH(userId, matchInfo.getLx(), matchInfo.getSsid(), key, page).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else return Lists.newArrayList();
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void getJGMessageGP(Consumer<List<MatchPigeonsGP>> consumer) {
        submitRequestThrowError(MatchModel.getJGMessageGP(userId, matchInfo.getLx(), matchInfo.getSsid(), key, page).map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else return Lists.newArrayList();
            } else throw new HttpErrorException(r);
        }), consumer);
    }

}
