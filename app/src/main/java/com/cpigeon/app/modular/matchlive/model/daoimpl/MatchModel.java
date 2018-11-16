package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.GeCheJianKongOrgInfo;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/12/11.
 */

public class MatchModel {
    public static Observable<ApiResponse<List<MatchReportXH>>> greatReportXH(int userId, String matchType, String ssid, String sKey,int page/*,int pager, int pageSize*/) {
        return PigeonHttpUtil.<ApiResponse<List<MatchReportXH>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MatchReportXH>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_match_race_report)
                .addQueryString("u", String.valueOf(userId))
                .addBody("t", String.valueOf("gp".equals(matchType) ? 1 : 2))
                .addBody("bi", ssid)
                .addBody("s", sKey)
                .addBody("hcz", String.valueOf(true))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(100))
                .setCache()
                .request();
    }

    public static Observable<ApiResponse<List<MatchReportGP>>> greatReportGP(int userId, String matchType, String ssid, String sKey, boolean hcz, int page/*,int pager, int pageSize*/) {
        return PigeonHttpUtil.<ApiResponse<List<MatchReportGP>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MatchReportGP>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_match_race_report)
                .addQueryString("u", String.valueOf(userId))
                .addBody("t", String.valueOf("gp".equals(matchType) ? 1 : 2))
                .addBody("bi", ssid)
                .addBody("s", sKey)
                .addBody("hcz", String.valueOf(hcz))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(100))
                .setCache()
                .request();
    }


    public static Observable<ApiResponse<List<MatchPigeonsXH>>> getJGMessageXH(int userId, String matchType, String ssid, String sKey, int page/*,int pager, int pageSize*/) {
        return PigeonHttpUtil.<ApiResponse<List<MatchPigeonsXH>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MatchPigeonsXH>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_JG_message)
                .addQueryString("u", String.valueOf(userId))
                .addBody("t", String.valueOf("gp".equals(matchType) ? 1 : 2))
                .addBody("bi", ssid)
                .addBody("s", sKey)
                .addBody("hcz", String.valueOf(1))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(100))
                .setCache()
                .request();
    }

    public static Observable<ApiResponse<List<MatchPigeonsGP>>> getJGMessageGP(int userId, String matchType, String ssid, String sKey, int page/*,int pager, int pageSize*/) {
        return PigeonHttpUtil.<ApiResponse<List<MatchPigeonsGP>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MatchPigeonsGP>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_JG_message)
                .addQueryString("u", String.valueOf(userId))
                .addBody("t", String.valueOf("gp".equals(matchType) ? 1 : 2))
                .addBody("bi", ssid)
                .addBody("s", sKey)
                .addBody("hcz", String.valueOf(1))
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(100))
                .setCache()
                .request();
    }

    public static Observable<ApiResponse<List<GeCheJianKongOrgInfo>>> getApiPigeonList( String keyWord, String type, int page ,int count) {
        return PigeonHttpUtil.<ApiResponse<List<GeCheJianKongOrgInfo>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<GeCheJianKongOrgInfo>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_pigeon_race_list)
                .addBody("key", keyWord)
                .addBody("pi", String.valueOf(page))
                .addBody("ps", String.valueOf(count))
                .addBody("t", type)
                .request();
    }

}
