package com.cpigeon.app.message.ui.modifysign;


import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.entity.PersonInfoEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.utils.http.HttpUtil;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.utils.http.RetrofitHelper;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class PersonSignModel {

    public static Observable<ApiResponse<PersonInfoEntity>> personSignInfo(int userId){
        return PigeonHttpUtil.<ApiResponse<PersonInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<PersonInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_sign_info)
                .addQueryString("u", String.valueOf(userId))
                .request();
    }

    public static Observable<ApiResponse<PersonInfoEntity>> personInfo(int userId){
        return PigeonHttpUtil.<ApiResponse<PersonInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<PersonInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_person_info)
                .addQueryString("u", String.valueOf(userId))
                .request();
    }


    public static Observable<ApiResponse> modifySign(int userId, String sign, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, String> map = new HashMap<>();
        map.put("qm", sign);
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);

        Map<String, RequestBody> files = new HashMap<>();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("qm", getString(sign))
                .addFormDataPart("xingming",getString(name) )
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu",getString(familyName) )
                .addFormDataPart("zhuzhi",getString(address) )
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan",getString(organization))
                .addFormDataPart("youxiaoqi", getString(idCardDate));

        if (imgIdCardP != null) {
            files.put("sfzzm", RequestBody.create(MediaType.parse("image/*"), imgIdCardP));
            builder.addPart(MultipartBody.Part.createFormData("sfzzm",imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            files.put("sfzbm", RequestBody.create(MediaType.parse("image/*"), imgIdCardN));
            builder.addPart(MultipartBody.Part.createFormData("sfzbm",imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            files.put("gswj", RequestBody.create(MediaType.parse("image/*"), imgLicense));
            builder.addPart(MultipartBody.Part.createFormData("gswj",imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        RequestBody requestBody = builder.build();


        return RetrofitHelper.getApi().modifySign(
                CommonTool.getUserToken(MyApp.getInstance().getBaseContext()),
                String.valueOf(userId),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                requestBody);

    }


    public static Observable<ApiResponse> modifyPersonInfo(int userId, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate, String personName, String personPhoneNumber, String personWork, String sign) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, String> map = new HashMap<>();
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);

        map.put("lianxiren",personName);
        map.put("sjhm",personPhoneNumber);
        map.put("dwmc",personWork);
        map.put("qianming",sign);


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("xingming",getString(name))
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu",getString(familyName))
                .addFormDataPart("zhuzhi",getString(address) )
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan",getString(organization) )
                .addFormDataPart("youxiaoqi", getString(idCardDate))
                .addFormDataPart("lianxiren", getString(personName))
                .addFormDataPart("sjhm", getString(personPhoneNumber))
                .addFormDataPart("qianming", getString(sign))
                .addFormDataPart("dwmc", getString(personWork));

        if (imgIdCardP != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzzm",imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzbm",imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            builder.addPart(MultipartBody.Part.createFormData("gswj",imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        LogUtil.print("{\n");
        for (String key : map.keySet()) {
            LogUtil.print(key + ": " + map.get(key)+"\n");
        }

        if(imgIdCardP != null){
            LogUtil.print("sfzzm" + ": " + imgIdCardP.getPath()+"\n");
        }

        if(imgIdCardN != null){
            LogUtil.print("sfzbm" + ": " + imgIdCardN.getPath()+"\n");
        }

        if(imgLicense != null){
            LogUtil.print("gswj" + ": " + imgLicense.getPath()+"\n");
        }

        LogUtil.print("}");

        LogUtil.print(CPigeonApiUrl.getInstance().getServer()
                + "/CPAPI/V1/"+"GXT_XGGRXX");

        RequestBody requestBody = builder.build();


        return RetrofitHelper.getApi().modifyPersonInfo(
                CommonTool.getUserToken(MyApp.getInstance().getBaseContext()),
                String.valueOf(userId),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                requestBody);

    }


    public static Observable<ApiResponse> uploadPersonInfo(int userId, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate, String personName, String personPhoneNumber, String personWork, String sign) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, String> map = new HashMap<>();
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);
        map.put("lianxiren",personName);
        map.put("sjhm",personPhoneNumber);
        map.put("dwmc",personWork);
        map.put("qianming",sign);


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("xingming",getString(name))
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu",getString(familyName))
                .addFormDataPart("zhuzhi",getString(address) )
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan",getString(organization) )
                .addFormDataPart("youxiaoqi", getString(idCardDate))
                .addFormDataPart("lianxiren", getString(personName))
                .addFormDataPart("sjhm", getString(personPhoneNumber))
                .addFormDataPart("dwmc", getString(personWork))
                .addFormDataPart("qianming", getString(sign));

        if (imgIdCardP != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzzm",imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzbm",imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            builder.addPart(MultipartBody.Part.createFormData("gswj",imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        LogUtil.print("{\n");
        for (String key : map.keySet()) {
            LogUtil.print(key + ": " + map.get(key)+"\n");
        }

        if(imgIdCardP != null){
            LogUtil.print("sfzzm" + ": " + imgIdCardP.getPath()+"\n");
        }

        if(imgIdCardN != null){
            LogUtil.print("sfzbm" + ": " + imgIdCardN.getPath()+"\n");
        }

        if(imgLicense != null){
            LogUtil.print("gswj" + ": " + imgLicense.getPath()+"\n");
        }

        LogUtil.print("}");

        LogUtil.print(CPigeonApiUrl.getInstance().getServer()
                + "/CPAPI/V1/"+"GXT_TJGRXX");

        RequestBody requestBody = builder.build();


        return RetrofitHelper.getApi().uploadPersonInfo(
                CommonTool.getUserToken(MyApp.getInstance().getBaseContext()),
                String.valueOf(userId),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                requestBody);

    }


    private static String getString(String s){
        return StringValid.isStringValid(s) ? s:"";
    }

}
