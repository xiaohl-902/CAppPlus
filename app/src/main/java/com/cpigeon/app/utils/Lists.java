package com.cpigeon.app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chenshuai on 2017/11/7.
 */

public class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {

        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }
    private static int computeArrayListCapacity(int arraySize) {

        return saturatedCast(5L + arraySize + (arraySize / 10));
    }

    private static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    public static <E> boolean isFull(E... objects){
        for(int i = 0, len = objects.length; i < len; i++){
            if(objects[i] == null){
                return false;
            }
        }
        return true;
    }

    public static String appendStringByList(List<String> list){
        StringBuffer sb  = new StringBuffer();
        for (int i = 0, len = list.size(); i < len; i++) {
            sb.append(list.get(i));
            if(i != list.size() - 1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static List<String> split(String s, String split){
        List<String> list = Lists.newArrayList();
        String[] strings = s.split(split);
        for (String string : strings ) {
            list.add(string);

        }
        return list;
    }

}
