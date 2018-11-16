package com.cpigeon.app.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/3.
 */

public class DateTool {

    //JDK中的日期格式（年-月-日）
    public final static String FORMAT_DATE = "yyyy-MM-dd";

    //JDK中的日期时间格式（年-月-日 时:分:秒）
    public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public final static String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_HH_MM_SS = "HH:mm:ss";

    //只要年份
    public final static String FORMAT_YYYY = "yyyy";

    //年月

    public final static String FORMAT_YYYY_MM = "yyyy-MM";
    public final static String FORMAT_YYYY_MM2 = "yyyy.MM";

    public final static String FORMAT_MM = "MM";
    public final static String FORMAT_DD = "dd";

    /*
* 毫秒转化
*/
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day + "天" : "" + day + "天"; //天
        String strHour = hour < 10 ? "0" + hour + "时" : "" + hour + "时";//小时
        String strMinute = minute < 10 ? "0" + minute + "分" : "" + minute + "分";//分钟
        String strSecond = second < 10 ? "0" + second + "秒" : "" + second + "秒";//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        StringBuffer sb = new StringBuffer();

        if (day != 0) {
            sb.append(strDay);
        }

        if (hour != 0) {
            sb.append(strHour);
        }

        if (minute != 0) {
            sb.append(strMinute);
        }

        sb.append(strSecond);

        return sb.toString();
    }

    //字符串转换为日期
    public static Date strToDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public static String format(long date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(date));
    }

    //字符串转换为日期时间
    public static Date strToDateTime(String strDateTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(strDateTime);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dateTime;
    }

    //字符串转换为日期时间
    public static Date timeStamp2DateTime(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATETIME);
        String d = format.format(timeStamp);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //日期转换为字符串
    public static String dateToStr(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    //日期时间转换为字符串
    public static String dateTimeToStr(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
        String strDateTime = dateTimeFormat.format(date);
        return strDateTime;
    }


    /**
     * 获取今天凌晨00:00的时间戳(毫秒)
     *
     * @return
     */
    public static long getTodayBeginTime() {
        Date currDate = new Date();
        return DateTool.strToDateTime(getDayBeginTimeStr(currDate)).getTime();
    }

    /**
     * 获取日期00:00的时间戳(毫秒)
     *
     * @return
     */
    public static long getDayBeginTime(Date date) {
        return DateTool.strToDateTime(getDayBeginTimeStr(date)).getTime();
    }


    /**
     * 日期转换为字符串 例如：2017-01-01 00:00:00
     *
     * @param date
     * @return
     */
    public static String getDayBeginTimeStr(Date date) {
        return String.format("%04d-%02d-%02d 00:00:00", date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }

    /**
     * 获取友好时间 提示
     *
     * @param time
     * @return
     */
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param time
     * @return
     */
    public static String getTimeFormatText(String time) {
        Date date = strToDateTime(time);
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 已用时间戳
     *
     * @param totalSeconds
     * @return
     */
    public static String getTimeFormatText(long totalSeconds) {

        long diff = totalSeconds * 1000;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String getTimeFormat(long totalSeconds) {
        StringBuilder sbTime = new StringBuilder();
        if (totalSeconds > 3600 * 24) {
            sbTime.append(totalSeconds / (3600 * 24) + "天");
            totalSeconds %= (3600 * 24);
        }
        if (totalSeconds > 3600) {
            sbTime.append(totalSeconds / 3600 + "小时");
            totalSeconds %= 3600;
        }
        if (totalSeconds > 60) {
            sbTime.append(totalSeconds / 60 + "分");
            totalSeconds %= 60;
        }
        if (totalSeconds > 0) {
            sbTime.append(totalSeconds + "秒");
        }
        return sbTime.toString();
    }

    public static String doubleformat(double d, int cout) {
        NumberFormat nf = NumberFormat.getNumberInstance();


        // 保留两位小数
        nf.setMaximumFractionDigits(cout);


        nf.setRoundingMode(RoundingMode.HALF_UP);


        return nf.format(d);
    }

    public static boolean isToday(Date time) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date cDate = new Date(System.currentTimeMillis());
        pre.setTime(cDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isToYear(Date time) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date cDate = new Date(System.currentTimeMillis());
        pre.setTime(cDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    public static boolean isYesterday(Date time) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }


}
