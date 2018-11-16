package com.cpigeon.app.modular.usercenter.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.usercenter.model.bean.UserFollow;
import com.cpigeon.app.modular.usercenter.model.dao.IUserFollowDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by chenshuai on 2017/6/28.
 */

public class UserFollowDaoImpl implements IUserFollowDao {

    @Override
    public void getUserRaceFollows(int pageIndex, int pageSize, String type, final OnCompleteListener<List<UserFollow>> onCompleteListener) {
        CallAPI.getUserRaceFollows(MyApp.getInstance(), pageIndex, pageSize, type, new CallAPI.Callback<List<UserFollow>>() {
            @Override
            public void onSuccess(List<UserFollow> data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("获取失败");
            }
        });
    }

    @Override
    public void getAllUserRaceFollows(int cacheTime,final OnCompleteListener<List<UserFollow>> onCompleteListener) {
        CallAPI.getUserRaceFollows(MyApp.getInstance(), -1, 0, "all", new CallAPI.Callback<List<UserFollow>>() {
            @Override
            public void onSuccess(List<UserFollow> data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("获取失败");
            }
        });
    }

    @Override
    public void addUserRaceFollow(String ssid, String followType, String displayName, final OnCompleteListener<UserFollow> onCompleteListener) {
        CallAPI.addUserRaceFollows(MyApp.getInstance(), ssid, followType, displayName, new CallAPI.Callback<UserFollow>() {
            @Override
            public void onSuccess(UserFollow data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("关注失败");
            }
        });
    }

    @Override
    public void removeUserRaceFollow(int followId, String ssid, String followType, final OnCompleteListener<Integer> onCompleteListener) {
        CallAPI.removeUserRaceFollows(MyApp.getInstance(), followId, ssid, followType, new CallAPI.Callback<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                if (onCompleteListener != null) onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null) onCompleteListener.onFail("关注失败");
            }
        });
    }
}
