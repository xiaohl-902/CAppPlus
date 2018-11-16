package com.cpigeon.app.modular.matchlive.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.matchlive.model.MatchModel;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeEntity;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.HistoryGradeActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/4/17.
 */

public class HistoryGradePre extends BasePresenter {

    int userId;
    public String foodId;
    public String cardId;
    public String type;
    public MatchInfo matchInfo;
    public String lastRank;
    public String uploadImage;
    public List<HistoryGradeInfo> data;
    public List<HistoryGradeInfo> lineData = Lists.newArrayList();

    public String searchNumber;
    public Double speed;
    public String pigeonMaster;
    public boolean isCacahe;
    public double distance;

    public HistoryGradePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        foodId = activity.getIntent().getStringExtra(HistoryGradeActivity.FOOD_ID);
        cardId = activity.getIntent().getStringExtra(HistoryGradeActivity.CARD_ID);
        type = activity.getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        matchInfo = (MatchInfo) activity.getIntent().getSerializableExtra(IntentBuilder.KEY_DATA);
        lastRank = activity.getIntent().getStringExtra(HistoryGradeActivity.RANK);
        speed = activity.getIntent().getDoubleExtra(HistoryGradeActivity.SPEED, 0);
        pigeonMaster = activity.getIntent().getStringExtra(HistoryGradeActivity.PIGEON_MASTER);
        distance = activity.getIntent().getDoubleExtra(HistoryGradeActivity.DISTANCE, 0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getHistoryGrade(Consumer<ApiResponse<HistoryGradeEntity>> consumer) {
        submitRequestThrowError(MatchModel.getHistoryGrade(userId, foodId, cardId, type), consumer);
    }

    public void uploadShareImage(Consumer<String> consumer) {
        submitRequestThrowError(MatchModel.uploadShareImage(userId, uploadImage).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public float getMaxDistance() {
        return Float.valueOf(Collections.max(data, new HistoryGradeInfo.distanceComparator()).getBskj()) + 50f;
    }

    public int getMaxRank() {
        return Integer.valueOf(Collections.max(data, new HistoryGradeInfo.rankComparator()).getBsmc()) + 50;
    }

}
