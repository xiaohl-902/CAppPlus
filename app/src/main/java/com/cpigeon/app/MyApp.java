package com.cpigeon.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.cache.CacheManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.cpigeon.app.modular.settings.view.activity.SettingPushActivity.SETTING_KEY_PUSH_ENABLE;
import static com.cpigeon.app.modular.settings.view.activity.SettingPushActivity.SETTING_KEY_PUSH_NOTIFICATION;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyApp extends Application {

    {
        PlatformConfig.setWeixin("wxc9d120321bd1180a", "1e9c27bb36823ccca5dcc5ae7e089126");
        PlatformConfig.setQQZone("1105989530", "ztLtwrRKr7YiDLly");
    }

    private static MyApp instance;
    private static String TAG = "cpigeon";
    public static int screenWidth;
    public static int screenHeight;


    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        instance = this;
        initBugly();
        if (BuildConfig.DEBUG) {
            Logger.init(TAG).methodCount(3);
        }
        initJPhsh();
        initLocalCacheManager();
        Fresco.initialize(this);
        UMShareAPI.get(this);

        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;

        //Config.DEBUG = true;
    }

    /**
     * 低内存自动杀死
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化BUGLY
     */
    private void initBugly() {

        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);

        CrashReport.initCrashReport(getApplicationContext(), "f7c8c8f49a", BuildConfig.DEBUG);

        //设置用户ID
        CrashReport.setUserId("" + CpigeonData.getInstance().getUserId(this));
        try {
            //获取并设置Bugly渠到
            ApplicationInfo appInfo = null;
            appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            String app_channel = appInfo.metaData.getString("APP_CHANNEL");
            CrashReport.setAppChannel(getApplicationContext(), app_channel);
            Log.d(TAG, "APP_CHANNEL:" + app_channel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化极光推送
     */
    private void initJPhsh() {
        try {
            //极光推送
            JPushInterface.setDebugMode(BuildConfig.DEBUG);
            JPushInterface.init(this);
            //加载提示配置，默认全部
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(instance);
            builder.notificationDefaults = SharedPreferencesTool.Get(instance, SETTING_KEY_PUSH_NOTIFICATION, ~0, SharedPreferencesTool.SP_FILE_APPSETTING);
            JPushInterface.setDefaultPushNotificationBuilder(builder);

            if (SharedPreferencesTool.Get(instance, SETTING_KEY_PUSH_ENABLE, true, SharedPreferencesTool.SP_FILE_APPSETTING)) {
                JPushInterface.resumePush(instance);
            } else {
                JPushInterface.stopPush(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化本地缓存管理器
     */
    private void initLocalCacheManager() {
        CacheManager.setCacheModel(CacheManager.ALL_ALLOW);
        CacheManager.setMemaryCacheTime(30 * 1000);
        CacheManager.setDiskCacheTime(3 * 60 * 1000);
        CacheManager.init(this);
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。

    public static void setJPushAlias() {

        String alias = String.valueOf(CpigeonData.getInstance().getUserId(instance));
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (alias.equals(SharedPreferencesTool.Get(instance, "jpush_alia", ""))) {
            return;
        }
        // 调用 Handler 来异步设置别名
        mJpushHandler.sendMessage(mJpushHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    public static void clearJPushAlias() {
        // 调用 Handler 来异步设置别名
        //mJpushHandler.sendMessage(mJpushHandler.obtainMessage(MSG_SET_ALIAS, "_1"));
    }

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            /**
             *6001   无效的设置，tag/alias 不应参数都为 null
             *6002   设置超时    建议重试
             *6003   alias 字符串不合法    有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
             *6004   alias超长。最多 40个字节    中文 UTF-8 是 3 个字节
             *6005   某一个 tag 字符串不合法  有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
             *6006   某一个 tag 超长。一个 tag 最多 40个字节  中文 UTF-8 是 3 个字节
             *6007   tags 数量超出限制。最多 100个 这是一台设备的限制。一个应用全局的标签数量无限制。
             *6008   tag/alias 超出总长度限制。总长度最多 1K 字节
             *6011   10s内设置tag或alias大于3次 短时间内操作过于频繁
             **/
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Logger.i(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedPreferencesTool.Save(instance, "jpush_alia", alias);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Logger.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mJpushHandler.sendMessageDelayed(mJpushHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Logger.e(logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private static final Handler mJpushHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Logger.d("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(instance,
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Logger.i("Unhandled msg - " + msg.what);
            }
        }
    };

}
