package com.cpigeon.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Zhu TingYu on 2018/3/9.
 */

public class GoToMarkerUtils {

    public static void goToMarker(Context context){
        try {
            goToBNormalMarker(context);
        } catch (Exception e) {
            try {
                goToXiaoMiMarket(context);
            } catch (Exception e1) {
                try {
                    goToSamsungappsMarket(context);
                } catch (Exception e2) {
                    goToLeTVStoreDetail(context);
                }
            }
        }
    }

    public static void goToBNormalMarker(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(goToMarket);
    }

    public static void goToXiaoMiMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
        context.startActivity(goToMarket);
    }

    public static void goToSamsungappsMarket(Context context) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + context.getPackageName());
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        context.startActivity(goToMarket);
    }

    public static void goToLeTVStoreDetail(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", context.getPackageName());
        context.startActivity(intent);

    }
}
