package com.cpigeon.app.modular.matchlive.model.bean;

/**
 * Created by Administrator on 2017/6/15.
 */

public class RaceImageOrVideo {
        /**
         * fid : 3
         * url : 文件请求地址24
         * thumburl : 缩略图请求地址
         * type : 视频
         * size : 12355441
         * time : 2017-06-14 15:38:31
         * tag : 标签名称
         * longitude : 105.203065
         * latitude : 40.203065
         * weartherName :  晴
         * windPower : 4
         * windDir : 西南
         * temperature : 30
         */

        private int fid;
        private String url;
        private String thumburl;
        private String type;
        private int size;
        private String time;
        private String tag;
        private double longitude;
        private double latitude;
        private String weartherName;
        private String windPower;
        private String windDir;
        private int temperature;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getWeartherName() {
            return weartherName;
        }

        public void setWeartherName(String weartherName) {
            this.weartherName = weartherName;
        }

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

}
