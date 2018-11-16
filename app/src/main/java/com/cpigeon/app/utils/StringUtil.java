package com.cpigeon.app.utils;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class StringUtil {


    /**
     * 字符串是否为空
     * @param string
     * @return
     */

    public static boolean isStringValid(String string) {
        return string != null && !string.isEmpty() && string.length() > 0;
    }


    public static String getCutString(String s, int start, int end){
        return s.substring(start, end);
    }

    public static String removeAllSpace(String s){
        return s.replaceAll(" +","");
    }

    public static boolean stringIsNumber(String s){
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static String emptyString(){
        return "";
    }

    public static String twoPoint(double price){
        return String.format("%.2f", price);
    }

    public static List<String> splitString(String string, String split){
         return Lists.newArrayList(string.split(split));
    }

    public static String toUpperCase(String s){
        return  s.trim().toUpperCase();
    }

}
