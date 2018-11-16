package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.PushCircleMessageModel;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.cpigeon.app.utils.http.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2018/1/20.
 */

public class PushCircleMessagePre extends BasePresenter {

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";

    public static final int TYPE_SHOW_PUBLIC = 0;
    public static final int TYPE_SHOW_FRIEND = 1;
    public static final int TYPE_SHOW_PERSON = 2;

    int userId;
    public String message;
    public List<String> imgs;
    public String video;
    public String messageType = TYPE_TEXT;
    public int showType;
    public String location;
    public double la;
    public double lon;
    public String lo;


    public PushCircleMessagePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }


    public void pushMessage(Consumer<Boolean> consumer) {

        if(!StringValid.isStringValid(message)){
            getError().onNext(getErrorString("请您填写你要发送的内容！"));
            return;
        }

        if(StringValid.isHttp(message)){
            getError().onNext(getErrorString("鸽迷圈是鸽友交流的平台，请勿发布网址和广告！"));
            return;
        }

        if ((imgs == null || imgs.size() == 0) && !StringValid.isStringValid(video)) {
            getError().onNext(getErrorString("请至少选择一张图片或者视频!"));
            return;
        }

        submitRequestThrowError(PushCircleMessageModel.pushMessage(userId, message, location,lo,la, lon, showType, messageType, video, imgs).map(r -> {
            LogUtil.print(r.toJsonString());
            if (r.status) {
                return true;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public Consumer<String> setMessage() {
        return s -> {
            message = s.trim();
        };
    }

    public Consumer<String> setLocation() {
        return s -> {
            if (!s.equals(getString(R.string.string_text_not_show))) {
                location = s.trim();
            }
        };
    }

    public Consumer<String> setShowType() {
        return s -> {
            if (s.equals(getString(R.string.string_circle_message_show_type_public))) {
                showType = TYPE_SHOW_PUBLIC;
            } else if (s.equals(getString(R.string.string_circle_message_show_type_friend))) {
                showType = TYPE_SHOW_FRIEND;
            } else if (s.equals(getString(R.string.string_circle_message_show_type_person))) {
                showType = TYPE_SHOW_PERSON;
            }
        };
    }
}
