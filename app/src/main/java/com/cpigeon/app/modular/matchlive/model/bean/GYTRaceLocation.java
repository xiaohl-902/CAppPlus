package com.cpigeon.app.modular.matchlive.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GYTRaceLocation implements Parcelable {
    /**
     * sj : 1497579973
     * tq : {"sj":1497578400,"fl":"5","fx":" 西 南 ","mc":" 多 云","wd":22}
     * sd : 0
     * lid : 31
     * wd : 30.668366
     * jd : 104.032464
     */
    private Long sj;
    private TqBean tq;
    private double sd;
    private int lid;
    private double wd;
    private double jd;
    private double lc;

    public double getLc() {
        return lc;
    }

    public void setLc(double lc) {
        this.lc = lc;
    }

    public Long getSj() {
        return sj;
    }

    public void setSj(Long sj) {
        this.sj = sj;
    }

    public TqBean getTq() {
        return tq;
    }

    public void setTq(TqBean tq) {
        this.tq = tq;
    }

    public double getSd() {
        return sd;
    }

    public void setSd(double sd) {
        this.sd = sd;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public double getWd() {
        return wd;
    }

    public void setWd(double wd) {
        this.wd = wd;
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public static class TqBean implements Parcelable {
        /**
         * sj : 1497578400
         * fl : 5
         * fx :  西 南
         * mc :  多 云
         * wd : 22
         */

        private int sj;
        private String fl;
        private String fx;
        private String mc;
        private int wd;

        public int getSj() {
            return sj;
        }

        public void setSj(int sj) {
            this.sj = sj;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public int getWd() {
            return wd;
        }

        public void setWd(int wd) {
            this.wd = wd;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.sj);
            dest.writeString(this.fl);
            dest.writeString(this.fx);
            dest.writeString(this.mc);
            dest.writeInt(this.wd);
        }

        public TqBean() {
        }

        protected TqBean(Parcel in) {
            this.sj = in.readInt();
            this.fl = in.readString();
            this.fx = in.readString();
            this.mc = in.readString();
            this.wd = in.readInt();
        }

        public static final Creator<TqBean> CREATOR = new Creator<TqBean>() {
            @Override
            public TqBean createFromParcel(Parcel source) {
                return new TqBean(source);
            }

            @Override
            public TqBean[] newArray(int size) {
                return new TqBean[size];
            }
        };

        @Override
        public String toString() {
            return "TqBean{" +
                    "sj=" + sj +
                    ", fl='" + fl + '\'' +
                    ", fx='" + fx + '\'' +
                    ", mc='" + mc + '\'' +
                    ", wd=" + wd +
                    '}';
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.sj);
        dest.writeParcelable(this.tq, flags);
        dest.writeDouble(this.sd);
        dest.writeInt(this.lid);
        dest.writeDouble(this.wd);
        dest.writeDouble(this.jd);
        dest.writeDouble(this.lc);
    }

    public GYTRaceLocation() {
    }

    protected GYTRaceLocation(Parcel in) {
        this.sj = (Long) in.readValue(Long.class.getClassLoader());
        this.tq = in.readParcelable(TqBean.class.getClassLoader());
        this.sd = in.readInt();
        this.lid = in.readInt();
        this.wd = in.readDouble();
        this.jd = in.readDouble();
        this.lc = in.readDouble();
    }

    public static final Parcelable.Creator<GYTRaceLocation> CREATOR = new Parcelable.Creator<GYTRaceLocation>() {
        @Override
        public GYTRaceLocation createFromParcel(Parcel source) {
            return new GYTRaceLocation(source);
        }

        @Override
        public GYTRaceLocation[] newArray(int size) {
            return new GYTRaceLocation[size];
        }
    };

    @Override
    public String toString() {
        return "GYTRaceLocation{" +
                "sj=" + sj +
                ", tq=" + tq +
                ", sd=" + sd +
                ", lid=" + lid +
                ", wd=" + wd +
                ", jd=" + jd +
                ", lc=" + lc +
                '}';
    }
}
