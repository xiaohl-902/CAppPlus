package com.cpigeon.app.event;

/**
 * Created by Zhu TingYu on 2018/6/13.
 */

public class MatchLiveRefreshEvent {
    private String type;
    private int matchCount;

    public MatchLiveRefreshEvent(String type, int matchCount){
        this.type = type;
        this.matchCount = matchCount;
    }

    public String getType() {
        return type;
    }

    public int getMatchCount() {
        return matchCount;
    }
}
