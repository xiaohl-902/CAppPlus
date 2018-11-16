package com.cpigeon.app.entity;

/**
 * Created by Zhu TingYu on 2018/5/26.
 */

public class CircleDetailsReplayEntity {

    public String hfid;//回复内容的ID
    public String hfuid;//回复人的会员ID
    public String hfcontent;//回复内容
    public String hfnickname;//回复人昵称
    public String hfheadimgurl;//回复人头像URL
    public String hftime;

    public HFUserEntity hfuser;
    public HFToUserEntity hftouser;

    public static class HFUserEntity {
        public int hfuid;//回复人会员ID；
        public String hfnickname;//回复人昵称；
        public String hfheadimgurl;//回复人头像URL

    }

    public static class HFToUserEntity {
        public int uid;//回复人会员ID；
        public String nickname;//回复人昵称；
        public String headimgurl;//回复人头像URL

    }

}
