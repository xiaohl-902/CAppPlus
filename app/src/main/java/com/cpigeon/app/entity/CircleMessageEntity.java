package com.cpigeon.app.entity;

import android.os.Parcel;

import com.cpigeon.app.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/15.
 */

public class CircleMessageEntity extends SnsEntity {

    /**
     * st : 1
     * picture : [{"sub":"0","size":467635,"id":2063,"url":"http://118.123.244.89:818/uploadfiles/pigeonsCircle/picture/467635_aa7c18792eef74673116487cc2310c31.jpg","thumburl":""}]
     * time : 2018-01-17 19:08:42
     * mid : 8196
     * msg : 开撸
     * video : []
     * from : 手机客户端
     * lo :
     * isAttention : true
     * commentList : [{"time":"2018-01-17 20:25:30","id":555,"content":"😄","user":{"nickname":"我就是我","uid":20251,"headimgurl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg"}},{"time":"2018-01-18 08:13:29","id":557,"content":"厉害了","user":{"nickname":"我就是我","uid":20251,"headimgurl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg"}},{"time":"2018-01-18 10:06:37","id":558,"content":"我公婆人","user":{"nickname":"zg18302810737","uid":24204,"headimgurl":"http://www.cpigeon.com/Content/faces/20171130173847.png"}}]
     * loabs : 北大街
     * userinfo : {"username":"zg18408249304","nickname":"我就是我","uid":20251,"headimgurl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg"}
     * fn : 0
     * tid : 0
     * uid : 20251
     * praiseCount : 2
     * praiseList : [{"nickname":"我就是我","isPraise":1,"uid":20251},{"nickname":"啦啦啦","isPraise":1,"uid":19138}]
     * commentCount : 3
     */

    private int st;
    private String time;
    private int mid;
    private String msg;
    private String from;
    private String lo;
    private boolean isAttention;
    private String loabs;
    private UserinfoBean userinfo;
    private int fn;
    private int tid;
    private int uid;
    private int praiseCount;
    private int commentCount;
    private List<PictureBean> picture;
    private List<CommentListBean> commentList;
    private List<PraiseListBean> praiseList;
    private List<PictureBean> video;

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public boolean isIsAttention() {
        return isAttention;
    }

    public void setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public String getLoabs() {
        return loabs;
    }

