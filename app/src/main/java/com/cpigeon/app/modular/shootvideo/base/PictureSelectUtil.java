package com.cpigeon.app.modular.shootvideo.base;


import android.app.Activity;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class PictureSelectUtil {


    public static void showChooseImage(Activity activity, int type, int count) {
        showChooseImage(activity, type, count, false, PictureConfig.MULTIPLE);
    }

    public static void showChooseHeadImage(Activity activity) {
        showChooseImage(activity, PictureMimeType.ofImage(), 1, true, PictureConfig.SINGLE);
    }

    public static void showChooseImage(Activity activity, int type, int count, boolean isCrop, int selectionMode) {
        PictureSelectionModel model = PictureSelector.create(activity)
                .openGallery(type)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(count)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(selectionMode)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .compressMaxKB(200)
                .enableCrop(isCrop)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                //.compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(75)// 裁剪压缩质量 默认90 int
                //.minimumCompressSize(100)// 小于100kb的图片不压缩
                //.synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(300);//视频秒数录制 默认60s int;//结果回调onActivityResult code

        if (type == PictureMimeType.ofVideo()) {
            model.selectionMode(PictureConfig.SINGLE);// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
        }

        model.forResult(type);
    }


    public static void openCamera(Activity activity, boolean isCrop) {
        // 进入相机 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .enableCrop(isCrop)// 是否裁剪 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如 16:9 3:2 3:4 1:1 可自定义
                .imageSpanCount(4)// 每行显示个数
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .circleDimmedLayer(true)
                //.compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(false)// 是否显示拍照按钮
                .compress(true)
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//// / 是否压缩
                .forResult(PictureMimeType.ofImage());//结果回调
    }

    public static void openCamera(Activity activity){
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .forResult(PictureMimeType.ofImage());//结果回调
    }

    public void cleanCache(Activity activity) {
        PictureFileUtils.deleteCacheDirFile(activity);
    }

    public void showVedios(Activity activity, String path) {
        PictureSelector.create(activity).externalPictureVideo(path);
    }


}