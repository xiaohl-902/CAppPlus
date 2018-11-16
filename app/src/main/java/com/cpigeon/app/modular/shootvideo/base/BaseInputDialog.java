package com.cpigeon.app.modular.shootvideo.base;


import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.shootvideo.util.KeyboardUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.StringUtil;


/**
 * Created by Zhu TingYu on 2018/8/28.
 */

public class BaseInputDialog extends BaseDialogFragment {

    public static final String KEY_CHOOSE_TEXT = "KEY_CHOOSE_TEXT";

    private ImageView mImgClose;
    private TextView mTvTitle;
    protected TextView mTvFinish;
    private EditText mEdContent;
    private TextView mTvChoose;
    private int mEditInputType;
    private String mContent;
    private boolean mIsOpen;

    private int maxLength = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_base_input_fragment;
    }

    @Override
    protected void initView(Dialog dialog) {
        KeyboardUtils.registerSoftInputChangedListener(getActivity(), isOpen -> {
            mIsOpen = isOpen;
        });
        mImgClose = dialog.findViewById(R.id.imgClose);
        mTvTitle = dialog.findViewById(R.id.tvTitle);
        mTvFinish = dialog.findViewById(R.id.tvFinish);
        mEdContent = dialog.findViewById(R.id.edContent);
        mTvChoose = dialog.findViewById(R.id.tvChoose);


        if (getArguments() != null) {
            String title = getArguments().getString(IntentBuilder.KEY_TITLE);
            mEditInputType = getArguments().getInt(IntentBuilder.KEY_TYPE);
            String chooseText = getArguments().getString(KEY_CHOOSE_TEXT);
            mContent = getArguments().getString(IntentBuilder.KEY_DATA);


            try {
                maxLength = getArguments().getInt(IntentBuilder.KEY_DATA_2);//输入文字最大长度
            } catch (Exception e) {
                e.printStackTrace();
            }


            mEdContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            mEdContent.setText(mContent);

            mTvTitle.setText(title);
            if (mEditInputType != 0) {
                mEdContent.setInputType(mEditInputType);
            }
            if (StringUtil.isStringValid(chooseText)) {
                mTvChoose.setText(chooseText);
            }
        }

        mImgClose.setOnClickListener(v -> {
            hide();
        });

        mTvFinish.setOnClickListener(v -> {
            if (mOnFinishListener != null) {
                mOnFinishListener.finish(mEdContent.getText().toString());
                hide();
            }
        });

        if (mOnChooseListener != null) {
            mTvChoose.setVisibility(View.VISIBLE);
            mTvChoose.setOnClickListener(v -> {
                mOnChooseListener.choose();
                dismiss();
            });
        }

        mEdContent.requestFocus();
        KeyboardUtils.toggleSoftInput();

    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setTitle(@StringRes int title) {
        mTvTitle.setText(MyApp.getInstance().getString(title));
    }

    public interface OnFinishListener {
        void finish(String content);
    }

    public interface OnChooseClickListener {
        void choose();
    }

    public EditText getEditText() {
        return mEdContent;
    }

    private OnFinishListener mOnFinishListener;
    private OnChooseClickListener mOnChooseListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
    }

    public void setOnChooseClickListener(OnChooseClickListener onChooseClickListener) {
        mOnChooseListener = onChooseClickListener;
    }

    public void setContent(String content) {
        mEdContent.setText(content);
    }



    public static BaseInputDialog show(FragmentManager fragmentManager
            , String resId, String content, int maxLength, int editInputType, OnFinishListener onFinishListener, @Nullable OnChooseClickListener onChooseClickListener) {
        BaseInputDialog dialog = new BaseInputDialog();
        Bundle bundle = new Bundle();
        bundle.putString(IntentBuilder.KEY_TITLE, resId);
        if (editInputType != 0) {
            bundle.putInt(IntentBuilder.KEY_TYPE, editInputType);
        }

        bundle.putInt(IntentBuilder.KEY_DATA_2, maxLength);


        if (StringUtil.isStringValid(content)) {
            bundle.putString(IntentBuilder.KEY_DATA, content);
        } else {
            bundle.putString(IntentBuilder.KEY_DATA, "");
        }


        dialog.setArguments(bundle);
        dialog.setOnFinishListener(onFinishListener);
        dialog.setOnChooseClickListener(onChooseClickListener);
        dialog.show(fragmentManager);
        return dialog;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mIsOpen) {
            KeyboardUtils.toggleSoftInput();
        }
    }
}
