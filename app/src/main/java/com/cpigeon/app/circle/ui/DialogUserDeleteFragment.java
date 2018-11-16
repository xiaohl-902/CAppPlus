package com.cpigeon.app.circle.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.presenter.DeleteMessagePre;
import com.cpigeon.app.circle.presenter.HideMessagePre;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.loc.r;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class DialogUserDeleteFragment extends DialogFragment {
    private OnDialogClickListener listener;
    DeleteMessagePre mPre;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        mPre = new DeleteMessagePre(getActivity());
        mPre.messageId = getArguments().getInt(IntentBuilder.KEY_DATA);
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_user_delete_layout);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.AnimBottomDialog);
        window.setBackgroundDrawableResource(R.drawable.bg_transparent_dailog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 5;
        window.setAttributes(lp);
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        dialog.findViewById(R.id.ll_1).setOnClickListener(v -> {
            mPre.deleteMessage(r -> {
                if(r.status){
                    listener.delete();
                }else {
                    error(r.msg);
                }
            });
        });

        dialog.findViewById(R.id.ll_2).setOnClickListener(v -> {
            mPre.showType = DeleteMessagePre.TYPE_ALL;
            mPre.setShowType(r -> {
                if(r.status){
                    listener.forAll();
                }else {
                    error(r.msg);
                }
            });
            dismiss();

        });

        dialog.findViewById(R.id.ll_3).setOnClickListener(v -> {
            mPre.showType = DeleteMessagePre.TYPE_FRIEND;
            mPre.setShowType(r -> {
                if(r.status){
                    listener.forFriend();
                }else {
                    error(r.msg);
                }
            });
            dismiss();

        });

        dialog.findViewById(R.id.ll_4).setOnClickListener(v -> {
            mPre.showType = DeleteMessagePre.TYPE_SELF;
            mPre.setShowType(r -> {
                if(r.status){
                    listener.forSelf();
                }else {
                    error(r.msg);
                }
            });
            dismiss();

        });

        dialog.findViewById(R.id.ll_5).setOnClickListener(v -> {
            dismiss();
        });
    }

    public interface OnDialogClickListener{
        void delete();
        void forAll();
        void forFriend();
        void forSelf();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void setCircleMessageId(int messageId){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentBuilder.KEY_DATA, messageId);
        setArguments(bundle);
    }

    private void error(String message){
        if(getActivity() != null){
            ((BaseActivity)getActivity()).error(message);
        }
    }
}
