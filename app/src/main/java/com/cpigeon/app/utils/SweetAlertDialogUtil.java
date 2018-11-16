package com.cpigeon.app.utils;

import android.app.Activity;
import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018/2/25.
 */

public class SweetAlertDialogUtil {


    /**
     * 外部传入 dialog  进行初始化   只有确定按钮  可以点返回
     */
    public static SweetAlertDialog showSweetDialog(SweetAlertDialog dialog, Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {
        try {
            dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 外部传入 dialog  进行初始化   只有确定按钮  不可以点返回
     */
    public static SweetAlertDialog showSweetDialogNo(SweetAlertDialog dialog, Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {
        try {
            dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * hl 第三方提示框
     */
    public static SweetAlertDialog showSweetDialog(Activity context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * hl 第三方提示框
     */
    public static SweetAlertDialog showSweetDialog2(SweetAlertDialog dialog, Activity context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (context == null) return null;

            dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText(content);
            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次
     */
    public static SweetAlertDialog showDialog(SweetAlertDialog dialogZ, String errThr, Activity mContext) {

        try {
            if (mContext == null || mContext.isDestroyed()) return null;

            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, dialog -> {
                        dialog.dismiss();
                    });
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, dialog -> {
                        dialog.dismiss();
                    });
                } else {
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, dialog -> {
                        dialog.dismiss();
                    });
                }
            }
        } catch (Exception e) {
            return null;
        }


        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次  不能点返回键　　只有确定
     */
    public static SweetAlertDialog showDialog(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {

            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次  有返回键效果  只有确定
     */
    public static SweetAlertDialog showDialog2(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (mContext == null || mContext.isDestroyed()) return null;

            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialog(dialogZ, mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次  有返回键效果   确定，取消
     */
    public static SweetAlertDialog showDialog3(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialog(mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialog(mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialog(mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次  有返回键效果   确定，取消
     */
    public static SweetAlertDialog showDialog4(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    /**
     * 对于同一个 dialog 只弹出一次  无返回键效果   确定，取消
     */
    public static SweetAlertDialog showDialog5(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {
            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialog2(dialogZ, mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
