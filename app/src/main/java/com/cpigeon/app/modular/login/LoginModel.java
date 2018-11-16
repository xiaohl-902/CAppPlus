package com.cpigeon.app.modular.login;

import com.cpigeon.app.BuildConfig;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/6/1.
 */

public class LoginModel {
    public static Observable<ApiResponse<String>> login(String userName, String password) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_login)
                .addBody("u",userName)
                .addBody("p", EncryptionTool.encryptAES(password))
                .addQueryString("t","1")
                .addQueryString("devid", CommonTool.getCombinedDeviceID(MyApp.getInstance()))
                .addQueryString("dev", android.os.Build.MODEL)
                .addQueryString("ver", String.valueOf(CommonTool.getVersionCode(MyApp.getInstance())))
                .addQueryString("appid", BuildConfig.APPLICATION_ID)
                .request().map(r -> {
                    if(r.status){
                        JSONObject data = new JSONObject(r.data);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("username", data.getString("yonghuming"));
                        map.put("token", data.getString("token"));
                        map.put("touxiang", data.getString("touxiang"));
                        map.put("touxiangurl", data.getString("touxiangurl"));
                        map.put("nicheng", data.getString("nicheng"));
                        map.put("logined", true);
                        map.put("userid", Integer.valueOf(EncryptionTool.decryptAES(data.getString("token")).split("\\|")[0]));
                        map.put("sltoken", data.getString("sltoken"));
                        map.put("password", EncryptionTool.MD516(password));
                        CpigeonData.getInstance().setUserId((int) map.get("userid"));
                        SharedPreferencesTool.Save(MyApp.getInstance(), map, SharedPreferencesTool.SP_FILE_LOGIN);
                    }else {
                        SharedPreferencesTool.Save(MyApp.getInstance(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
                    }
                    return r;
                });
    }



    public static Observable<ApiResponse<String>> getCode(String phone, int type) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {}.getType())
                .setType(HttpUtil.TYPE_GET)
                .url(R.string.api_get_code)
                .addQueryString("t", phone)
                .addQueryString("p", String.valueOf(type))//1：注册；2：找回密码；
                .addQueryString("v", EncryptionTool.encryptAES("appcpigeon|" + System.currentTimeMillis() / 1000))
                .request();
    }

    public static Observable<ApiResponse<String>> findPassword(String phone, String password, String code) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_find_password)
                .addBody("t", phone)
                .addBody("p", EncryptionTool.encryptAES(password))
                .addBody("y", code)
                .request();
    }

    public static Observable<ApiResponse<String>> signUp(String phone, String password, String code) {
        return PigeonHttpUtil.<ApiResponse<String>>build()
                .setToJsonType(new TypeToken<ApiResponse<String>>() {}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_sign_up)
                .addBody("t", phone)
                .addBody("p", EncryptionTool.encryptAES(password))
                .addBody("y", code)
                .request();
    }
}
