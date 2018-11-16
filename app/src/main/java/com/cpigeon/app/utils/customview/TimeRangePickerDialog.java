package com.cpigeon.app.utils.customview;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cpigeon.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/7/10.
 */

public class TimeRangePickerDialog extends DialogFragment {

    OnTimeRangeSelectedListener onTimeRangeSelectedListener;
    @BindView(R.id.pv_start_hour)
    PickerView pvStartHour;
    @BindView(R.id.pv_start_minute)
    PickerView pvStartMinute;
    @BindView(R.id.pv_end_hour)
    PickerView pvEndHour;
    @BindView(R.id.pv_end_minute)
    PickerView pvEndMinute;
    Unbinder unbinder;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnOk)
    Button btnOk;

    int startHour, startMinute, endHour, endMinute;

    public void setStartHour(int startHour) {
        this.startHour = startHour;
        if (pvStartHour != null)
            pvStartHour.setSelected(String.format("%02d", startHour));
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
        if (pvStartMinute != null)
            pvStartMinute.setSelected(String.format("%02d", startMinute));
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
        if (pvEndHour != null)
            pvEndHour.setSelected(String.format("%02d", endHour));
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
        if (pvEndMinute != null)
            pvEndMinute.setSelected(String.format("%02d", endMinute));
    }

    PickerView.onSelectListener onSelectListener = new PickerView.onSelectListener() {
        @Override
        public void onSelect(PickerView view, String text) {
            switch (view.getId()) {
                case R.id.pv_start_hour:
                    startHour = Integer.valueOf(text);
                    break;
                case R.id.pv_start_minute:
                    startMinute = Integer.valueOf(text);
                    break;
                case R.id.pv_end_hour:
                    endHour = Integer.valueOf(text);
                    break;
                case R.id.pv_end_minute:
                    endMinute = Integer.valueOf(text);
                    break;
            }
        }
    };

    public static TimeRangePickerDialog newInstance(OnTimeRangeSelectedListener callback) {
        TimeRangePickerDialog ret = new TimeRangePickerDialog();
        ret.onTimeRangeSelectedListener = callback;
        return ret;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnCancel, R.id.btnOk})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.btnCancel:
                break;
            case R.id.btnOk:
                if (onTimeRangeSelectedListener != null)
                    onTimeRangeSelectedListener.onTimeRangeSelected(startHour, startMinute, endHour, endMinute);
                break;
        }

    }

    public interface OnTimeRangeSelectedListener {
        void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin);
    }

    public void setOnTimeRangeSetListener(OnTimeRangeSelectedListener callback) {
        this.onTimeRangeSelectedListener = callback;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_timerange_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        unbinder = ButterKnife.bind(this, root);
        List<String> hours = new ArrayList<>();
        List<String> minutes = new ArrayList<>();
        List<String> hours1 = new ArrayList<>();
        List<String> minutes1 = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
            hours1.add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format("%02d", i));
            minutes1.add(String.format("%02d", i));
        }

        pvStartHour.setData(hours);
        pvStartMinute.setData(minutes);
        pvEndHour.setData(hours1);
        pvEndMinute.setData(minutes1);

        pvStartHour.setSelected(String.format("%02d", startHour));
        pvStartMinute.setSelected(String.format("%02d", startMinute));
        pvEndHour.setSelected(String.format("%02d", endHour));
        pvEndMinute.setSelected(String.format("%02d", endMinute));

        pvStartHour.setOnSelectListener(onSelectListener);
        pvEndHour.setOnSelectListener(onSelectListener);
        pvStartMinute.setOnSelectListener(onSelectListener);
        pvEndMinute.setOnSelectListener(onSelectListener);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}