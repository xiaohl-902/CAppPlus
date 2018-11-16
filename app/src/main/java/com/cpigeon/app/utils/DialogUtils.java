package com.cpigeon.app.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class DialogUtils {

    public static void createDialog(Context context, String title, String content, String left) {

        if(content == null){
            return;
        }

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setTitleText(title)
                .setContentText(content)
                .setConfirmText(left).show();
    }

    public static void createDialog(Context context, String title, String content, String right,
                                    SweetAlertDialog.OnSweetClickListener rightListener) {
        if(content == null){
            return;
        }
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setTitleText(title)
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText(right).show();
    }

    public static void createDialog(Context context, String content,
                                    SweetAlertDialog.OnSweetClickListener rightListener) {

        if(content == null){
            return;
        }

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setTitleText("提示")
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static void createDialogWithLeft(Context context, String content,
                                            SweetAlertDialog.OnSweetClickListener rightListener) {

        if(content == null){
            return;
        }
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCanceledOnTouchOutside(false);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText("提示")
                .setCancelText("取消")
                .setCancelClickListener(sweetAlertDialog -> {
                    dialogPrompt.dismissWithAnimation();
                })
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static void createDialogWithLeft(Context context, String content,
                                            SweetAlertDialog.OnSweetClickListener leftListener,
                                            SweetAlertDialog.OnSweetClickListener rightListener) {

        if(content == null){
            return;
        }
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCanceledOnTouchOutside(false);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText("提示")
                .setCancelText("取消")
                .setCancelClickListener(leftListener)
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static SweetAlertDialog createErrorDialog(Context context, String error) {

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialogPrompt.setTitleText("失败");
        dialogPrompt.setContentText(error);
        dialogPrompt.show();
        return dialogPrompt;
    }

    public static SweetAlertDialog createDialog(Context context, String title, String content
            , @Nullable String left, String right, @Nullable SweetAlertDialog.OnSweetClickListener leftListener, @Nullable SweetAlertDialog.OnSweetClickListener rightListener) {


        if(content == null){
            return null;
        }

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setTitleText(title);
        if (left != null) {
            dialogPrompt.setCancelText(left);
        }
        dialogPrompt.setCancelClickListener(leftListener);
        dialogPrompt.setConfirmClickListener(rightListener);
        dialogPrompt.setContentText(content);
        dialogPrompt.setConfirmText(right);
        dialogPrompt.show();

        return dialogPrompt;

    }

    public static SweetAlertDialog createHintDialog(Context context, String content) {

       return createDialog(context, "提示", content
                , null, "确定", null,null);

    }

    public static SweetAlertDialog createHintDialog(Context context, String content, SweetAlertDialog.OnSweetClickListener sureListener) {

       return createDialog(context, "提示", content
                , null, "确定", null,sureListener);

    }
}
