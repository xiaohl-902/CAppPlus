package com.cpigeon.app.circle;

import com.cpigeon.app.circle.ui.BaseCircleMessageFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.utils.Lists;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Zhu TingYu on 2018/5/25.
 */
public class CircleUpdateManager {

    private static CircleUpdateManager circleUpdateManager;


    private CircleUpdateManager(){
    }


    public class CircleUpdateEvent {

        String currentType;
        String updateType;

        CircleMessageEntity entity;

        CircleUpdateEvent(String currentType, String updateType, CircleMessageEntity entity){
            this.currentType = currentType;
            this.updateType = updateType;
            this.entity = entity;
        }

        public String getCurrentType() {
            return currentType;
        }

        public String getUpdateType() {
            return updateType;
        }

        public CircleMessageEntity getEntity() {
            return entity;
        }
    }

    public class CircleFollowUpdateEvent {

        public static final String TYPE_FOLLOW = "TYPE_FOLLOW";
        public static final String TYPE_CANCEL_FOLLOW = "TYPE_CANCEL_FOLLOW";

        String type;

        CircleMessageEntity entity;

        CircleFollowUpdateEvent(CircleMessageEntity entity, String type){
            this.entity = entity;
            this.type = type;
        }

        public CircleMessageEntity getEntity() {
            return entity;
        }

        public void setEntity(CircleMessageEntity entity) {
            this.entity = entity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class CircleHideMessageEvent {

        public static final String TYPE_HIDE_ONE = "TYPE_HIDE_ONE";
        public static final String TYPE_HIDE_ALL = "TYPE_HIDE_ALL";

        CircleMessageEntity entity;
        int userId;
        String type;

        CircleHideMessageEvent(CircleMessageEntity entity, String type){
            this.type = type;
            this.entity = entity;
        }
        CircleHideMessageEvent(int userId, String type){
            this.type = type;
            this.userId = userId;
        }


        public CircleMessageEntity getEntity() {
            return entity;
        }

        public void setEntity(CircleMessageEntity entity) {
            this.entity = entity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static CircleUpdateManager get(){
        synchronized (CircleUpdateManager.class){
            if(circleUpdateManager == null){
                circleUpdateManager = new CircleUpdateManager();
            }
            return circleUpdateManager;
        }
    }

    public void update(String currentType, List<String>  updateType, CircleMessageEntity entity){
        for(String type : updateType){
            EventBus.getDefault().post(new CircleUpdateEvent(currentType, type, entity));
        }
    }

    public void update(String currentType, String updateType, CircleMessageEntity entity){
        EventBus.getDefault().post(new CircleUpdateEvent(currentType, updateType, entity));
    }

    public void updateAllCircleList(String currentType, CircleMessageEntity entity){
        update(currentType, Lists.newArrayList(BaseCircleMessageFragment.TYPE_ALL
                , BaseCircleMessageFragment.TYPE_FOLLOW, BaseCircleMessageFragment.TYPE_FRIEND
                ,BaseCircleMessageFragment.TYPE_MY), entity);
    }

    public void updateFollow(CircleMessageEntity entity, boolean isFollow){
        if(isFollow){
            EventBus.getDefault().post(new CircleFollowUpdateEvent(entity, CircleFollowUpdateEvent.TYPE_FOLLOW));
        }else {
            EventBus.getDefault().post(new CircleFollowUpdateEvent(entity, CircleFollowUpdateEvent.TYPE_CANCEL_FOLLOW));
        }
    }

    public void hideMessage(CircleMessageEntity entity){
        EventBus.getDefault().post(new CircleHideMessageEvent(entity, CircleHideMessageEvent.TYPE_HIDE_ONE));
    }

    public void hideMessage(int userId){
        EventBus.getDefault().post(new CircleHideMessageEvent(userId, CircleHideMessageEvent.TYPE_HIDE_ALL));
    }

}


