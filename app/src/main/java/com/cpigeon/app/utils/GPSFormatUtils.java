package com.cpigeon.app.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.cpigeon.app.utils.http.CommonUitls;

import java.text.DecimalFormat;

import static com.cpigeon.app.utils.DateTool.doubleformat;

/**
 * Created by Administrator on 2017/11/13.
 */

public class GPSFormatUtils {

    public static double Aj2GPSLocation(double ajLocationValue) {
        int du = (int) ajLocationValue;
        double temp = (ajLocationValue - du) * 100;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 100;//秒
        return du + (double) fen / 60 + temp / 3600;
    }

    public static String GPS2AjLocation(double gpsLocationValue) {
        int du = (int) gpsLocationValue;
        double temp = (gpsLocationValue - du) * 60;//需要转换的部分（分）
        int fen = (int) temp;
        temp = (temp - fen) * 60;//秒
        return doubleformat(du + (double) fen / 100 + temp / 10000, 6);
    }

    /**
     * 功能：         度-->度分秒
     *
     * @param d 传入待转化格式的经度或者纬度
     */
    public void DDtoDMS(Double d) {

        String[] array = d.toString().split("[.]");
        String degrees = array[0];//得到度

        Double m = Double.parseDouble("0." + array[1]) * 60;
        String[] array1 = m.toString().split("[.]");
        String minutes = array1[0];//得到分

        Double s = Double.parseDouble("0." + array1[1]) * 60;
        String[] array2 = s.toString().split("[.]");
        String seconds = array2[0];//得到秒
        System.out.println(degrees + "  " + minutes + "  " + seconds);
    }


    /**
     * 功能：         度-->度分秒
     *
     * @param d 传入待转化格式的经度或者纬度
     */
    public static String DtoDMS(Double d) {

        String[] array = d.toString().split("[.]");
        String degrees = array[0];//得到度

        Double m = Double.parseDouble("0." + array[1]) * 60;
        String[] array1 = m.toString().split("[.]");
        String minutes = array1[0];//得到分

        Double s = Double.parseDouble("0." + array1[1]) * 60;
        String[] array2 = s.toString().split("[.]");
        String seconds = array2[0] + "." + new DecimalFormat("#.00").format(array2[1]);//得到秒

        System.out.println(degrees + "  " + minutes + "  " + seconds);

        return degrees + "*" + minutes + "*" + seconds + "*";
    }

    /**
     * 功能：度
     *
     * @param d 传入待转化格式的经度或者纬度
     */
    public static String LoLatoD(double d) {
        return (int) d + "";
    }


    /**
     * 功能：分
     *
     * @param d 传入待转化格式的经度或者纬度
     */
    public static String LoLatoM(double d) {
        int du = (int) d;
        double temp = (d - du) * 60;//需要转换的部分（分）
        int fen = (int) temp;

        if (fen < 10) {
            return "0" + fen;
        } else {
            return "" + fen;
        }
    }

    /**
     * 功能：秒
     *
     * @param d 传入待转化格式的经度或者纬度
     */
    public static String LoLatoS(double d) {
        return GPSFormatUtils.getStrToS(CommonUitls.GPS2AjLocation(d));
    }

