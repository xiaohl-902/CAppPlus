package com.cpigeon.app.utils;

import android.Manifest;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 权限
 * Created by Administrator on 2017/11/24.
 */

public class PermissonUtil {


    /**
     * 权限检查
     */
    public static void getAppDetailSettingIntent(Context mContext) {

        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
        permissonItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "电话簿", R.drawable.permission_ic_phone));
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "网络信息", R.drawable.permission_ic_phone));
        permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "相机", R.drawable.permission_ic_camera));
        permissonItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS, "联系人", R.drawable.permission_ic_contacts));
        permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "地理位置", R.drawable.permission_ic_location));
        permissonItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));

        HiPermission.create(mContext)
                .title("权限设置")
                .permissions(permissonItems)
                .filterColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, mContext.getTheme()))
                .msg("当前应用需要一些权限，请打开")
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        AppManager.getAppManager().AppExit(mContext);
                    }

                    @Override
                    public void onFinish() {
//                        checkDevice();
                    }

                    @Override
                    public void onDeny(String s, int i) {
                        Logger.e("onDeny" + s + "第" + i + "个");
                    }

                    @Override
                    public void onGuarantee(String s, int i) {
                        Logger.e("onGuarantee" + s + "第" + i + "个");
                    }
                });
    }
}
