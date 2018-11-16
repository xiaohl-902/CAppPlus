package com.cpigeon.app.utils.inputfilter;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.cpigeon.app.modular.matchlive.model.bean.GYTRaceLocation;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.cache.CacheManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zhu TingYu on 2018/4/26.
 */

public class GPSCorrect {
    private static final long CAR_MAX_SPEED = 25;
    private String filterString;
    private boolean isFirst = true;
    TraceLocation weight1 = new TraceLocation();
    List<TraceLocation> w1TempList = Lists.newArrayList();
    List<TraceLocation> w2TempList = Lists.newArrayList();
    List<TraceLocation> mListPoint = Lists.newArrayList();
     TraceLocation weight2;
    private int w1Count = 0;

    public GPSCorrect(){

    }

    public List<LatLng> getPoint(){
        List<LatLng> point = Lists.newArrayList();
        for (int i = 0, len = mListPoint.size(); i < len; i++) {
            TraceLocation traceLocation = mListPoint.get(i);
            LatLng latLng = new LatLng(traceLocation.getLatitude(), traceLocation.getLongitude());
            point.add(latLng);
        }
        return point;
    }
    public Boolean filterPos(GYTRaceLocation raceLocation) {

        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date= new Date(raceLocation.getSj());
        String time= df.format(date);//定位时间
        filterString =time + "开始虑点"+ "\r\n";

        try {
            // 获取的第一个定位点不进行过滤
            if (isFirst) {
                isFirst =false;
                weight1.setLatitude(raceLocation.getWd());
                weight1.setLongitude(raceLocation.getJd());
                weight1.setTime(raceLocation.getSj());
                weight1.setSpeed((float) raceLocation.getSd());

                /****************存储数据到文件中，后面好分析******************/

                filterString +="第一次定位"+ "\r\n";

                /**********************************/

                // 将得到的第一个点存储入w1的缓存集合
                final TraceLocation traceLocation =new TraceLocation();
                traceLocation.setLatitude(raceLocation.getWd());
                traceLocation.setLongitude(raceLocation.getJd());
                traceLocation.setTime(raceLocation.getSj());
                traceLocation.setSpeed((float) raceLocation.getSd());
                w1TempList.add(traceLocation);
                w1Count++;

                return true;

            } else{
                filterString +="非第一次定位"+ "\r\n";
                // 过滤静止时的偏点，在静止时速度小于1米就算做静止状态
                if (raceLocation.getSd() <1) {
                    return false;
                }

                /****************存储数据到文件中，后面好分析******************/
                SimpleDateFormat df1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                Date date1= new Date(raceLocation.getSj());
                String time1= df1.format(date1);// 定位时间
                String testString=time1 + ":" + raceLocation.getSj() +"_"+"," + raceLocation.getWd() +"," + raceLocation.getJd() +"," + raceLocation.getSd() +"\r\n";
                CacheManager.put("tutu_driver_true.txt",testString);

                if (weight2== null) {
                    filterString +="weight2 == null" +"\r\n";

                    // 计算w1与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils= raceLocation.getSj() -weight1.getTime();
                    long offsetTimes = (offsetTimeMils/ 1000);
                    long MaxDistance =offsetTimes * CAR_MAX_SPEED;
                    float distance = AMapUtils.calculateLineDistance(
                            new LatLng(weight1.getLatitude(),weight1.getLongitude()),
                            new LatLng(raceLocation.getWd(),raceLocation.getJd()));

                    filterString +="distance = " +distance + ",MaxDistance =" +MaxDistance  +"\r\n";

                    if (distance> MaxDistance) {
                        filterString +="distance > MaxDistance" +"\r\n";
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 =new TraceLocation();
                        weight2.setLatitude(raceLocation.getWd());
                        weight2.setLongitude(raceLocation.getJd());
                        weight2.setTime(raceLocation.getSj());
                        weight2.setSpeed((float) raceLocation.getSd());

                        w2TempList.add(weight2);

                        return false;

                    } else{
                        filterString +="distance < MaxDistance" +"\r\n";
                        // 将p1加入到做坐标集合w1TempList
                        TraceLocation traceLocation=new TraceLocation();
                        traceLocation.setLatitude(raceLocation.getWd());
                        traceLocation.setLongitude(raceLocation.getJd());
                        traceLocation.setTime(raceLocation.getSj());
                        traceLocation.setSpeed((float) raceLocation.getSd());
                        w1TempList.add(traceLocation);
                        w1Count++;

                        // 更新w1权值点
                        weight1.setLatitude(weight1.getLatitude() * 0.2 + raceLocation.getWd() *0.8);
                        weight1.setLongitude(weight1.getLongitude() * 0.2 + raceLocation.getJd() *0.8);
                        weight1.setTime(raceLocation.getSj());
                        weight1.setSpeed((float) raceLocation.getSd());

                        if (w1TempList.size() >3) {
                            filterString += "d1TempList.size() > 3"+ "\r\n";
                            // 将w1TempList中的数据放入finalList，并将w1TempList清空
                            mListPoint.addAll(w1TempList);
                            w1TempList.clear();
                            return true;
                        } else{
                            filterString += "d1TempList.size() < 3"+ "\r\n";
                            return false;
                        }
                    }

                } else {

                    filterString +="weight2 != null" +"\r\n";
                    // 计算w1与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils= raceLocation.getSj() -weight2.getTime();
                    long offsetTimes = (offsetTimeMils/ 1000);
                    long MaxDistance =offsetTimes *16;
                    float distance =AMapUtils.calculateLineDistance(
                            new LatLng(weight2.getLatitude(),weight2.getLongitude()),
                            new LatLng(raceLocation.getWd(),raceLocation.getJd()));

                    filterString+="distance= " +distance + ",MaxDistance = " +MaxDistance  +"\r\n";

                    if (distance> MaxDistance) {
                        filterString +="distance > MaxDistance" +"\r\n";
                        w2TempList.clear();
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 =new TraceLocation();
                        weight2.setLatitude(raceLocation.getWd());
                        weight2.setLongitude(raceLocation.getJd());
                        weight2.setTime(raceLocation.getSj());
                        weight2.setSpeed((float) raceLocation.getSd());

                        w2TempList.add(weight2);

                        return false;
                    } else{
                        filterString +="distance < MaxDistance" +"\r\n";

                        // 将p1加入到做坐标集合w2TempList
                        TraceLocation traceLocation=new TraceLocation();
                        traceLocation.setLatitude(raceLocation.getWd());
                        traceLocation.setLongitude(raceLocation.getJd());
                        traceLocation.setTime(raceLocation.getSj());
                        traceLocation.setSpeed((float) raceLocation.getSd());
                        w2TempList.add(traceLocation);

                        // 更新w2权值点
                        weight2.setLatitude(weight2.getLatitude() * 0.2 + raceLocation.getWd() *0.8);
                        weight2.setLongitude(weight2.getLongitude() * 0.2 + raceLocation.getJd() *0.8);
                        weight2.setTime(raceLocation.getSj());
                        weight2.setSpeed((float) raceLocation.getSd());

                        if (w2TempList.size() >4) {
                            filterString+="w2TempList.size()> 4" +"\r\n";
                            // 判断w1所代表的定位点数是否>4,小于说明w1之前的点为从一开始就有偏移的点
                            if (w1Count> 4) {
                                filterString += "w1Count > 4" + "\r\n";
                                mListPoint.addAll(w1TempList);
                            } else{
                                filterString += "w1Count < 4" + "\r\n";
                                w1TempList.clear();
                            }

                            // 将w2TempList集合中数据放入finalList中
                            mListPoint.addAll(w2TempList);

                            // 1、清空w2TempList集合 2、更新w1的权值点为w2的值 3、将w2置为null
                            w2TempList.clear();
                            weight1 = weight2;
                            weight2 = null;
                            return true;

                        } else{
                            filterString += "w2TempList.size() > 4"+ "\r\n";
                            return false;
                        }
                    }
                }
            }
        } finally {
            CacheManager.put("tutu_driver_filter.txt",filterString);
//           Log.d("hhh","finnaly");
        }


    }

}