    /**
     * 经纬度转化为度分秒
     *
     * @param lola
     * @return
     */
    public static String loLaToDMS(double lola) {
        return GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(lola));
    }


    /**
     * 获取秒获取秒 有小数点
     */
    public static String getStrToS(String str) {

        String s = "";

        String strDms = "";
        strDms = ((str.split("\\."))[0]).toString() + "°";//度

        Log.d("GPSFormatUtils", "strToDMs: " + str);
        String[] srr = (str.split("\\."));

        if (srr.length != 2) {
            return "";
        }

        byte[] strByte = srr[1].getBytes();

        if (strByte != null) {
            switch (strByte.length) {
                case 0:
                    break;
                case 1:
                    strDms = strDms + (strByte[0] - 48) + "'";
                    break;
                case 2:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "'";

                    break;
                case 3:
                    s = "0" + (strByte[2] - 48) + ".00";
                    break;
                case 4:
                    s = (strByte[2] - 48) + "" + (strByte[3] - 48) + ".00";
                    break;

                case 5:
                    s = (strByte[2] - 48) + "" + (strByte[3] - 48) + "." + (strByte[4] - 48) + "0";
                    break;
                case 6:
                    s = (strByte[2] - 48) + "" + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "";
                    break;
                default:
                    s = (strByte[2] - 48) + "" + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "";
            }
        }
        return s;
    }

    /**
     * 通过字符串获取 度
     */
    public static String getStrToD(String str) {
        String strDms = "";
        strDms = ((str.split("\\."))[0]).toString();//度
        return strDms;
    }

    /**
     * 通过字符串 获取 分
     */
    public static String getStrToM(String str) {
        String strDms = "";
        Log.d("GPSFormatUtils", "strToDMs: " + str);
        String[] srr = (str.split("\\."));

        if (srr.length != 2) {
            return "";
        }

        byte[] strByte = srr[1].getBytes();

        if (strByte != null) {
            switch (strByte.length) {
                case 0:
                    break;
                case 1:
                    strDms = "" + (strByte[0] - 48);
                    break;
                default:
                    strDms = "" + (strByte[0] - 48) + (strByte[1] - 48);
            }
        }
        return strDms;
    }


    /**
     * 经纬度转化为字符串度分秒
     */
    public static String loLaToDMSString(double lola) {
        return GPSFormatUtils.LoLatoD(lola) + "." + GPSFormatUtils.LoLatoM(lola) + GPSFormatUtils.LoLatoS(lola);
    }

    /**
     * 功能：  度-->度分秒（满足图片格式）
     *
     * @param d 传入待转化格式的经度或者纬度
     * @return
     */
    public static String DDtoDMS_photo(Double d) {

        String[] array = d.toString().split("[.]");
        String D = array[0];//得到度

        Double m = Double.parseDouble("0." + array[1]) * 60;
        String[] array1 = m.toString().split("[.]");
        String M = array1[0];//得到分

        Double s = Double.parseDouble("0." + array1[1]) * 60 * 10000;
        String[] array2 = s.toString().split("[.]");
        String S = array2[0];//得到秒
        return D + "/1," + M + "/1," + S + "/10000";
    }


    /**
     * 通过字符串，获取度分秒
     */
    public static String strToDMs(String str) {

        String strDms = "";
        strDms = ((str.split("\\."))[0]).toString() + "°";//度

        Log.d("GPSFormatUtils", "strToDMs: " + str);
        String[] srr = (str.split("\\."));

        if (srr.length != 2) {
            return str + "°";
        }

        byte[] strByte = srr[1].getBytes();

        if (strByte != null) {
            switch (strByte.length) {
                case 0:
                    break;
                case 1:
                    strDms = strDms + "0" + (strByte[0] - 48) + "\'00.00\"";
                    break;
                case 2:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + "'00.00\"";

                    break;
                case 3:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'0" + (strByte[2] - 48) + ".00\"";
                    break;
                case 4:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + ".00\"";
                    break;

                case 5:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + "0\"";
                    break;
                case 6:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "\"";
                    break;
                default:
                    strDms = strDms + (strByte[0] - 48) + (strByte[1] - 48) + "\'" + (strByte[2] - 48) + (strByte[3] - 48) + "." + (strByte[4] - 48) + (strByte[5] - 48) + "\"";
            }
        }
        return strDms;
    }


    private static String TAG = "GPSFormatUtils";

    /**
     * 两个文本监听方法
     *
     * @param etLo 输入经度
     * @param etLa 输入纬度
     */
    public static void textChangedListener(Context context, EditText etLo, EditText etLa) {

        //输入经度文本监听
        etLo.addTextChangedListener(new TextWatcher() {

            String strTag = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s);

                String str = s.toString();

                if (str.isEmpty()) {
                    return;
                }

                double d = Double.parseDouble(str);

                if (str.length() == 4 && str.indexOf(".") == -1) {
                    String zh = str.substring(str.length() - 1, str.length());//最后一个字符

                    etLo.setText(str.substring(0, str.length() - 1) + "." + zh);
                    etLo.setSelection(etLo.getText().toString().length());//光标移动到最后的位置
                    return;
                }

                if (d > 180) {
                    CommonUitls.showSweetAlertDialog(context, "温馨提示", "经度格式错误，经度不能超度180");
                    return;
                }

                if (str.indexOf(".") != -1) {
                    //包含小数点
                    String[] srr = (str.split("\\."));

                    if (srr.length != 2) {//字符小数点前后都有值
                        return;
                    }
                    String xsd = srr[1];//小数点后面的字符

                    Log.d(TAG, "xsd: " + xsd);
                    if (xsd.length() >= 2) {//小数点后两位
                        if (xsd.length() == 2) {//小数点两位
                            if (Double.parseDouble(xsd) >= 60) {
                                CommonUitls.showSweetAlertDialog(context, "温馨提示", "经度格式错误，分不能超过60");
                                etLo.setText(str.substring(0, str.length() - 1));
                                etLo.setSelection(etLo.getText().toString().length());//光标移动到最后的位置
                                return;
                            }
                        } else if (xsd.length() == 4) {//小数点四位
                            String m = xsd.substring(2, 4);
                            Log.d(TAG, "m: " + m);
                            if (Double.parseDouble(m) >= 60) {
                                CommonUitls.showSweetAlertDialog(context, "温馨提示", "经度格式错误，秒不能超过60");
                                etLo.setText(str.substring(0, str.length() - 1));
                                etLo.setSelection(etLo.getText().toString().length());//光标移动到最后的位置
                                return;
                            }
                        } else if (xsd.length() > 6) {
                            CommonUitls.showSweetAlertDialog(context, "温馨提示", "小数最多为6位");
                            etLo.setText(str.substring(0, str.length() - 1));
                            etLo.setSelection(etLo.getText().toString().length());//光标移动到最后的位置
                            return;
                        }
                    }
                }
            }
        });

        //输入纬度文本监听
        etLa.addTextChangedListener(new TextWatcher() {

            String strTag = "";//标签，用来保存上一次的输入数字的值


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();

                if (str.isEmpty()) {
                    return;
                }
                double d = Double.parseDouble(str);

                if (str.length() == 3 && str.indexOf(".") == -1) {
                    String zh = str.substring(str.length() - 1, str.length());//最后一个字符

                    etLa.setText(str.substring(0, str.length() - 1) + "." + zh);
                    etLa.setSelection(etLa.getText().toString().length());//光标移动到最后的位置
                    return;
                }

                if (d > 90) {
                    CommonUitls.showSweetAlertDialog(context, "温馨提示", "纬度格式错误，纬度不能超过90");
                    return;
                }

                if (str.indexOf(".") != -1) {
                    //包含小数点
                    String[] srr = (str.split("\\."));

                    if (srr.length != 2) {//字符小数点前后都有值
                        return;
                    }

                    String xsd = srr[1];//小数点后面的字符
                    Log.d(TAG, "xsd: " + xsd);
                    if (xsd.length() >= 2) {//小数点后两位
                        if (xsd.length() == 2) {//小数点两位
                            if (Double.parseDouble(xsd) >= 60) {
                                CommonUitls.showSweetAlertDialog(context, "温馨提示", "纬度格式错误，分不能超过60");
                                etLa.setText(str.substring(0, str.length() - 1));
                                etLa.setSelection(etLa.getText().toString().length());//光标移动到最后的位置
                                return;
                            }
                        } else if (xsd.length() == 4) {//小数点四位
                            String m = xsd.substring(2, 4);
                            Log.d(TAG, "m: " + m);
                            if (Double.parseDouble(m) >= 60) {
                                CommonUitls.showSweetAlertDialog(context, "温馨提示", "纬度格式错误，秒不能超过60");
                                etLa.setText(str.substring(0, str.length() - 1));
                                etLa.setSelection(etLa.getText().toString().length());//光标移动到最后的位置
                                return;
                            }
                        } else if (xsd.length() > 6) {
                            CommonUitls.showSweetAlertDialog(context, "温馨提示", "小数最多为6位");
                            etLa.setText(str.substring(0, str.length() - 1));
                            etLa.setSelection(etLa.getText().toString().length());//光标移动到最后的位置
                            return;
                        }
                    }
                }
            }
        });
    }
}