package com.cpigeon.app.modular.settings.view.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.customview.TimeRangePickerDialog;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by chenshuai on 2017/7/10.
 * 推送设置
 */

public class SettingPushActivity extends BaseActivity {
    public static final String SETTING_KEY_PUSH_ENABLE = "push-enable";
    public static final String SETTING_KEY_PUSH_NOTIFICATION = "push-notification";
    public static final String SETTING_KEY_PUSH_SILENCE_START = "push_silence_start";
    public static final String SETTING_KEY_PUSH_SILENCE_END = "push-silence_end";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sb_push_enable)
    SwitchButton sbPushEnable;
    @BindView(R.id.sb_push_shock)
    SwitchButton sbPushShock;

    @BindView(R.id.sb_push_sound)
    SwitchButton sbPushSound;
    @BindView(R.id.tv_push_enable)
    TextView tvPushEnable;
    @BindView(R.id.tv_tip_push_enable)
    TextView tvTipPushEnable;
    @BindView(R.id.tv_push_sound)
    TextView tvPushSound;
    @BindView(R.id.tv_tip_push_sound)
    TextView tvTipPushSound;
    @BindView(R.id.tv_push_shock)
    TextView tvPushShock;
    @BindView(R.id.tv_tip_push_shock)
    TextView tvTipPushShock;
    @BindView(R.id.aciv_enter)
    AppCompatImageView acivEnter;
    @BindView(R.id.tv_push_silence)
    TextView tvPushSilence;
    @BindView(R.id.tv_tip_push_silence)
    TextView tvTipPushSilence;
    @BindView(R.id.rl_push_silence)
    RelativeLayout rlPushSilence;

    int startHour, startMinute, endHour, endMinute;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.sb_push_enable:
                    SharedPreferencesTool.Save(mContext, SETTING_KEY_PUSH_ENABLE, isChecked, SharedPreferencesTool.SP_FILE_APPSETTING);
                    if (isChecked) {
                        JPushInterface.resumePush(mContext.getApplicationContext());
                    } else {
                        JPushInterface.stopPush(mContext.getApplicationContext());
                    }
                    showTips(isChecked ? "已开启推送" : "已关闭推送", TipType.ToastShort);
                    break;
                case R.id.sb_push_sound:
                case R.id.sb_push_shock:
                    BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
                    //builder.statusBarDrawable = R.drawable.jpush_notification_icon;
                    //builder.notificationFlags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
                    builder.notificationDefaults = 0;

                    if (sbPushShock.isChecked()) {
                        builder.notificationDefaults |= Notification.DEFAULT_VIBRATE;
                    }
                    if (sbPushSound.isChecked()) {
                        builder.notificationDefaults |= Notification.DEFAULT_SOUND;
                    }
                    SharedPreferencesTool.Save(mContext, SETTING_KEY_PUSH_NOTIFICATION, builder.notificationDefaults, SharedPreferencesTool.SP_FILE_APPSETTING);
                    JPushInterface.setDefaultPushNotificationBuilder(builder);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_push;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolbar.setTitle(getResources().getString(R.string.Push_Notifications));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    public void initData() {
        boolean pushArgs = SharedPreferencesTool.Get(mContext, SETTING_KEY_PUSH_ENABLE, true, SharedPreferencesTool.SP_FILE_APPSETTING);
        sbPushEnable.setChecked(pushArgs);

        int pushNotification = SharedPreferencesTool.Get(mContext, SETTING_KEY_PUSH_NOTIFICATION, ~0, SharedPreferencesTool.SP_FILE_APPSETTING);
        sbPushShock.setChecked(pushNotification == ~0 || (pushNotification & Notification.DEFAULT_VIBRATE) == Notification.DEFAULT_VIBRATE);
        sbPushSound.setChecked(pushNotification == ~0 || (pushNotification & Notification.DEFAULT_SOUND) == Notification.DEFAULT_SOUND);

        sbPushEnable.setOnCheckedChangeListener(onCheckedChangeListener);
        sbPushShock.setOnCheckedChangeListener(onCheckedChangeListener);
        sbPushSound.setOnCheckedChangeListener(onCheckedChangeListener);
        int startTime = SharedPreferencesTool.Get(mContext, SETTING_KEY_PUSH_SILENCE_START, 0, SharedPreferencesTool.SP_FILE_APPSETTING);
        int endTime = SharedPreferencesTool.Get(mContext, SETTING_KEY_PUSH_SILENCE_END, 0, SharedPreferencesTool.SP_FILE_APPSETTING);
        startHour = startTime / 60;
        startMinute = startTime % 60;
        endHour = endTime / 60;
        endMinute = endTime % 60;
        refreshPushSilence();
    }

    @SuppressLint("DefaultLocale")
    private void refreshPushSilence() {
        if (startHour == 0 && startMinute == 0 && endHour == 0 && endMinute == 0) {
            tvTipPushSilence.setText("已关闭");
        } else
            tvTipPushSilence.setText(String.format("%02d:%02d ~ %02d:%02d", startHour, startMinute, endHour, endMinute));
        JPushInterface.setSilenceTime(mContext.getApplicationContext(), startHour, startMinute, endHour, endMinute);
    }

    @OnClick(R.id.rl_push_silence)
    public void onViewClicked() {
        //时间选择器
        final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(new TimeRangePickerDialog.OnTimeRangeSelectedListener() {
            @Override
            public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
                SettingPushActivity.this.startHour = startHour;
                SettingPushActivity.this.startMinute = startMin;
                SettingPushActivity.this.endHour = endHour;
                SettingPushActivity.this.endMinute = endMin;
                refreshPushSilence();
                SharedPreferencesTool.Save(mContext, SETTING_KEY_PUSH_SILENCE_START, startHour * 60 + startMin, SharedPreferencesTool.SP_FILE_APPSETTING);
                SharedPreferencesTool.Save(mContext, SETTING_KEY_PUSH_SILENCE_END, endHour * 60 + endMin, SharedPreferencesTool.SP_FILE_APPSETTING);
            }
        });
        timePickerDialog.setStartHour(startHour);
        timePickerDialog.setStartMinute(startMinute);
        timePickerDialog.setEndHour(endHour);
        timePickerDialog.setEndMinute(endMinute);
        timePickerDialog.show(getFragmentManager(), "123");
    }
}
