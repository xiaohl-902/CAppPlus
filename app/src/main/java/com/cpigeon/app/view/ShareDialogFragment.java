package com.cpigeon.app.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.cpigeon.app.R;
import com.cpigeon.app.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;

import java.io.File;


/**
 * Created by Administrator on 2017/1/7.
 * 分享Fragment
 */

public class ShareDialogFragment extends DialogFragment {

    public static final int TYPE_DEFALTE = -1;
    public static final int TYPE_URL = 1;
    public static final int TYPE_IMAGE_URL = 2;
    public static final int TYPE_IMAGE_FILE = 3;
    public static final int TYPE_VIDEO = 4;


    private ImageButton imgbtn_wx, imgbtn_pyq, imgbtn_qq, imgbtn_qqz;//分享按钮
    private Button btn_cancel;//取消

    private Bitmap mBitmap;

    public UMShareListener umShareListener;
    private OnShareCallBackListener onShareCallBackListener;

    private String shareUrl;

    private int shareType = -1;// -1  默认， 1 链接   2网络图片  3 本地图片 4 视频

    private String description = "中鸽网分享";

    private File file;

    private String videoUrl;
    private String videoThumb;
    private String videoTitle;
    private String title = "中鸽网";

    public void setTitle(String title) {
        this.title = title;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgbtn_wx:
                    //微信分享
                    startShareApp(SHARE_MEDIA.WEIXIN);
                    break;
                case R.id.imgbtn_pyq:
                    //微信朋友圈
                    startShareApp(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.imgbtn_qq:
                    //QQ
                    startShareApp(SHARE_MEDIA.QQ);
                    break;

                case R.id.imgbtn_qqz:
                    //QQ空间
                    startShareApp(SHARE_MEDIA.QZONE);
                    break;
                case R.id.btn_cancel:
                    dismiss();
                    break;
            }
        }
    };

    private void startShareApp(SHARE_MEDIA platform) {
        if (shareType == TYPE_DEFALTE) {
            new ShareAction(getActivity())
                    .setPlatform(platform)//传入平台
                    .withText(description)
                    .setCallback(umShareListener)//回调监听器
                    .share();

        } else if (shareType == 1) {
            //分享链接
            UMWeb web = new UMWeb(shareUrl);
            UMImage image = null;
            if (file != null) {
                image = new UMImage(getActivity(), file);
            }
            web.setTitle(title);//标题
            web.setDescription(description);//描述
            new ShareAction(getActivity())
                    .setPlatform(platform)//传入平台
                    .withMedia(web)//分享内容
                    .setCallback(umShareListener)//回调监听器
                    .share();
        } else if (shareType == 2) {
            //分享图片
            UMImage image = new UMImage(getActivity(), shareUrl);//网络图片
            image.setTitle(title);

            new ShareAction(getActivity())
                    .setPlatform(platform)//传入平台
                    .withMedia(image)//分享内容
                    .withText(description)
                    .setCallback(umShareListener)//回调监听器
                    .share();
        } else if (shareType == 3) {
            //分享图片
            UMImage image;
            if (file != null) {
                image = new UMImage(getActivity(), file);//file图片
            } else {
                image = new UMImage(getActivity(), mBitmap);
            }

            new ShareAction(getActivity())
                    .setPlatform(platform)//传入平台
                    .withMedia(image)//分享内容
                    .withText(description)
                    .setCallback(umShareListener)//回调监听器
                    .share();
        } else if (shareType == TYPE_VIDEO) {

            UMWeb web = new UMWeb(shareUrl);
            web.setTitle("中鸽网");//标题
            web.setDescription("中鸽网分享");//描述
            web.setThumb(new UMImage(getActivity(), videoThumb));//file图片);
            new ShareAction(getActivity())
                    .setPlatform(platform)//传入平台
                    .withMedia(web)//分享内容
                    .setCallback(umShareListener)//回调监听器
                    .share();
        }
    }

    private String TAG = "ShareDialogFragment";

    //分享类型 // -1  默认， 1 链接   2。图片
    public void setShareType(int shareType) {
        this.shareType = shareType;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                try {
                    ToastUtil.showShortToast(getActivity(), "分享成功");
                    if (onShareCallBackListener != null) {
                        onShareCallBackListener.onSuccess();
                    }
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                try {
                    ToastUtil.showShortToast(getActivity(), throwable.getMessage());
                    if (onShareCallBackListener != null) {
                        onShareCallBackListener.onFail();
                    }

                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                try {
                    ToastUtil.showShortToast(getActivity(), "分享取消");
                    if (onShareCallBackListener != null) {
                        onShareCallBackListener.onFail();
                    }
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

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

        imgbtn_wx.setOnClickListener(clickListener);
        imgbtn_pyq.setOnClickListener(clickListener);
        imgbtn_qq.setOnClickListener(clickListener);
        imgbtn_qqz.setOnClickListener(clickListener);
        btn_cancel.setOnClickListener(clickListener);

    }

    public void setShareContent(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setShareContent(Bitmap shareBitmap) {
        this.mBitmap = shareBitmap;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public interface OnShareListener {
        void onShare(Dialog dialog);
    }

    public interface OnShareCallBackListener {
        void onSuccess();

        void onFail();
    }

    public void setOnShareCallBackListener(OnShareCallBackListener onShareCallBackListener) {
        this.onShareCallBackListener = onShareCallBackListener;
    }

//    @Override
//    public void show(FragmentManager manager, String tag) {
//        if (getDialog() != null && getDialog().isShowing()) {
//            dismiss();
//        }
//        super.show(manager, tag);
//
//    }
}
