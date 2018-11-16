package com.cpigeon.app.circle.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.presenter.HideMessagePre;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.entity.CircleUserInfoEntity;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.ToastUtil;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class DialogHideCircleFragment extends DialogFragment {
    private HideMessagePre mPre;
    private OnDialogClickListener listener;
    private CircleMessageEntity entity;
    public CircleUserInfoEntity userinfoBean;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        try {
            entity = getArguments().getParcelable(IntentBuilder.KEY_DATA);
        } catch (Exception e) {
            userinfoBean = getArguments().getParcelable(IntentBuilder.KEY_DATA);
        }
        mPre = new HideMessagePre(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_hide_circel_message);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.drawable.bg_transparent_dailog);
        window.setWindowAnimations(R.style.AnimBottomDialog);

        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 5;
        window.setAttributes(lp);
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {

        if(userinfoBean == null){
            dialog.findViewById(R.id.ll_hide_message).setOnClickListener(v -> {
                dismiss();
                mPre.messageId = entity.getMid();
                mPre.setIsHide(true);
                mPre.hideMessage(apiResponse -> {
                    if(apiResponse.status){
                        listener.hideMessage();
                    }else error(apiResponse.msg);
                });
            });

            dialog.findViewById(R.id.ll_hide_his).setOnClickListener(v -> {
                dismiss();
                mPre.hideUserId = entity.getUserinfo().getUid();
                mPre.setIsHide(true);
                mPre.hideUser(apiResponse -> {
                    if(apiResponse.status){
                        listener.hideHisMessage();
                    }else error(apiResponse.msg);
                });
            });

            dialog.findViewById(R.id.ll_black).setOnClickListener(v -> {
                dismiss();
                mPre.blackUserId = entity.getUserinfo().getUid();
                mPre.setIsHide(true);
                mPre.addBlackList(apiResponse -> {
                    if(apiResponse.status){
                        listener.black();
                    }else error(apiResponse.msg);
                });
            });

            dialog.findViewById(R.id.ll_report).setOnClickListener(v -> {
                listener.report();
                dismiss();
            });

        }else {
            dialog.findViewById(R.id.ll_hide_message).setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_report).setVisibility(View.GONE);

            if(userinfoBean.isFollow()){
                dialog.findViewById(R.id.ll_cancel_follow).setVisibility(View.VISIBLE);

                dialog.findViewById(R.id.ll_cancel_follow).setOnClickListener(v -> {
                    if(listener != null){
                        listener.cancelFollow();
                    }
                    dialog.dismiss();
                });
            }


            dialog.findViewById(R.id.ll_hide_his).setOnClickListener(v -> {
                mPre.hideUserId = userinfoBean.id;
                mPre.setIsHide(true);
                mPre.hideUser(apiResponse -> {
                    if(apiResponse.status){
                        listener.hideHisMessage();
                    }else error(apiResponse.msg);
                });
                dismiss();
            });

            dialog.findViewById(R.id.ll_black).setOnClickListener(v -> {
                mPre.blackUserId = userinfoBean.id;
                mPre.setIsHide(true);
                mPre.addBlackList(apiResponse -> {
                    if(apiResponse.status){
                        listener.black();
                    }else error(apiResponse.msg);
                });
                dismiss();
            });
        }

        dialog.findViewById(R.id.ll_cancel).setOnClickListener(v -> {
            dismiss();
        });

    }

    public interface OnDialogClickListener{
         void hideMessage();
         void hideHisMessage();
         void black();
         void report();
         void cancelFollow();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void setCircleMessageEntity(CircleMessageEntity entity){
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentBuilder.KEY_DATA, entity);
        setArguments(bundle);
    }

    public void setUserinfoBean(CircleUserInfoEntity entity){
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentBuilder.KEY_DATA, entity);
        setArguments(bundle);
    }

    private void error(String message){
        if(getActivity() != null){
            ((BaseActivity)getActivity()).error(message);
        }
    }
}
