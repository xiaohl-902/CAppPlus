package com.cpigeon.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.amap.api.maps.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.utils.customview.CustomEmptyView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cpigeon.app.utils.DateTool.doubleformat;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CommonTool {


    //public final static String PATTERN_EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public final static String PATTERN_TENDIGITS = "^[1-9]\\d{9}$";
    public final static String PATTERN_PHONE = "^[1][3-8]+\\d{9}$";
    public final static String PATTERN_ALLNUMBER = "^\\d+$";
    public final static String PATTERN_URL = "^((https|http|ftp|rtsp|mms)?://)"
            + "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" //ftp的user@
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"                                 // IP形式的URL- 199.194.52.184
            + "|"                                                         // 允许IP和DOMAIN（域名）
            + "([0-9a-zA-Z_!~*'()-]+\\.)*"                                 // 域名- www.
            + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\."                     // 二级域名
            + "[a-zA-Z]{2,6})"                                         // first level domain- .com or .museum
            + "(:[0-9]{1,4})?"                                                     // 端口- :80
            + "((/?)|"
            + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    public final static String PATTERN_WEB_URL = "^((https|http)?://)"
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"                                 // IP形式的URL- 199.194.52.184
            + "|"                                                         // 允许IP和DOMAIN（域名）
            + "([0-9a-zA-Z_!~*'()-]+\\.)*"                                 // 域名- www.
            + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\."                     // 二级域名
            + "[a-zA-Z]{2,6})"                                         // first level domain- .com or .museum
            + "(:[0-9]{1,4})?"                                                     // 端口- :80
            + "((/?)|"
            + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    private static String DeviceID;

    /**
     * 获得当前应用的版本号名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获得当前应用的版本号数字
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            verCode = 0;
        }
        return verCode;
    }

    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }


    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lat1 30.66869
     * @param lng1 104.032232
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return Math.abs(s);
    }

    public static double Aj2GPSLocation(double ajLocationValue) {
        int du = (int) ajLocationValue;
        double temp = (ajLocationValue - du) * 100;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 100;//秒
        return du + (double) fen / 60 + temp / 3600;
    }

    public static String GPS2AjLocation(double gpsLocationValue) {
        int du = (int) gpsLocationValue;
        double temp = (gpsLocationValue - du) * 60;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 60;//秒
        return doubleformat(du + (double) fen / 100 + temp / 10000, 6);
    }

    public static String GPSformatOfEveryMinute(double gpsLocationValue) {
        int du = (int) gpsLocationValue;
        double temp = (gpsLocationValue - du) * 60;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 60;//秒
        return doubleformat(du, 2) + "°" + seizeASeat(doubleformat(fen, 2)) + "'" + resizeMiao(temp) + "\"";
    }

    /**
     * 通过字符串，获取度分秒
     */
    public static String strToDMs(String str) {

        String strDms = "";
        strDms = ((str.split("\\."))[0]).toString() + "°";//度

        String[] srr = (str.split("\\."));

        if (srr.length != 2) {
            return str + "°";
        }

        byte[] strByte = srr[1].getBytes();

        if (strByte != null) {
            switch (strByte.length) {
                case 0:
                    break;
                case 1:
                    strDms = strDms + "0" + (strByte[0] - 48) + "\'00.00\"";
                    break;
                case 2:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + "'00.00\"";

                    break;
                case 3:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'0" + (strByte[2] - 48) + ".00\"";
                    break;
                case 4:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + ".00\"";
                    break;

                case 5:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + "0\"";
                    break;
                case 6:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "\"";
                    break;
                default:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "\"";
            }
        }
        return strDms;
    }


    private static String seizeASeat(String string) {
        if (string.length() == 1) {
            return "0" + string;
        }
        return string;
    }

    private static String resizeMiao(double value) {
        StringBuffer sb = new StringBuffer();
        int beforePoint = (int) value;
        double afterPoint = (value - beforePoint);

        if (beforePoint / 10 < 1) {
            sb.append(0);
            sb.append(beforePoint);
        } else {
            sb.append(beforePoint);
        }

        sb.append(".");

        int temp = (int) (Double.valueOf(doubleformat(afterPoint, 2)) * 100);

        if (temp < 10) {
            sb.append(0);
            sb.append(temp);
        } else {
            sb.append(temp);
        }

        return sb.toString();
    }

    /**
     * 安装应用
     *
     * @param context
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        try {
            Logger.d(filePath);
            File _file = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", _file);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fileUri = Uri.fromFile(_file);
            }
            Logger.d(fileUri);
            intent.setDataAndType(fileUri,
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出
     *
     * @param activity
     */
    public static void exitApp(Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
        activity.finish();
        System.exit(0);
    }

    public static void hideIME(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public static String getUserToken(Context context) {
        StringBuilder userToken = new StringBuilder();
        String userTokenOri = "";
        try {
            userTokenOri = EncryptionTool.decryptAES((String) SharedPreferencesTool.Get(context, "token", "", SharedPreferencesTool.SP_FILE_LOGIN));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        userToken.append(userTokenOri);
        userToken.append("|appcpigeon|");
        userToken.append(System.currentTimeMillis() / 1000);
        userToken.append("|android " + getVersionCode(context));
        Logger.i(userToken.toString());
        String res = EncryptionTool.encryptAES(userToken.toString());
        Logger.i(res);
        return res;
    }

    /**
     * 通用正则表达式调用方法
     *
     * @param Content    需要检查的字符串
     * @param PatternStr 正则表达式
     * @return
     */
    public static boolean Compile(String Content, String PatternStr) {
        Pattern p = Pattern.compile(PatternStr);
        Matcher m = p.matcher(Content);
        return m.matches();
    }

    /**
     * 结合多种方式计算设备唯一码
     *
     * @param context
     * @return
     */
    public static String getCombinedDeviceID(Context context) {
        if (!TextUtils.isEmpty(DeviceID)) return DeviceID;
        //IMEI
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String m_szImei = null;
        try {
            m_szImei = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            m_szImei = "";
        }

        //Pseudo-Unique ID
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        //Android ID
        String m_szAndroidID = null;
        try {
            m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            m_szAndroidID = "";
        }

        //WIFI MAC Address string

//        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        String m_szWLANMAC = getMacAddr();
        //BT MAC Address string
        String m_szBTMAC = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");

        String m_szLongID = m_szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        //compute md5
        DeviceID = EncryptionTool.MD5(m_szLongID).toLowerCase();
        return DeviceID;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static void setEmptyView(BaseQuickAdapter adapter, String message) {
        setEmptyView(adapter, R.drawable.ic_empty_img, message);


    }

    public static void setEmptyView(BaseQuickAdapter adapter, @DrawableRes int resId, String message) {
        CustomEmptyView emptyView = new CustomEmptyView(MyApp.getInstance().getBaseContext());
        emptyView.setEmptyImage(resId);
        emptyView.setEmptyText(message);
        adapter.setEmptyView(emptyView);
    }

    public static void setEmptyView(BaseQuickAdapter adapter, View.OnClickListener listener) {
        CustomEmptyView emptyView = new CustomEmptyView(MyApp.getInstance().getBaseContext());
        emptyView.isNotHaveNet();
        emptyView.setEmptyImage(R.drawable.ic_net_error);
        emptyView.setButtomOnClickListener(listener);
        adapter.setEmptyView(emptyView);
    }

    public static void setIconView(ImageView imageView, @DrawableRes int url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.mipmap.head_image_default)
                .error(R.mipmap.head_image_default)
                .into(imageView);
    }

    public static void setIconView(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.mipmap.head_image_default)
                .error(R.mipmap.head_image_default)
                .into(imageView);
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 两个点距离（两个经纬之间的距离（单位：米））
     * @param var0
     * @param var1
     * @return
     */

    public static float calculateLineDistance(LatLng var0, LatLng var1) {
            double var2 = 0.01745329251994329D;
            double var4 = var0.longitude;
            double var6 = var0.latitude;
            double var8 = var1.longitude;
            double var10 = var1.latitude;
            var4 *= 0.01745329251994329D;
            var6 *= 0.01745329251994329D;
            var8 *= 0.01745329251994329D;
            var10 *= 0.01745329251994329D;
            double var12 = Math.sin(var4);
            double var14 = Math.sin(var6);
            double var16 = Math.cos(var4);
            double var18 = Math.cos(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.sin(var10);
            double var24 = Math.cos(var8);
            double var26 = Math.cos(var10);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var18 * var16;
            var28[1] = var18 * var12;
            var28[2] = var14;
            var29[0] = var26 * var24;
            var29[1] = var26 * var20;
            var29[2] = var22;
            double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
            return (float) (Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
    }

    /**
     * 地图轨迹抽稀算法
     * @param points
     * @param epsilon
     * @return
     */

    public static List<LatLng> DouglasPeucker(List<LatLng> points, int epsilon) {
        // 找到最大阈值点，即操作（1）
        double maxH = 0;
        int index = 0;
        int end = points.size();
        for (int i = 1; i < end - 1; i++) {
            double h = H(points.get(i), points.get(0), points.get(end - 1));
            if (h > maxH) {
                maxH = h;
                index = i;
            }
        }

        // 如果存在最大阈值点，就进行递归遍历出所有最大阈值点
        List<LatLng> result = new ArrayList<>();
        if (maxH > epsilon) {
            List<LatLng> leftPoints = new ArrayList<>();// 左曲线
            List<LatLng> rightPoints = new ArrayList<>();// 右曲线
            // 分别提取出左曲线和右曲线的坐标点
            for (int i = 0; i < end; i++) {
                if (i <= index) {
                    leftPoints.add(points.get(i));
                    if (i == index)
                        rightPoints.add(points.get(i));
                } else {
                    rightPoints.add(points.get(i));
                }
            }

            // 分别保存两边遍历的结果
            List<LatLng> leftResult = new ArrayList<>();
            List<LatLng> rightResult = new ArrayList<>();
            leftResult = DouglasPeucker(leftPoints, epsilon);
            rightResult = DouglasPeucker(rightPoints, epsilon);

            // 将两边的结果整合
            rightResult.remove(0);//移除重复点
            leftResult.addAll(rightResult);
            result = leftResult;
        } else {// 如果不存在最大阈值点则返回当前遍历的子曲线的起始点
            result.add(points.get(0));
            result.add(points.get(end - 1));
        }
        return result;
    }

    /**
     * 计算点到直线的距离
     *
     * @param p
     * @param s
     * @param e
     * @return
     */
    public static double H(LatLng p, LatLng s, LatLng e) {
        double AB = calculateLineDistance(s, e);
        double CB = calculateLineDistance(p, s);
        double CA = calculateLineDistance(p, e);

        double S = helen(CB, CA, AB);
        double H = 2 * S / AB;

        return H;
    }

    /**
     * 计算两点之间的距离
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double distance(Point p1, Point p2) {
        double x1 = p1.x;
        double y1 = p1.y;

        double x2 = p2.x;
        double y2 = p2.y;

        double xy = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return xy;
    }

    /**
     * 海伦公式，已知三边求三角形面积
     *
     * @param CB
     * @param CA
     * @param CB
     * @return 面积
     */
    public static double helen(double CB, double CA, double AB) {
        double p = (CB + CA + AB) / 2;
        double S = Math.sqrt(p * (p - CB) * (p - CA) * (p - AB));
        return S;
    }


}
