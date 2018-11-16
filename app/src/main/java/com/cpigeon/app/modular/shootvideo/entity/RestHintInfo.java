package com.cpigeon.app.modular.shootvideo.entity;


public class RestHintInfo {
    public int code;
    public String message;
    public boolean cancelable;
    public boolean isClosePage;


    private RestHintInfo(Builder builder) {
        code = builder.code;
        message = builder.message;
        cancelable = builder.cancelable;
        isClosePage = builder.isClosePage;
    }


    public static final class Builder {
        private int code;
        private String message;
        private boolean cancelable = true; //  是否可以点击返回按钮   true  可以，  false  不可以
        private boolean isClosePage = false;//  是否关闭当前页面， true  关闭  false   不关闭

        public Builder() {
        }

        public Builder code(int val) {
            code = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder cancelable(boolean val) {
            cancelable = val;
            return this;
        }

        public Builder isClosePage(boolean val) {
            isClosePage = val;
            return this;
        }

        public RestHintInfo build() {
            return new RestHintInfo(this);
        }
    }
}
