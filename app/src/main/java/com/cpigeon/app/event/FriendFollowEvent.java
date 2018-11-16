package com.cpigeon.app.event;

/**
 * Created by Zhu TingYu on 2018/6/8.
 */

public class FriendFollowEvent {

    private boolean isFollow;

    public FriendFollowEvent(boolean isFollow){
        this.isFollow = isFollow;
    }

    public boolean isFollow() {
        return isFollow;
    }
}