    public void setLoabs(String loabs) {
        this.loabs = loabs;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }


    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public List<PraiseListBean> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<PraiseListBean> praiseList) {
        this.praiseList = praiseList;
    }

    public List<PictureBean> getVideo() {
        return video;
    }

    public void setVideo(List<PictureBean> video) {
        this.video = video;
    }


    public static class UserinfoBean implements android.os.Parcelable {
        /**
         * username : zg18408249304
         * nickname : 我就是我
         * uid : 20251
         * headimgurl : http://www.cpigeon.com/Content/faces/20170719112540.jpg
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.username);
            dest.writeString(this.nickname);
            dest.writeInt(this.uid);
            dest.writeString(this.headimgurl);
        }

        public UserinfoBean() {
        }

        protected UserinfoBean(Parcel in) {
            this.username = in.readString();
            this.nickname = in.readString();
            this.uid = in.readInt();
            this.headimgurl = in.readString();
        }

        public static final Creator<UserinfoBean> CREATOR = new Creator<UserinfoBean>() {
            @Override
            public UserinfoBean createFromParcel(Parcel source) {
                return new UserinfoBean(source);
            }

            @Override
            public UserinfoBean[] newArray(int size) {
                return new UserinfoBean[size];
            }
        };
    }

    public static class PictureBean implements android.os.Parcelable {
        /**
         * sub : 0
         * size : 467635
         * id : 2063
         * url : http://118.123.244.89:818/uploadfiles/pigeonsCircle/picture/467635_aa7c18792eef74673116487cc2310c31.jpg
         * thumburl :
         */

        private String sub;
        private int size;
        private int id;
        private String url;
        private String thumburl;
        private String vid;

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumburl() {
            return thumburl;
        }

        public void setThumburl(String thumburl) {
            this.thumburl = thumburl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.sub);
            dest.writeInt(this.size);
            dest.writeInt(this.id);
            dest.writeString(this.url);
            dest.writeString(this.thumburl);
            dest.writeString(this.vid);
        }

        public PictureBean() {
        }

        protected PictureBean(Parcel in) {
            this.sub = in.readString();
            this.size = in.readInt();
            this.id = in.readInt();
            this.url = in.readString();
            this.thumburl = in.readString();
            this.vid = in.readString();
        }

        public static final Creator<PictureBean> CREATOR = new Creator<PictureBean>() {
            @Override
            public PictureBean createFromParcel(Parcel source) {
                return new PictureBean(source);
            }

            @Override
            public PictureBean[] newArray(int size) {
                return new PictureBean[size];
            }
        };
    }

    public static class CommentListBean implements android.os.Parcelable {
        /**
         * time : 2018-01-17 20:25:30
         * id : 555
         * content : 😄
         * user : {"nickname":"我就是我","uid":20251,"headimgurl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg"}
         * touser : {"nickname":"我就是我","uid":20251,"headimgurl":"http://www.cpigeon.com/Content/faces/20170719112540.jpg"}
         */

        private String time;
        private int id;
        private String content;
        private UserBean user;
        private UserBean touser;
        private List<CircleDetailsReplayEntity> hflist;

        public UserBean getTouser() {
            return touser;
        }

        public void setTouser(UserBean touser) {
            this.touser = touser;

        }

        public List<CircleDetailsReplayEntity> getHflist() {
            return hflist;
        }

        public void setHflist(List<CircleDetailsReplayEntity> hflist) {
            this.hflist = hflist;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements android.os.Parcelable {
            /**
             * nickname : 我就是我
             * uid : 20251
             * headimgurl : http://www.cpigeon.com/Content/faces/20170719112540.jpg
             */

            private String nickname;
            private int uid;
            private String headimgurl;

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.nickname);
                dest.writeInt(this.uid);
                dest.writeString(this.headimgurl);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this.nickname = in.readString();
                this.uid = in.readInt();
                this.headimgurl = in.readString();
            }

            public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
                @Override
                public UserBean createFromParcel(Parcel source) {
                    return new UserBean(source);
                }

                @Override
                public UserBean[] newArray(int size) {
                    return new UserBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.time);
            dest.writeInt(this.id);
            dest.writeString(this.content);
            dest.writeParcelable(this.user, flags);
            dest.writeParcelable(this.touser, flags);
        }

        public CommentListBean() {
        }

        protected CommentListBean(Parcel in) {
            this.time = in.readString();
            this.id = in.readInt();
            this.content = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
            this.touser = in.readParcelable(UserBean.class.getClassLoader());
        }

        public static final Creator<CommentListBean> CREATOR = new Creator<CommentListBean>() {
            @Override
            public CommentListBean createFromParcel(Parcel source) {
                return new CommentListBean(source);
            }

            @Override
            public CommentListBean[] newArray(int size) {
                return new CommentListBean[size];
            }
        };
    }

    public static class PraiseListBean implements android.os.Parcelable {
        /**
         * nickname : 我就是我
         * isPraise : 1
         * uid : 20251
         */

        private String nickname;
        private String headimgurl;
        private String praisetime ;
        private int isPraise;
        private int uid;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(int isPraise) {
            this.isPraise = isPraise;
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

        public String getPraisetime() {
            return praisetime;
        }

        public void setPraisetime(String praisetime) {
            this.praisetime = praisetime;
        }

        public PraiseListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.nickname);
            dest.writeString(this.headimgurl);
            dest.writeString(this.praisetime);
            dest.writeInt(this.isPraise);
            dest.writeInt(this.uid);
        }

        protected PraiseListBean(Parcel in) {
            this.nickname = in.readString();
            this.headimgurl = in.readString();
            this.praisetime = in.readString();
            this.isPraise = in.readInt();
            this.uid = in.readInt();
        }

        public static final Creator<PraiseListBean> CREATOR = new Creator<PraiseListBean>() {
            @Override
            public PraiseListBean createFromParcel(Parcel source) {
                return new PraiseListBean(source);
            }

            @Override
            public PraiseListBean[] newArray(int size) {
                return new PraiseListBean[size];
            }
        };
    }

    public CircleMessageEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.st);
        dest.writeString(this.time);
        dest.writeInt(this.mid);
        dest.writeString(this.msg);
        dest.writeString(this.from);
        dest.writeString(this.lo);
        dest.writeByte(this.isAttention ? (byte) 1 : (byte) 0);
        dest.writeString(this.loabs);
        dest.writeParcelable(this.userinfo, flags);
        dest.writeInt(this.fn);
        dest.writeInt(this.tid);
        dest.writeInt(this.uid);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.commentCount);
        dest.writeTypedList(this.picture);
        dest.writeList(this.commentList);
        dest.writeList(this.praiseList);
        dest.writeTypedList(this.video);
    }

    protected CircleMessageEntity(Parcel in) {
        super(in);
        this.st = in.readInt();
        this.time = in.readString();
        this.mid = in.readInt();
        this.msg = in.readString();
        this.from = in.readString();
        this.lo = in.readString();
        this.isAttention = in.readByte() != 0;
        this.loabs = in.readString();
        this.userinfo = in.readParcelable(UserinfoBean.class.getClassLoader());
        this.fn = in.readInt();
        this.tid = in.readInt();
        this.uid = in.readInt();
        this.praiseCount = in.readInt();
        this.commentCount = in.readInt();
        this.picture = in.createTypedArrayList(PictureBean.CREATOR);
        this.commentList = new ArrayList<CommentListBean>();
        in.readList(this.commentList, CommentListBean.class.getClassLoader());
        this.praiseList = new ArrayList<PraiseListBean>();
        in.readList(this.praiseList, PraiseListBean.class.getClassLoader());
        this.video = in.createTypedArrayList(PictureBean.CREATOR);
    }

    public static final Creator<CircleMessageEntity> CREATOR = new Creator<CircleMessageEntity>() {
        @Override
        public CircleMessageEntity createFromParcel(Parcel source) {
            return new CircleMessageEntity(source);
        }

        @Override
        public CircleMessageEntity[] newArray(int size) {
            return new CircleMessageEntity[size];
        }
    };
}
