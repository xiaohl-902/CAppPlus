package com.cpigeon.app.utils;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */


import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class SendWX {
    public String APP_ID = "";
    public static String STATE = "EShopApp";
    private Context context;
    private IWXAPI api;

    public SendWX(BaseActivity context) {
        this.context = context;
        this.APP_ID = getAppId();
    }
    public SendWX(Context context) {
        this.context = context;
        this.APP_ID = getAppId();
    }

    public static String getAppId() {
        try {
            return new String(Base64.decode(WXPayEntryActivity.APP_ID, Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    public static String getAppSecret()
    {
        try {
            return new String(Base64.decode(WXPayEntryActivity.APP_ID, Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public Boolean isBinding() {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
        return api.isWXAppInstalled();
    }


    public boolean isInstalled() {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        return api.isWXAppInstalled();
    }



    public void payWeiXin(PayReq payReq) {
        if (context == null) {
            return;
        }
        if (payReq == null) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
            }
            return;
        }
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        if (!api.isWXAppInstalled()) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
            }
            createDialog();
            return;
        }
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
            }
            createDialogNotPay();
            return;
        }
        api.sendReq(payReq);
    }

    /*private void sendWeb(boolean isTimelineCb, String webpageUrl, String title, String desc, Bitmap thumb) {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
        if (!api.isWXAppInstalled()) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            createDialog();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        if (thumb != null) {
            msg.setThumbImage(thumb);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }*/



    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("当前没有下载微信，请下载安装");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void createDialogNotPay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("当前版本不支持微信功能，请下载安装");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    // 判断手机内是否安装了微信APP
    public static boolean isWeixinAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


}
