package com.cpigeon.app.modular.shootvideo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


import com.cpigeon.app.R;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.SendWX;
import com.cpigeon.app.utils.http.CommonUitls;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * Created by Administrator on 2017/1/7.
 * 分享Fragment
 */

public class LocalShareDialogFragment extends DialogFragment {


    private ImageButton imgbtn_wx, imgbtn_pyq, imgbtn_qq, imgbtn_qqz;//分享按钮
    private Button btn_cancel;//取消

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgbtn_wx:
                    //微信分享

                    share2WX();

                    LocalShareDialogFragment.this.dismiss();
                    break;
                case R.id.imgbtn_pyq:
                    //微信朋友圈

                    break;
                case R.id.imgbtn_qq:
                    //QQ
                    share2QQ();
                    LocalShareDialogFragment.this.dismiss();
                    break;
                case R.id.imgbtn_qqz:
                    //QQ空间

                    break;
                case R.id.btn_cancel:
                    dismiss();
                    break;
            }
        }
    };

    private String TAG = "ShareDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_layout_share);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 5;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView(dialog);

        return dialog;
    }

    private void initView(Dialog dialog) {
        imgbtn_wx = (ImageButton) dialog.findViewById(R.id.imgbtn_wx);
        imgbtn_pyq = (ImageButton) dialog.findViewById(R.id.imgbtn_pyq);
        imgbtn_qq = (ImageButton) dialog.findViewById(R.id.imgbtn_qq);
        imgbtn_qqz = (ImageButton) dialog.findViewById(R.id.imgbtn_qqz);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        imgbtn_pyq.setVisibility(View.GONE);
        imgbtn_qqz.setVisibility(View.GONE);

        imgbtn_wx.setOnClickListener(clickListener);
        imgbtn_pyq.setOnClickListener(clickListener);
        imgbtn_qq.setOnClickListener(clickListener);
        imgbtn_qqz.setOnClickListener(clickListener);
        btn_cancel.setOnClickListener(clickListener);
    }

    //本地分享
    private boolean isUM = true;
    private String localFilePath = "";
    private String fileType = "video";

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public void setIsUM(boolean isUMShare) {
        this.isUM = isUMShare;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public void share2QQ() {

        try {
            checkFileUriExposure();

            Intent qqIntent = new Intent(Intent.ACTION_SEND);
            qqIntent.setPackage("com.tencent.mobileqq");
            qqIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            File file = new File(localFilePath);

            if (fileType.equals("video")) {
                qqIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localFilePath));
                qqIntent.setType("video/*");
                startActivity(qqIntent);
            } else {
                qqIntent.putExtra(Intent.EXTRA_STREAM, insertImageToSystem(getActivity(), localFilePath));
                qqIntent.setType("image/*");
                startActivity(Intent.createChooser(qqIntent, "图片分享"));
            }

        } catch (Exception e) {
            Log.d(TAG, "share2WX: " + e.getLocalizedMessage());
        }
    }


    public void share2WX() {
        try {

            checkFileUriExposure();

            if (SendWX.isWeixinAvilible(getActivity())) {
                ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                Intent shareIntent = new Intent();
                shareIntent.setComponent(comp);
                shareIntent.putExtra("Kdescription", "11");

                File file = new File(localFilePath);

                if (fileType.equals("video")) {
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(localFilePath)));
                    startActivity(Intent.createChooser(shareIntent, "分享"));
                } else {
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Lists.newArrayList(Uri.parse(insertImageToSystem(getActivity(), localFilePath))));
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "分享"));
                }

            } else {
                CommonUitls.showToast(getActivity(), "请先安装微信APP");
            }

        } catch (Exception e) {
            Log.d(TAG, "share2WX: " + e.getLocalizedMessage());
        }
    }


    /**
     * 分享前必须执行本代码，主要用于兼容SDK18以上的系统
     */
    private static void checkFileUriExposure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "ic", "你对图片的描述");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }


//    public void share2QQ_Z() {
//        try {
//            Intent qqIntent = new Intent(Intent.ACTION_SEND);
//            qqIntent.setPackage("com.tencent.mobileqq");
//            ComponentName comp = new ComponentName("com.qzone", "com.qzonex.module.maxvideo.activity.QzonePublishVideoActivity");
//            qqIntent.setComponent(comp);
//            File file = new File(localFilePath);
//            System.out.println("file " + file.exists());
//            qqIntent.putExtra(Intent.EXTRA_STREAM, getImageContentUri(file));
//            qqIntent.setType("*/*");
//            startActivity(qqIntent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public void share2WX_pyq() {
//        try {
//            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//            Intent shareIntent = new Intent();
//            shareIntent.setComponent(comp);
//            shareIntent.setAction(Intent.ACTION_SEND);
//            File file = new File(localFilePath);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageContentUri(file));
//            shareIntent.setType("*/*");
//            startActivity(Intent.createChooser(shareIntent, "分享"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 转换 content:// uri
     *
     * @param imageFile
     * @return
     */
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
