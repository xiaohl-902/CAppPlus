package com.cpigeon.app.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.cache.CacheManager;
import com.cpigeon.app.utils.cache.DataCleanManager;
import com.cpigeon.app.utils.databean.UpdateInfo;
import com.cpigeon.app.utils.http.LogUtil;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//import com.cpigeon.common.AppManager;

/**
 * Created by Administrator on 2016-10-20.
 */

public class UpdateManager {
    private static final String DOWNLOAD_STATE_NO = "no";
    private static final String DOWNLOAD_STATE_READY = "ready";
    private static final String DOWNLOAD_STATE_ERROR = "error";
    private static final String DOWNLOAD_STATE_CANCEL = "cancel";
    private static final String DOWNLOAD_STATE_FINISHED = "finished";

    private OnInstallAppListener onInstallAppListener;
    private OnCheckUpdateInfoListener onCheckUpdateInfoListener;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    private long loadingTime = 1;

    public UpdateManager(@NonNull Context context) {
        mContext = context;
    }

    public void setOnCheckUpdateInfoListener(OnCheckUpdateInfoListener onCheckUpdateInfoListener) {
        this.onCheckUpdateInfoListener = onCheckUpdateInfoListener;
    }

    public UpdateManager setOnInstallAppListener(UpdateManager.OnInstallAppListener onInstallAppListener) {
        this.onInstallAppListener = onInstallAppListener;
        return this;
    }

