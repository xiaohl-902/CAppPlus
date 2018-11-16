package com.cpigeon.app.modular.lineweather.view.viewdeo;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.lineweather.model.bean.GetGongPengListEntity;
import com.cpigeon.app.modular.lineweather.model.bean.GetSiFangDiEntity;
import com.cpigeon.app.modular.lineweather.model.bean.UllageToolEntity;
import com.cpigeon.app.modular.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CHAPIHttpUtil;
import com.cpigeon.app.utils.http.CPAPIHttpUtil;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ILineWeatherView implements IBaseDao {


    //获取公棚坐标信息
    public static Observable<ApiResponse<List<GetGongPengListEntity>>> getTool_GetGongPengInfoData(String str) {
        return CPAPIHttpUtil.<ApiResponse<List<GetGongPengListEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<GetGongPengListEntity>>>() {
                }.getType())
                .url(R.string.api_tool_get_gong_peng_info)
                .setType(HttpUtil.TYPE_POST)
                .addBody("s", str)
                .request();
    }


    //获取公棚坐标信息
    public static Observable<ApiResponse<List<GetSiFangDiEntity>>> getTool_GetSiFangDiData(String str) {
        return CPAPIHttpUtil.<ApiResponse<List<GetSiFangDiEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<GetSiFangDiEntity>>>() {
                }.getType())
                .url(R.string.api_tool_get_sfd_info)
                .setType(HttpUtil.TYPE_POST)
                .addBody("s", str)
                .request();
    }

    //获取公棚坐标信息
    public static Observable<ApiResponse<UllageToolEntity>> getKongju(Map<String, String> body) {
        return CHAPIHttpUtil.<ApiResponse<UllageToolEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UllageToolEntity>>() {
                }.getType())
                .url(R.string.api_get_kongju)
                .setType(HttpUtil.TYPE_POST)
                .addList(body)
                .request();
    }


    //设置用户头像和鸽舍名称
    public static Observable<ApiResponse<Object>> setShoot(String sszz, String imgfile) {
        return CPAPIHttpUtil.<ApiResponse<Object>>build()
                .setToJsonType(new TypeToken<ApiResponse<Object>>() {
                }.getType())
                .url(R.string.api_set_shoot)
                .setType(HttpUtil.TYPE_POST)
                .addBody("sszz", sszz)
                .addFileBody("imgfile", imgfile)
                .request();
    }

    //获取用户头像和鸽舍名称
    public static Observable<ApiResponse<ShootInfoEntity>> getShoot() {
        return CPAPIHttpUtil.<ApiResponse<ShootInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<ShootInfoEntity>>() {
                }.getType())
                .url(R.string.api_get_shoot)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }

}
