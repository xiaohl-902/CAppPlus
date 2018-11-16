package com.cpigeon.app.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.base.FragmentParentActivity;
import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.circle.ui.CircleFriendFragment;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.entity.JPushCircleEntity;
import com.cpigeon.app.event.FansAddEvent;
import com.cpigeon.app.event.JPushEvent;
import com.cpigeon.app.modular.cpigeongroup.view.activity.GroupFriendActivity;
import com.cpigeon.app.modular.guide.view.SplashActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceXunFangActivity;
import com.cpigeon.app.pigeonnews.ui.NewsListFragment;
import com.cpigeon.app.pigeonnews.ui.PigeonNewsActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.http.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by chenshuai on 2017/3/30.
 */

public class JPushBroadcastReceiver extends BroadcastReceiver {
    /**
     * 鸽友圈推送
     */
    private static final String TYPE_CIRCLE_MESSAGE = "circlemsg";
    /**
     * 今日比赛场次统计推送类型
     */
    public static final String TYPE_TODAY_RACE_COUNT = "TodayRaceCount";
    /**
     * 用户比赛关注推送
     */
    public static final String TYPE_USER_RACE_FOLLOW = "UserRaceFollow";

    public static final String TYPE_EARTHQUCK_MESSAGE= "DiZhenXinXi";

    public static final String TYPE_CIRCLE_FOLLOW= "circlegz";





    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
//        Logger.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));
        Logger.d("onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Logger.d("JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Logger.d("接受到推送下来的自定义消息");

            // Push Talk messages are push down by custom message format
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.d("接受到推送下来的通知");

            receivingNotification(context, bundle);
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.d("用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            Logger.d("Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Logger.d(" title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Logger.d("message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d("extras : " + extras);
        JSONObject extrasJson = null;
        try {
            extrasJson = new JSONObject(extras);
            String extraType = extrasJson.optString("type");
            String extraData = extrasJson.getString("data");

            if(extraType.equals(TYPE_CIRCLE_MESSAGE)){
                JPushCircleEntity jPushCircleEntity = GsonUtil.fromJson(extraData
                        , new TypeToken<JPushCircleEntity>(){}.getType());
                jPushCircleEntity.setPushId(bundle.getString(JPushInterface.EXTRA_MSG_ID));
                EventBus.getDefault().post(new JPushEvent<JPushCircleEntity>(jPushCircleEntity
                        , JPushEvent.TYPE_CIRCLE));
            }else if(extraType.equals(TYPE_CIRCLE_FOLLOW)){
                EventBus.getDefault().post(new FansAddEvent());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d("extras : " + extras);
        String extraType = "", extraData = "";
        Intent intent;
        try {
            JSONObject extrasJson = new JSONObject(extras);
            extraType = extrasJson.optString("type");
            extraData = extrasJson.getString("data");
            Logger.d("data : " + extraData);
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (TYPE_CIRCLE_MESSAGE.equals(extraType)){
                JPushCircleEntity jPushCircleEntity = GsonUtil.fromJson(extraData, new TypeToken<JPushCircleEntity>(){}.getType());
                Intent circle = new Intent(context, CircleMessageDetailsNewActivity.class);
                circle.putExtra(IntentBuilder.KEY_DATA, jPushCircleEntity.getMid());
                circle.putExtra(IntentBuilder.KEY_TYPE, BaseCircleMessageFragment.TYPE_ALL);
                Intent[] intents  = {mainIntent, circle};
                context.startActivities(intents);
            }else if(TYPE_EARTHQUCK_MESSAGE.equals(extraType)){
                Intent earth = new Intent(context, PigeonNewsActivity.class);
                earth.putExtra(IntentBuilder.KEY_DATA, 1);
                Intent[] intents  = {mainIntent, earth};
                context.startActivities(intents);
            }
            else if (TYPE_TODAY_RACE_COUNT.equals((extraType))) {
                SharedPreferencesTool.Save(context.getApplicationContext(), MainActivity.APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(context.getApplicationContext()), 1, SharedPreferencesTool.SP_FILE_APPSTATE);
                mainIntent.putExtra(IntentBuilder.KEY_TYPE, TYPE_TODAY_RACE_COUNT);
                Intent[] intents  = {mainIntent};
                context.startActivities(intents);
            } else if (TYPE_USER_RACE_FOLLOW.equals((extraType))) {
                MatchInfo matchInfo = JSON.parseObject(extraData, new TypeReference<MatchInfo>() {
                });
                SharedPreferencesTool.Save(context.getApplicationContext(), MainActivity.APP_STATE_KEY_VIEWPAGER_SELECT_INDEX + CpigeonData.getInstance().getUserId(context.getApplicationContext()), 1, SharedPreferencesTool.SP_FILE_APPSTATE);
                if (matchInfo != null && matchInfo.getRuid() == 0 && !"jg".equals(matchInfo.getDt())) {
                    if (matchInfo.isMatch()) {
                        intent = new Intent(context, RaceReportActivity.class);
                    } else {
                        intent = new Intent(context, RaceXunFangActivity.class);
                    }
                    Bundle bundle1 = new Bundle();                //创建Bundle对象
                    bundle1.putSerializable("matchinfo", matchInfo);     //装入数据
                    bundle1.putString("loadType", matchInfo.getLx());
                    intent.putExtras(bundle1);
                    context.startActivity(intent);
                    return;
                }
            }else if(TYPE_CIRCLE_FOLLOW.equals(extraType)){
                Intent circle = new Intent(context, FragmentParentActivity.class);
                circle.putExtra(FragmentParentActivity.KEY_FRAGMENT, CircleFriendFragment.class);
                circle.putExtra(IntentBuilder.KEY_DATA, 1);
                Intent[] intents  = {mainIntent, circle};
                context.startActivities(intents);
            }

        } catch (Exception e) {
            Logger.w("Unexpected: extras is not a valid json", e);
            return;
        }
        /*MainActivity mainActivity = (MainActivity) AppManager.getAppManager().getActivityByClass(MainActivity.class);
        Intent mIntent = null;
        if (mainActivity == null) {
            mIntent = new Intent(context, SplashActivity.class);
        } else {
            mIntent = new Intent(context, MainActivity.class);
        }
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);*/
    }

    private void processCustomMessage(Context context, Bundle bundle) {

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        if(!StringValid.isStringValid(title) || !StringValid.isStringValid(message)){
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        nm.notify(1, builder.build());

    }

    // When received message, increase unread number for Recent Chat
    private void unreadMessage(final String friend, final String channel) {
//        new Thread() {
//            public void run() {
//                String chattingFriend = null;
//                if (StringUtils.isEmpty(channel)) {
//                    chattingFriend = friend;
//                }
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("udid", Config.udid);
//                params.put("friend", chattingFriend);
//                params.put("channel_name", channel);
//
//                try {
//                    HttpHelper.post(Constants.PATH_UNREAD, params);
//                } catch (Exception e) {
//                    Logger.e(TAG, "Call pushtalk api to report unread error", e);
//                }
//            }
//        }.start();
    }
}
