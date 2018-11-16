package com.cpigeon.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * Created by Administrator on 2017/7/6.
 */

public class DateUtils {

    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }

    public static String millis2String(final long millis, final DateFormat format) {
        return format.format(new Date(millis));
    }


    public static String compareDate(String endTime, String nowTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            now = df.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = df.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day > 365) {
            return day / 365 + "年" + (day - (day / 365) * 365) + "天";
        } else {
            return "" + day + "天";
        }
    }

    /**
     * 小时转化为天数
     */
    public static String hourOfDay(int tims) {
        if (tims > 0 && tims < 24) {//时间小于24小时
            return tims + "小时";
        } else if (tims == 24) {//时间等于24小时
            return "1天";
        } else if (tims > 24) {//时间大于24小时

            int day = tims / 24;//取整
            int hour = tims % 24;//取余
            if (hour == 0) {//整数天
                return day + "天";
            } else {//非整数天
                return day + "天" + hour + "小时";
            }
        }
        return "0小时";
    }

    /**
     * hl 秒转化为天，小时 （有问题）
     */
    public static String misOfDay(int time) {
        long tims = time * 1000;
        long day = tims / (24 * 60 * 60 * 1000);//天数
        long hour = (tims / (60 * 60 * 1000) - day * 24);//小时
        long min = ((tims / (60 * 1000)) - day * 24 * 60 - hour * 60);//分钟
        long s = (tims / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);//毫秒

        if (hour > 0 && hour < 24) {//时间小于24小时

            if (day == 0) {//不足一天
                return hour + "小时";
            } else {//非整数天
                return day + "天" + hour + "小时";
            }
        } else if (hour == 0) {//时间等于0小时

            if (day >= 1) {
                return day + "天";
            }
        }

        return "0小时";
    }


    /**
     * 秒转化为天小时分秒字符串
     *
     * @param seconds
     * @return String
     */
    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }


    /**
     * 秒转化为天小时分秒字符串
     *
     * @param seconds
     * @return String
     */
    public static String formatSecond(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
//            timeStr = min + "分" + second + "秒";
            timeStr = min + "分";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
//                timeStr = hour + "小时" + min + "分" + second + "秒";
                timeStr = hour + "小时";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
//                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                    timeStr = day + "天" + hour + "小时";
                }
            }
        }
        return timeStr;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy年MM月dd日HH时mm分
     */
    public static String getStringDateNow() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     * 时间字符串转换为毫秒数
     *
     * @param strDate
     * @return
     */
    public static long DataToMs(String strDate) {

        long msDate = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(strToDateLong(strDate));
        msDate = c.getTimeInMillis();

        return msDate;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 毫秒转化为时分秒
     *
     * @return
     */
    public static String msToHsm(long ms) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return formatter.format(ms);
    }
    /**
     * 毫秒转化为分秒
     *
     * @return
     */
    public static String msToSm(long ms) {

        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return formatter.format(ms);
    }


    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 传入年月日 时分秒， 获取年月日
     */

    public static String getYMD(String strDate) {
        String[] strs = strDate.split(" ");
        return strs[0];
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(smdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = cal.getTimeInMillis();
        try {
            cal.setTime(sdf.parse(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 将短时间格式时间转换为字符串 获取年
     */
    public static String dateToStrY(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 获取月
     */
    public static String dateToStrM(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     * 将短时间格式时间转换为字符串 获取日
     */
    public static String dateToStrD(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
}
