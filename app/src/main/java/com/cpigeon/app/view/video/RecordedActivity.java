package com.cpigeon.app.view.video;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.DateUtils;
import com.cpigeon.app.utils.GPSFormatUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.cache.CacheManager;
import com.cpigeon.app.utils.http.CommonUitls;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.video.camera.SensorControler;
import com.cpigeon.app.view.video.widget.CameraView;
import com.cpigeon.app.view.video.widget.CircularProgressView;
import com.cpigeon.app.view.video.widget.FocusImageView;


import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;


/**
 * Created by cj on 2017/7/25.
 * desc 视频录制
 * 主要包括 音视频录制、断点续录、对焦等功能
 */

public class RecordedActivity extends Activity implements View.OnClickListener, View.OnTouchListener, SensorControler.CameraFocusListener {

    @BindView(R.id.watermark_gezhu)
    TextView watermarkGeZhu;//鸽主名字
    @BindView(R.id.watermark_time)
    TextView watermarkTime;//水印时间
    @BindView(R.id.watermark_dz)
    TextView watermarkDz;//水印地址

    @BindView(R.id.watermark_llz)
    LinearLayout watermarkLlz;//水印总的布局

    @BindView(R.id.watermark_center_img)
    ImageView waterCenImg;//图片中间水印

    private CameraView mCameraView;
    private CircularProgressView mCapture;
    private FocusImageView mFocus;
    //    private ImageView mBeautyBtn;
//    private ImageView mFilterBtn;
    private ImageView mCameraChange;
    private static final int maxTime = 11000;//最长录制11s
    private boolean pausing = false;
    private boolean recordFlag = false;//是否正在录制

    private int WIDTH = 720, HEIGHT = 1280;

    private long timeStep = 50;//进度条刷新的时间
    long timeCount = 0;//用于记录录制时间
    private boolean autoPausing = false;
    ExecutorService executorService;
    private SensorControler mSensorControler;
    LocationManager locationManager;

    private Unbinder mUnbinder;

    private ImageButton mVideoWc;
    private String savePath;//视频保存路径
    private String type;

    private int cameraTag = 1;

    public static final String TYPE_VIDEO = "video";

