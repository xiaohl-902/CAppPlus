package com.cpigeon.app.modular.shootvideo;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;

import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.modular.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DateUtils;
import com.cpigeon.app.utils.GPSFormatUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.StringUtil;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.view.video.Constants;
import com.cpigeon.app.view.video.camera.SensorControler;
import com.cpigeon.app.view.video.widget.CameraView;
import com.cpigeon.app.view.video.widget.FocusImageView;


import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cj on 2017/7/25.
 * 随行拍
 * type = "video"   拍摄视频
 * type = "photo"   拍摄图片
 */

public class AtAnyTimeShootingActivity extends AppCompatActivity implements View.OnTouchListener, SensorControler.CameraFocusListener {

    @BindView(R.id.watermark_z)
    RelativeLayout watermark_z;//水印总布局
    @BindView(R.id.water_tv_name)
    TextView water_tv_name;
    @BindView(R.id.water_tv_time)
    TextView watermarkTime;//水印时间
    @BindView(R.id.water_tv_lo)
    TextView water_tv_lo;//经度
    @BindView(R.id.water_tv_la)
    TextView water_tv_la;//纬度
    @BindView(R.id.water_tv_address)
    TextView water_tv_address;//地址
    @BindView(R.id.water_tv_altitude)
    TextView water_tv_altitude;//海拔

    @BindView(R.id.btn_video_record)
    FrameLayout btn_video_record;//视频录制

    @BindView(R.id.imgbtn_ture)
    ImageButton imgbtn_ture;//拍照确定]
    @BindView(R.id.btn_click_start)
    ImageView btn_click_start;//
    @BindView(R.id.btn_type_video)
    TextView btn_type_video;//
    @BindView(R.id.btn_type_photo)
    TextView btn_type_photo;

    @BindView(R.id.btn_cen)
    RelativeLayout btn_cen;

