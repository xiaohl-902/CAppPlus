package com.cpigeon.app.modular.shootvideo.viewmodel;//package com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.viewmodel;
//
//import android.arch.lifecycle.LiveData;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.cpigeon.cpigeonhelper.MyApplication;
//
///**
// * 高德定位
// * Created by Zhu TingYu on 2018/3/30.
// */
//
//public class LocationLiveData  {
//
//    private boolean isOnce;
//
//    static LocationLiveData locationLiveData;
//
//    AMapLocationClient mLocationClient;
//    AMapLocationClientOption mLocationOption;
//
//    AMapLocationListener locationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            setValue(aMapLocation);
//        }
//    };
//
//
//
//    public static LocationLiveData get(Boolean isOnce) {
//        synchronized (LocationLiveData.class) {
//            if (locationLiveData == null) {
//                locationLiveData = new LocationLiveData();
//            }
//            locationLiveData.isOnce = isOnce;
//            return locationLiveData;
//        }
//    }
//
//    private LocationLiveData() {
//        onActive();
//    }
//
//
//    protected void onActive() {
//
//        mLocationClient = new AMapLocationClient(MyApplication.getContext());
//        //设置定位回调监听
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
////        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//
////该方法默认为false。
////        mLocationOption.setOnceLocation(true);
//
////获取最近3s内精度最高的一次定位结果：
////设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
////        mLocationOption.setOnceLocationLatest(true);
////下面这句就是设置只定位一次的代码，默认是1秒钟定位一次，此方法在AMAPLocationActivity这个类里面
//
//        mLocationOption.setOnceLocation(isOnce);
//
//
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//
//
//        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
//        //关闭缓存机制
//        mLocationOption.setLocationCacheEnable(false);
//
//
////给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//
//        mLocationClient.setLocationListener(locationListener);
//
//        if(getValue() == null){
//            mLocationClient.startLocation();
//        }
//
//    }
//
//    @Override
//    protected void onInactive() {
//        super.onInactive();
//        mLocationClient.stopLocation();
//        mLocationClient.onDestroy();
//        mLocationClient = null;
//    }
//
//
//}
