package com.cpigeon.app.view;

/**
 * Created by Administrator on 2017/3/24.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
public class SingleSelectCenterDialog extends Dialog {

    public List<SelectItem> selectItemList;
    LinearLayout mRootLayout;
    ViewGroup.LayoutParams lp_item_textview;
    ViewGroup.LayoutParams lp_item_splitview;

    private SingleSelectCenterDialog(Context context, List<SelectItem> items) {
        super(context, R.style.comment_dialog);
        if (items == null)
            items = new ArrayList<>();
        this.selectItemList = items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        lp_item_textview = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_item_splitview = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
    }

    private void initView() {
        if (mRootLayout == null)
            mRootLayout = (LinearLayout) findViewById(R.id.layout_dialog);
        mRootLayout.removeAllViews();
        if (selectItemList != null) {

            float scale = getContext().getResources().getDisplayMetrics().density;
            int sp_10 = (int) (10 * scale + 0.5f);
            int sp_15 = (int) (15 * scale + 0.5f);

            for (int i = 0; i < selectItemList.size(); i++) {
                final SelectItem item = selectItemList.get(i);
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(lp_item_textview);
                textView.setBackgroundResource(R.drawable.item_select_bg);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setPadding(sp_10, sp_15, sp_10, sp_15);
                textView.setTextSize(16);
                textView.setTextColor(Color.parseColor("#232323"));
                textView.setText(item.getText());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getOnItemClickListener() != null)
                            item.getOnItemClickListener().onItemClick(SingleSelectCenterDialog.this, item);
                    }
                });
                mRootLayout.addView(textView);
                if (i > selectItemList.size() - 2) continue;

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp_item_splitview);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mRootLayout.addView(imageView);
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(SingleSelectCenterDialog dialog, SelectItem item);
    }

    public static class SelectItem {
        private String text;
        private OnItemClickListener onItemClickListener;

        public SelectItem(String text, OnItemClickListener onItemClickListener) {
            this.text = text;
            this.onItemClickListener = onItemClickListener;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public static class Builder {
        private SingleSelectCenterDialog dialog;
        List<SelectItem> selectItemList;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        private void iniItemtList() {
            if (selectItemList == null) {
                selectItemList = new ArrayList<>();
            }
        }

        public Builder addSelectItem(String text) {
            return this.addSelectItem(new SelectItem(text, null));
        }

        public Builder addSelectItem(String text, OnItemClickListener onItemClickListener) {
            return this.addSelectItem(new SelectItem(text, onItemClickListener));
        }

        public Builder removeSelectItem(String text, int position) {
            this.selectItemList.remove(position);
            return this;
        }

        public Builder addSelectItem(SelectItem item) {
            iniItemtList();
            this.selectItemList.add(item);
            return this;
        }

        public SingleSelectCenterDialog create() {
            iniItemtList();
            dialog = new SingleSelectCenterDialog(context, this.selectItemList);
            return dialog;
        }
    }
}
