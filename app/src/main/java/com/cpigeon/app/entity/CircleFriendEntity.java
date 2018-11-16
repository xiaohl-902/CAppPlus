package com.cpigeon.app.entity;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class CircleFriendEntity {

    /**
     * time : 2018-01-13 16:53:36
     * userinfo : {"username":"pigeonof","nickname":"中鸽网","uid":5473,"headimgurl":"http://www.cpigeon.com/Content/faces/zgw_avatar1_2016012505492613O20DYJWB.jpg"}
     * ismutual : true
     */

    private String time;
    private UserinfoBean userinfo;
    private boolean ismutual;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public boolean isIsmutual() {
        return ismutual;
    }

    public void setIsmutual(boolean ismutual) {
        this.ismutual = ismutual;
    }

    public static class UserinfoBean {
        /**
         * username : pigeonof
         * nickname : 中鸽网
         * uid : 5473
         * headimgurl : http://www.cpigeon.com/Content/faces/zgw_avatar1_2016012505492613O20DYJWB.jpg
         */

        private String username;
        private String nickname;
        private int uid;
        private String headimgurl;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

    }
}
