package com.cpigeon.app.modular.matchlive.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.matchlive.model.MatchModel;
import com.cpigeon.app.modular.matchlive.model.bean.HistoryGradeInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.RPImages;
import com.cpigeon.app.modular.matchlive.model.bean.TrainingDataEntity;
import com.cpigeon.app.modular.matchlive.view.fragment.TrainingDataNewActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/4/19.
 */

public class TrainingDataPre extends BasePresenter {

    int userId;
    public String foodId;
    String matchId;
    public MatchInfo matchInfo;

    public String uploadImage;

    public TrainingDataEntity trainingDataEntity;
    public List<RPImages> images;
    public List<HistoryGradeInfo> lineData;
    public List<HistoryGradeInfo> reverseLineData;

    public String firstSpeed;
    public String speed;
    public String rank;

    public String searchNumber;
    public boolean isCache;
    public String pigeonMaster;

    public TrainingDataPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        matchInfo = (MatchInfo) activity.getIntent().getSerializableExtra(TrainingDataNewActivity.MATCH_INFO);
        foodId = activity.getIntent().getStringExtra(TrainingDataNewActivity.FOOD_ID);
        matchId = matchInfo.getSsid();

        firstSpeed = activity.getIntent().getStringExtra(TrainingDataNewActivity.FIRST_SPEED);
        speed = activity.getIntent().getStringExtra(TrainingDataNewActivity.SPEED);
        rank = activity.getIntent().getStringExtra(TrainingDataNewActivity.RANK);
        pigeonMaster = activity.getIntent().getStringExtra(TrainingDataNewActivity.PIGEON_MASTER);

    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

//    public void getTrainingData(Consumer<String> consumer){
//        submitRequestThrowError(MatchModel.getTrainingData(userId, foodId, matchId).map(r -> {
//            if(r.isOk()){
//                getData(r.data);
//                isCache = r.isCache;
//                return r.msg;
//            }else throw new HttpErrorException(r);
//        }),consumer);
////    }


    public void getTrainingData(Consumer<ApiResponse<TrainingDataEntity>> consumer) {
        submitRequestThrowError(MatchModel.getTrainingData(userId, foodId, matchId), consumer);
    }

    public void getData(TrainingDataEntity trainingDataEntity) {

        if (StringValid.isStringValid(rank) && StringValid.isStringValid(firstSpeed) && StringValid.isStringValid(speed) &&
                Float.valueOf(speed) != 0) {
            HistoryGradeInfo bean = new HistoryGradeInfo();
            bean.setBsgm(String.valueOf(matchInfo.getCsys()));
            bean.setBskj(String.valueOf(matchInfo.getBskj()));
            bean.setBssj(matchInfo.getSt());
            bean.setXmmc(matchInfo.getBsmc());
            bean.setFoot(foodId);
            bean.setBsmc(rank);
            bean.setFirstSpeed(firstSpeed);
            bean.setSpeed(speed);
            trainingDataEntity.getRacelist().add(trainingDataEntity.getRacelist().size() > 0 ? trainingDataEntity.getRacelist().size() : 0, bean);
        }

        images = trainingDataEntity.getPiclist();
        lineData = trainingDataEntity.getRacelist();
        reverseLineData = Lists.newArrayList();
        reverseLineData.addAll(lineData);
        Collections.reverse(reverseLineData);
        searchNumber = trainingDataEntity.getTcsyts();
    }

    public List<String> getImageUrl() {
        List<String> data = Lists.newArrayList();
        for (RPImages piclistBean : images) {
            data.add(piclistBean.getImgurl());
        }
        return data;
    }

    public void uploadShareImage(Consumer<String> consumer) {
        submitRequestThrowError(MatchModel.uploadShareImage(userId, uploadImage).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public float getMaxFirstSpeed() {
        return Float.valueOf(Collections.max(lineData, new TrainingDataEntity.fristSpeedComparator()).getFirstSpeed()) + 50f;
    }

    public int getMaxRank() {
        return Integer.valueOf(Collections.max(lineData, new TrainingDataEntity.rankComparator()).getBsmc()) + 50;
    }
}