    /**
     * 检查App更新(从服务器拉取数据)
     */
    public void checkUpdate() {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onGetUpdateInfoStart();
        //从服务器获取更新信息
        CallAPI.getUpdateInfo(new CallAPI.Callback<List<UpdateInfo>>() {
            @Override
            public void onSuccess(final List<UpdateInfo> data) {
                if (onCheckUpdateInfoListener != null) {
                    new Handler(Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                             @Override
                                             public void run() {
                                                 if (!onCheckUpdateInfoListener.onGetUpdateInfoEnd(data))
                                                     checkUpdate(data);
                                             }
                                         }
                                    , 500);

                } else {
                    checkUpdate(data);
                }
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCheckUpdateInfoListener != null)
                    onCheckUpdateInfoListener.onGetUpdateInfoEnd(null);
            }
        });
    }

    /**
     * 检查App更新
     */

    private void checkUpdate(List<UpdateInfo> updateInfos) {
        if (updateInfos == null || updateInfos.size() == 0) return;
        for (UpdateInfo updateInfo : updateInfos) {
            if (mContext.getPackageName() != null && mContext.getPackageName().equals(updateInfo.getPackageName())) {
                if (updateInfo.getVerCode() > CommonTool.getVersionCode(mContext)) {
                    updateReady(updateInfo);
                    return;
                }
            }
        }
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onNotFoundUpdate();
    }

    /**
     * 下载准备
     */

    private void updateReady(final UpdateInfo updateInfo) {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onHasUpdate(updateInfo);
        final String _url = updateInfo.getUrl();
        LogUtil.print("apk_url:"+ _url);

        //Logger.i("下载");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = ((BaseActivity)mContext).getLayoutInflater().inflate(R.layout.dialog_update_layout,null);

        TextView newVersion = view.findViewById(R.id.n_version);
        TextView oldVersion = view.findViewById(R.id.c_version);
        TextView time = view.findViewById(R.id.time);
        TextView content = view.findViewById(R.id.content);

        TextView btn = view.findViewById(R.id.btn);
        ImageView close = view.findViewById(R.id.close);

        newVersion.setText(updateInfo.getVerName());
        oldVersion.setText("当前版本:" + CommonTool.getVersionName(mContext));
        time.setText("更新时间：" + updateInfo.getUpdateTime());
        content.setText(updateInfo.getUpdateExplain());

        btn.setOnClickListener(v -> {

//            try {
//                GoToMarkerUtils.goToMarker(mContext);
//            } catch (Exception e) {
                String path = CpigeonConfig.UPDATE_SAVE_FOLDER + mContext.getPackageName() + ".apk";
                //判断当前是否已经下载了
                if (SharedPreferencesTool.Get(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE).equals(UpdateManager.DOWNLOAD_STATE_FINISHED)) {
                    //获取APK信息
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                    //判断是否是最新版本的APK文件
                    if (info != null && info.versionCode == updateInfo.getVerCode() && info.applicationInfo.packageName.equals(updateInfo.getPackageName())) {
                        install(path);
                        return;
                    }
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_READY, SharedPreferencesTool.SP_FILE_APPUPDATE);
                DownLoad(_url, path);
//            }

            try {
                CacheManager.init(mContext);
                FileTool.DeleteFolder(CpigeonConfig.CACHE_FOLDER, false);
                CacheManager.delete();
                try {
                    DbManager db = x.getDb(CpigeonConfig.getDataDb());
                    db.delete(MatchInfo.class, WhereBuilder.b()
                            .and("lx", "=", "gp")
                            .and("st", "<", DateTool.dateTimeToStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * (1 + CpigeonConfig.LIVE_DAYS_GP)))));
                    db.delete(MatchInfo.class, WhereBuilder.b().and("lx", "=", "xh").and("st", "<", DateTool.dateTimeToStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * (1 + CpigeonConfig.LIVE_DAYS_XH)))));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                DataCleanManager.cleanApplicationData(mContext, (String[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        close.setOnClickListener(v -> {
            SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE);
            if (updateInfo.isForce())
                System.exit(0);
        });

        builder.setView(view);

        /*builder.setMessage("当前版本:" + CommonTool.getVersionName(mContext)
                + "\n更新时间：" + updateInfo.getUpdateTime()
                + "\n更新说明：\n" + updateInfo.getUpdateExplain()
        );
        builder.setTitle("发现新版本:" + updateInfo.getVerName());
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = CpigeonConfig.UPDATE_SAVE_FOLDER + mContext.getPackageName() + ".apk";
                //判断当前是否已经下载了
                if (SharedPreferencesTool.Get(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE).equals(UpdateManager.DOWNLOAD_STATE_FINISHED)) {
                    //获取APK信息
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                    //判断是否是最新版本的APK文件
                    if (info != null && info.versionCode == updateInfo.getVerCode() && info.applicationInfo.packageName.equals(updateInfo.getPackageName())) {
                        install(path);
                        return;
                    }
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_READY, SharedPreferencesTool.SP_FILE_APPUPDATE);
                DownLoad(_url, path);

            }
        });
        builder.setNegativeButton(updateInfo.isForce() ? "退出程序" : "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (updateInfo.isForce())
                    System.exit(0);
            }
        });*/
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 下载安装包
     *
     * @param url  下载路径
     * @param path 保存路径
     */
    private void DownLoad(String url, final String path) {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onDownloadStart();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("准备下载");
        mProgressDialog.setCancelable(false);// 设置点击空白处也不能关闭该对话框
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置采用圆形进度条
        mProgressDialog.setMax(0);
//      mProgressDialog.setIndeterminate(true);//设置不显示明确的进度
        mProgressDialog.setIndeterminate(false);// 设置显示明确的进度
        mProgressDialog.setProgressNumberFormat("%1dK/%2dK");
        mProgressDialog.show();
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);

        x.http().get(params, new Callback.ProgressCallback<File>() {


            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                mProgressDialog.setMessage("开始下载");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                if (mProgressDialog.getMax() == 100)
                    mProgressDialog.setMax((int) (total / 1024));
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) (f1 * mProgressDialog.getMax()));
            }

            @Override
            public void onSuccess(File result) {

                mProgressDialog.setMessage("下载完成");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_FINISHED, SharedPreferencesTool.SP_FILE_APPUPDATE);
                install(path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.showToast(MyApp.getInstance(), "下载失败", Toast.LENGTH_LONG);
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_ERROR, SharedPreferencesTool.SP_FILE_APPUPDATE);
                closeAndExit();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_CANCEL, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void install(String path) {

        if (Build.VERSION.SDK_INT >= 26) {//8.0
            //判断是否可以直接安装
            boolean canInstall = mContext.getPackageManager().canRequestPackageInstalls();
            if (canInstall) {
                //rxpermissions请求权限
                new RxPermissions((Activity) mContext)
                        .request(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                        .subscribe(granted -> {
                            if (granted) {
                                //安装apk
                                CommonTool.installApp(mContext, path);
                                if (onInstallAppListener != null)
                                    onInstallAppListener.onInstallApp();
                            }else {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                ((Activity)mContext).startActivityForResult(intent, 0x123);
                            }
                        });
            } else {
                //安装apk
                CommonTool.installApp(mContext, path);
                if (onInstallAppListener != null)
                    onInstallAppListener.onInstallApp();
            }
        } else {
            //安装apk
            CommonTool.installApp(mContext, path);
            if (onInstallAppListener != null)
                onInstallAppListener.onInstallApp();
        }


        /*if (Build.VERSION.SDK_INT >= 26) {
            boolean b = mContext.getPackageManager().canRequestPackageInstalls();1
            if (b) {
                CommonTool.installApp(mContext, path);
                if (onInstallAppListener != null)
                    onInstallAppListener.onInstallApp();;//安装应用的逻辑(写自己的就可以)
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            CommonTool.installApp(mContext, path);
            if (onInstallAppListener != null)
                onInstallAppListener.onInstallApp();
        }*/
    }

    private void closeAndExit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        System.exit(0);
    }

    public interface OnCheckUpdateInfoListener {
        /**
         * 开始获取更新信息
         */
        void onGetUpdateInfoStart();

        /**
         * 获取更新信息完成
         *
         * @param updateInfos
         * @return
         */
        boolean onGetUpdateInfoEnd(List<UpdateInfo> updateInfos);

        /**
         * 没有更新
         */
        void onNotFoundUpdate();

        /**
         * 有更新
         *
         * @param updateInfo
         */
        void onHasUpdate(UpdateInfo updateInfo);

        /**
         * 开始下载更新包
         */
        void onDownloadStart();
    }

    public interface OnInstallAppListener {
        void onInstallApp();
    }



}