    View water;/*
    TextView waterTime;
    TextView waterLocation;*/
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorde_layout);
        mUnbinder = ButterKnife.bind(this);

        executorService = Executors.newSingleThreadExecutor();
        mSensorControler = SensorControler.getInstance();
        mSensorControler.setCameraFocusListener(this);
        locationManager = new LocationManager(getBaseContext());
        initView();
    }

    private void initView() {

        initWater();

        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mCapture = (CircularProgressView) findViewById(R.id.mCapture);
        mFocus = (FocusImageView) findViewById(R.id.focusImageView);


        mCameraView.setOnTouchListener(this);

        type = getIntent().getStringExtra(IntentBuilder.KEY_TYPE);

        if (type.equals("sgt")) {
            watermarkGeZhu.setVisibility(View.VISIBLE);
        }

        if (type.equals("photo") || type.equals("sgt")) {
            photoOperation();//拍照
        } else if (type.equals("video")) {
            videoOperation();//拍摄视频
        }


        //开启线程，持续传递Bitmap,显示水印

        disposable = RxUtils.rollPoling(1, 1000, aLong -> {
            mCameraView.mCameraDrawer.getBitmap.setBitmap(BitmapUtils.getViewBitmap(water), cameraTag);
        });

    }

    private void initWater() {
        water = findViewById(R.id.water_layout);

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

            if (mCameraView.mCamera != null) {
                mCameraView.mCamera.close();
            }

            mCameraView.destroyDrawingCache();
            mUnbinder.unbind();//解除奶油刀绑定
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    //-----------------------------------------------------事件处理（操作）------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera_switch://翻转摄像头
                mCameraView.switchCamera();
                if (mCameraView.getCameraId() == 1) {
                    //前置摄像头 使用美颜
                    mCameraView.changeBeautyLevel(0);
                } else {
                    //后置摄像头不使用美颜
                    mCameraView.changeBeautyLevel(0);
                }
                break;
            case R.id.video_wc://点击录制完成按钮

                recordFlag = false;
                break;
        }
    }


    //视频录制成功返回
    private void recordComplete(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCapture.setProcess(0);

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
                                Intent intent = new Intent();
                                intent.putExtra("video_path", path);
                                setResult(0x00232, intent);

                                RecordedActivity.this.finish();
                            }
                        });
                    }
                }).start();
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
            // savePath = Constants.getPath("record/", time + ".mp4");
            savePath = getCacheDir().getAbsolutePath() + "/" + time + ".mp4";
            LogUtil.print("mp4 : " + savePath);
            try {
                mCameraView.setSavePath(savePath);
                mCameraView.startRecord();
                while (timeCount <= maxTime && recordFlag) {
                    if (pausing || autoPausing) {
                        continue;
                    }
                    mCapture.setProcess((int) timeCount);
                    Thread.sleep(timeStep);
                    timeCount += timeStep;
                }
                recordFlag = false;
                mCameraView.stopRecord();
                if (timeCount < 2000) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RecordedActivity.this, "录像时间太短", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    recordComplete(savePath);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    //-----------------------------------------------------定位相关（不动）------------------------------------------------------------------------
    private String TAG = "RecordedActivity";
    private String TAGA = "RecordedActivitys";


//-----------------------------------------------------视频相关（操作）------------------------------------------------------------------------

    /**
     * 当前页面用于拍摄视频
     */
    private void videoOperation() {

        mCameraChange = (ImageView) findViewById(R.id.btn_camera_switch);//翻转镜头
        mVideoWc = (ImageButton) findViewById(R.id.video_wc);//视频录制完成

        mCameraChange.setOnClickListener(this);
        mVideoWc.setOnClickListener(this);
        mCapture.setTotal(maxTime);


        mCapture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        mVideoWc.setVisibility(View.GONE);//显示录制完成按钮
                        mCameraChange.setVisibility(View.GONE);//隐藏翻转相机

                        if (!recordFlag) {//是否正在录制
                            executorService.execute(recordRunnable);
                        } else {
                            mCameraView.resume(false);
                            pausing = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP://离开
                        mVideoWc.setVisibility(View.VISIBLE);//显示录制完成按钮
                        mCameraChange.setVisibility(View.GONE);//隐藏翻转相机

                        if (!pausing) {
                            mCameraView.pause(false);//暂停
                            pausing = true;
                        }
                        break;

                }

                return false;
            }
        });

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
                float rawY = sRawY * ScreenTool.getScreenWidth(getBaseContext()) / ScreenTool.getScreenHeight(getBaseContext());
                float temp = sRawX;
                float rawX = rawY;
                rawY = (ScreenTool.getScreenWidth(getBaseContext()) - temp) * ScreenTool.getScreenHeight(getBaseContext()) / ScreenTool.getScreenWidth(getBaseContext());

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
        Point point = new Point(ScreenTool.getScreenWidth(getBaseContext()) / 2, ScreenTool.getScreenHeight(getBaseContext()) / 2);
        mCameraView.onFocus(point, callback);
    }


//-----------------------------------------------------图片相关（操作）------------------------------------------------------------------------

    /**
     * 当前页面用于拍照
     */
    private void photoOperation() {
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraView.mCamera.mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                        //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + File.separator + System.currentTimeMillis() + ".jpeg";

                                //图片保存
                                BitmapUtils.saveJPGE_After(RecordedActivity.this, BitmapUtils.createBitmapCenter(BitmapUtils.createBitmapLowerLeft(BitmapUtils.rotaingImageView(90, BitmapFactory.decodeByteArray(data, 0, data.length)), BitmapUtils.convertViewToBitmap(watermarkLlz)), BitmapUtils.convertViewToBitmap(waterCenImg)), img_path, 100);
                                if (type.equals("photo")) {
                                }

                                RecordedActivity.this.finish();

                            }
                        });
                    }
                });
            }
        });
    }
}
