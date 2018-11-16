package com.cpigeon.app.entity;

/**
 * Created by Zhu TingYu on 2018/1/19.
 */

public class HideMessageEntity {

    /**
     * time : 2018-01-19 15:16:16
     * msgInfo : {"st":0,"loabs":"","mid":8198,"msg":"测试","time":"2018-01-18 10:42:14","userinfo":{"username":"zg18408249730","nickname":"啦啦啦","uid":19138,"headimgurl":"http://www.cpigeon.com/Content/faces/20170914155311.jpg"},"tid":0,"fn":0,"uid":19138,"from":"手机客户端","lo":""}
     */

    private String time;
    private MsgInfoBean msgInfo;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MsgInfoBean getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(MsgInfoBean msgInfo) {
        this.msgInfo = msgInfo;
    }

    public static class MsgInfoBean {
        /**
         * st : 0
         * loabs :
         * mid : 8198
         * msg : 测试
         * time : 2018-01-18 10:42:14
         * userinfo : {"username":"zg18408249730","nickname":"啦啦啦","uid":19138,"headimgurl":"http://www.cpigeon.com/Content/faces/20170914155311.jpg"}
         * tid : 0
         * fn : 0
         * uid : 19138
         * from : 手机客户端
         * lo :
         */

        private int st;
        private String loabs;
        private int mid;
        private String msg;
        private String time;
        private UserinfoBean userinfo;
        private int tid;
        private int fn;
        private int uid;
        private String from;
        private String lo;

        public int getSt() {
            return st;
        }

        public void setSt(int st) {
            this.st = st;
        }

        public String getLoabs() {
            return loabs;
        }

        public void setLoabs(String loabs) {
            this.loabs = loabs;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

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

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getFn() {
            return fn;
        }

        public void setFn(int fn) {
            this.fn = fn;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getLo() {
            return lo;
        }

        public void setLo(String lo) {
            this.lo = lo;
        }

        public static class UserinfoBean {
            /**
             * username : zg18408249730
             * nickname : 啦啦啦
             * uid : 19138
             * headimgurl : http://www.cpigeon.com/Content/faces/20170914155311.jpg
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
}
