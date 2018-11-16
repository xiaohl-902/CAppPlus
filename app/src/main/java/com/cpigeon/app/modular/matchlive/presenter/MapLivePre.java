package com.cpigeon.app.modular.matchlive.presenter;

import android.app.Activity;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.MapLiveEntity;
import com.cpigeon.app.modular.matchlive.model.MapLiveModel;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongRace;
import com.cpigeon.app.modular.matchlive.view.activity.MapLiveActivity;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/3/5.
 */

public class MapLivePre extends BasePresenter {
    public GeCheJianKongRace geCheJianKongRace;
    public List<GYTRaceLocation> positions;
    public List<LatLng> latLngs = new ArrayList<LatLng>();
    public MapLiveEntity mapLiveEntity;
    public long time;


    public String rid = "";
    public String lid = "";

    public LatLng startLatLng;

    public MapLivePre(Activity activity) {
        super(activity);
        if (geCheJianKongRace == null){
            this.geCheJianKongRace = ((MapLiveActivity) getActivity()).getGeCheJianKongRace();
            rid = String.valueOf(geCheJianKongRace.getId());
        }
        positions = Lists.newArrayList();

    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getPosition(Consumer<Boolean> consumer){
        submitRequestThrowError(MapLiveModel.getPosition(rid, lid).map(r -> {
            if(r.status){
                mapLiveEntity = r.data;
                if(!mapLiveEntity.localinfolist.isEmpty()){
                    positions.clear();
                    positions.addAll(mapLiveEntity.localinfolist);
                    readLatLngs();
                    return true;
                }else {
                    return false;
                }
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    private void readLatLngs() {
        latLngs.clear();
        for (GYTRaceLocation gytRaceLocation : positions) {
            latLngs.add(new LatLng(gytRaceLocation.getWd(), gytRaceLocation.getJd()));
        }

        if(startLatLng == null && !latLngs.isEmpty()){
            startLatLng = latLngs.get(0);
        }
    }

    public GYTRaceLocation getLastPoint(){
        if(!positions.isEmpty()){
            return positions.get(positions.size() - 1);
        }else return null;
    }
}