    @BindView(R.id.imgbtn_false)
    ImageButton imgbtn_false;//拍照取消
    @BindView(R.id.water_tv_ad)
    TextView water_tv_ad;//

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.img_logo)
    CircleImageView img_logo;

    private CameraView mCameraView;
    private View mCapture;
    private FocusImageView mFocus;
    private static int maxTime = 11000;//最长录制11s
    private boolean pausing = false;
    private boolean recordFlag = false;//是否正在录制


    private long timeStep = 50;//进度条刷新的时间
    long timeCount = 0;//用于记录录制时间
    private boolean autoPausing = false;
    ExecutorService executorService;
    private SensorControler mSensorControler;

    private Unbinder mUnbinder;

    private String savePath;//视频保存路径
    private String type = "video";

    private Timer mTimer;

    private boolean isBitmap = true;
    private LocationManager mLocationManager;

    protected void initObserve() {
        mLocationManager = new LocationManager(this);
        mLocationManager.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                try {
                    water_tv_lo.setText(GPSFormatUtils.loLaToDMS(aMapLocation.getLongitude()) + " E");//"经度：" +
                    water_tv_la.setText(GPSFormatUtils.loLaToDMS(aMapLocation.getLatitude()) + "N");//"纬度："
                    water_tv_address.setText(aMapLocation.getPoiName() + aMapLocation.getCity() + aMapLocation.getDistrict());
                    water_tv_ad.setText(aMapLocation.getCountry() + " " + aMapLocation.getCity());

                    //获取海拔高度
                    android.location.LocationManager GpsManager = (android.location.LocationManager) AtAnyTimeShootingActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(AtAnyTimeShootingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AtAnyTimeShootingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    Location location = GpsManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        water_tv_altitude.setText("海拔 " + CommonUitls.strTwo(location.getAltitude()) + "米");
                    } else {
                        water_tv_altitude.setText("海拔 0 米");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mLocationManager.star();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_any_time_shooting);
        mUnbinder = ButterKnife.bind(this);
        executorService = Executors.newSingleThreadExecutor();
        mSensorControler = SensorControler.getInstance();
        mSensorControler.setCameraFocusListener(this);
        dialogFragment = new LocalShareDialogFragment();

        ShootInfoEntity mShootInfoEntity = getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        try {
            if (StringUtil.isStringValid(mShootInfoEntity.getImgurl())) {
                img_logo.setVisibility(View.VISIBLE);
                Glide.with(this).load(mShootInfoEntity.getImgurl()).into(img_logo);
            } else {
                img_logo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (StringUtil.isStringValid(mShootInfoEntity.getSszz())) {
                water_tv_name.setVisibility(View.VISIBLE);
                water_tv_name.setText(mShootInfoEntity.getSszz());
            } else {
                water_tv_name.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        initView();
        initObserve();

    }


    private void initView() {

        mCameraView = findViewById(R.id.camera_view);
        mCapture = findViewById(R.id.mCapture);
        mFocus = findViewById(R.id.focusImageView);

        mCameraView.setOnTouchListener(this);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //时间
                        if (isBitmap) {
                            if (watermarkTime != null && watermark_z != null) {
                                watermarkTime.setText(DateUtils.sdf.format(new Date()));
                                mCameraView.mCameraDrawer.getBitmap.setBitmap(BitmapUtils.getViewBitmap(watermark_z), cameraTag);
                            }
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    //-----------------------------------------------------生命周期（不动）------------------------------------------------------------------------
    @Override
    protected void onResume() {
        try {
            mCameraView.onResume();
            cameraTag = 1;

            if (recordFlag && autoPausing) {
                mCameraView.resume(true);
                autoPausing = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            cameraTag = 2;

            if (recordFlag && !pausing) {
                mCameraView.pause(true);
                autoPausing = true;
            }
            mCameraView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    private boolean isStop = false;

    @Override
    protected void onDestroy() {
        try {
            isStop = true;
            mTimer.cancel();

            if (mCameraView.mCamera != null) {
                mCameraView.mCamera.close();
            }

            if (mLocationManager.getAMapLocationClient() != null) {
                mLocationManager.getAMapLocationClient().stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationManager.getAMapLocationClient().onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            }

            mCameraView.destroyDrawingCache();
            mUnbinder.unbind();//解除奶油刀绑定
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    //-----------------------------------------------------事件处理（操作）------------------------------------------------------------------------

    @OnClick({R.id.imgbtn_ture, R.id.imgbtn_false, R.id.btn_cen, R.id.btn_type_video, R.id.btn_type_photo, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_ture://确定

                if (type.equals("video")) {
                    recordFlag = false;
                    recordComplete(savePath);
                    initBtn();

                } else {
                    showShareDialog(2);
                }
                break;
            case R.id.imgbtn_false://取消
                initBtn();
                break;

            case R.id.btn_cen:
                switch (type) {
                    case "video":
                        //拍摄视频
                        videoOperation();
                        break;
                    case "photo":
                        photoOperation();
                        break;
                    default:

                }
                break;
            case R.id.btn_type_video:
                //拍摄视频

                if (recordFlag) {
                    CommonUitls.showToast(this, "当前正在录制！请结束后继续");
                    return;
                }

                type = "video";
                btn_type_video.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_type_photo.setTextColor(getResources().getColor(R.color.color_text_cancel));

                initBtn2();

                break;
            case R.id.btn_type_photo:
                //拍摄照片
                if (recordFlag) {
                    CommonUitls.showToast(this, "当前正在录制！请结束后继续");
                    return;
                }

                type = "photo";
                btn_type_video.setTextColor(getResources().getColor(R.color.color_text_cancel));
                btn_type_photo.setTextColor(getResources().getColor(R.color.colorPrimary));

                initBtn2();

                break;

            case R.id.img_back:
                this.finish();
                break;
        }
    }

    private void initBtn() {

        try {
            if (mTimer != null) {
                mTimer.cancel();
            }

            btn_click_start.setVisibility(View.VISIBLE);//开始按钮显示
            btn_video_record.setVisibility(View.GONE);//录像隐藏
            imgbtn_false.setVisibility(View.GONE);//取消按钮隐藏
            imgbtn_ture.setVisibility(View.GONE);//确定按钮隐藏

            btn_cen.setVisibility(View.VISIBLE);
            isShootComplete = false;
            mCameraView.onResume();
            mCameraView.resume(true);
            cameraTag = 1;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //时间

                            if (isBitmap) {
                                if (watermarkTime != null && watermark_z != null) {
                                    watermarkTime.setText(DateUtils.sdf.format(new Date()));
                                    mCameraView.mCameraDrawer.getBitmap.setBitmap(BitmapUtils.getViewBitmap(watermark_z), cameraTag);
                                }
                            }
                        }
                    });
                }
            }, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initBtn2() {
        btn_click_start.setVisibility(View.VISIBLE);//开始按钮显示
        btn_video_record.setVisibility(View.GONE);//录像隐藏
        imgbtn_false.setVisibility(View.GONE);//取消按钮隐藏
        imgbtn_ture.setVisibility(View.GONE);//确定按钮隐藏

        btn_cen.setVisibility(View.VISIBLE);
        isShootComplete = false;
        cameraTag = 1;
    }


    private LocalShareDialogFragment dialogFragment;

    public void showShareDialog(int tag) {
        if (dialogFragment != null && dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()) {
            dialogFragment.getDialog().dismiss();
            dialogFragment.dismiss();
        }

        String error = "";
        if (tag == 1) {
            //视频分享
            error = "是否将该视频分享给鸽友";
        } else if (tag == 2) {
            //图片分享
            error = "是否将该图片分享给鸽友";
        }


        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);

        dialogPrompt.setTitleText("温馨提示");
        dialogPrompt.setContentText(error);
        dialogPrompt.setConfirmText("确定");
        dialogPrompt.setCancelText("取消");

        dialogPrompt.setConfirmClickListener(dialog -> {
            dialog.dismiss();
            initBtn();

            if (dialogFragment != null) {
                dialogFragment.setLocalFilePath(savePath);
                dialogFragment.setFileType(type);
                dialogFragment.show(this.getFragmentManager(), "share");
            }
        });

        dialogPrompt.setCancelClickListener(sweetAlertDialog -> {
            initBtn();
            sweetAlertDialog.dismiss();
        });
        dialogPrompt.setCancelable(true);
        dialogPrompt.show();

    }


    /**
     * 当前页面用于拍照
     */
    private boolean isShootComplete = false;

    private void photoOperation() {
        btn_cen.setVisibility(View.INVISIBLE);
        mCameraView.mCamera.mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cameraTag = 2;
                        isShootComplete = true;

                        btn_click_start.setVisibility(View.GONE);//开始按钮
                        btn_video_record.setVisibility(View.GONE);//录像
                        imgbtn_false.setVisibility(View.VISIBLE);//取消按钮
                        imgbtn_ture.setVisibility(View.VISIBLE);//确定按钮


                        savePath = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + File.separator + System.currentTimeMillis() + ".jpeg";

                        //图片保存
                        BitmapUtils.saveJPGE_After(AtAnyTimeShootingActivity.this,
                                BitmapUtils.createBitmapCenter2(
                                        BitmapUtils.rotaingImageView(90, BitmapFactory.decodeByteArray(data, 0, data.length)),
                                        BitmapUtils.getViewBitmap(watermark_z))
                                , savePath, 100);


                        RxUtils.delayed(2000, aLong -> {
                            Uri uri = Uri.fromFile(new File(savePath));
                            AtAnyTimeShootingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        });

                    }
                });
            }
        });

    }


//-----------------------------------------------------线程相关------------------------------------------------------------------------

    /**
     * 视频录制相关线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            recordFlag = true;
            pausing = false;
            autoPausing = false;
            timeCount = 0;
            long time = System.currentTimeMillis();

//            savePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + "/" + time + ".mp4";
            savePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator + "/" + time + ".mp4";
//            savePath = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + File.separator + System.currentTimeMillis() + ".mp4";

            try {
                mCameraView.setSavePath(savePath);
                mCameraView.startRecord();
                while (recordFlag) {
                    if (pausing || autoPausing) {
                        continue;
                    }
                    Thread.sleep(timeStep);git

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_time.setText(DateUtils.msToSm(timeCount));
                        }
                    });
                    timeCount += timeStep;
                }
                Log.d("xiaohls", "run: 1");
                recordFlag = false;
                mCameraView.stopRecord();
                if (timeCount < 2000) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initBtn();
                            Toast.makeText(AtAnyTimeShootingActivity.this, "录像时间太短", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_click_start.setVisibility(View.GONE);//开始按钮
                            btn_video_record.setVisibility(View.GONE);//录像
                            imgbtn_false.setVisibility(View.VISIBLE);//取消按钮
                            imgbtn_ture.setVisibility(View.VISIBLE);//确定按钮

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    //视频录制成功返回
    private void recordComplete(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    showShareDialog(1);

                                    RxUtils.delayed(2000, aLong -> {
                                        Uri uri = Uri.fromFile(new File(savePath));
                                        AtAnyTimeShootingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private int cameraTag = 1;

//-----------------------------------------------------视频相关（操作）------------------------------------------------------------------------

    /**
     * 当前页面用于拍摄视频
     */
    private void videoOperation() {

        try {
            if (recordFlag) {//是否正在录制

                mCameraView.resume(false);
                pausing = false;
                recordFlag = false;

                btn_click_start.setVisibility(View.GONE);//开始按钮
                btn_video_record.setVisibility(View.GONE);//录像
                imgbtn_false.setVisibility(View.VISIBLE);//取消按钮
                imgbtn_ture.setVisibility(View.VISIBLE);//确定按钮

            } else {
                btn_click_start.setVisibility(View.GONE);//开始按钮
                btn_video_record.setVisibility(View.VISIBLE);//录像
                imgbtn_false.setVisibility(View.GONE);//取消按钮
                imgbtn_ture.setVisibility(View.GONE);//确定按钮

                executorService.execute(recordRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mCameraView.getCameraId() == 1) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float sRawX = event.getRawX();
                float sRawY = event.getRawY();
                float rawY = sRawY * MyApp.screenWidth / MyApp.screenHeight;
                float temp = sRawX;
                float rawX = rawY;
                rawY = (MyApp.screenWidth - temp) * MyApp.screenHeight / MyApp.screenWidth;

                Point point = new Point((int) rawX, (int) rawY);
                mCameraView.onFocus(point, callback);
                mFocus.startFocus(new Point((int) sRawX, (int) sRawY));
        }
        return true;
    }

    Camera.AutoFocusCallback callback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //聚焦之后根据结果修改图片
            Log.e("hero", "----onAutoFocus====" + success);
            if (success) {
                mFocus.onFocusSuccess();
            } else {
                //聚焦失败显示的图片
                mFocus.onFocusFailed();
            }
        }
    };

    @Override
    public void onFocus() {
        if (mCameraView.getCameraId() == 1) {
            return;
        }

        Point point = new Point(MyApp.screenWidth / 2, MyApp.screenHeight / 2);
        mCameraView.onFocus(point, callback);
    }
}
