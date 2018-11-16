package com.cpigeon.app.message.ui.sendmessage;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.ContactsGroupEntity;
import com.cpigeon.app.entity.PersonInfoEntity;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.message.ui.common.CommonModel;
import com.cpigeon.app.message.ui.contacts.ContactsModel;
import com.cpigeon.app.message.ui.home.PigeonHomePre;
import com.cpigeon.app.message.ui.home.UserGXTModel;
import com.cpigeon.app.message.ui.modifysign.PersonSignModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringUtil;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/28.
 */

public class SendMessagePre extends BasePresenter {

    int userId;
    public String groupId;

    public String messageContent;
    public int sendCount;
    public String phoneNumber;

    public SendMessagePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getSendNumber(Consumer<String> consumer) {
        submitRequestThrowError(ContactsModel.NumberOfContactsInGroup(userId, groupId).map(r -> {
            if(r.isOk()){
                if(r.isHaveDate()){
                    return r.data.sjhm;
                }else {
                    return "";
                }
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void getPersonSignName(Consumer<String> consumer){
        submitRequestThrowError(PersonSignModel.personSignInfo(userId).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data.qianming;
                }else return StringUtil.emptyString();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void sendMessage(Consumer<ApiResponse> consumer){

        submitRequestThrowError(SendMessageModel.sendMessage(userId,groupId,messageContent, phoneNumber).map(r -> {
            return r;
        }),consumer);
    }

    public void addCommonMessage(Consumer<ApiResponse> consumer){

        if(!StringValid.isStringValid(messageContent)){
            error.onNext(getErrorString("发送的内容为空"));
            return;
        }

        submitRequestThrowError(CommonModel.addCommonMessage(userId, messageContent).map(r ->{
            return r;
        }),consumer);
    }

    public void setGroupIds(List<ContactsGroupEntity> entities){
        if (entities.isEmpty()){
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = entities.size(); i < len; i++) {
            sb.append(entities.get(i).fzid);
            if(i != len - 1){
                sb.append(",");
            }
        }
        groupId = sb.toString();
    }

    public int getContactsCount(List<ContactsGroupEntity> entities){
        int count = 0;
        for (int i = 0, len = entities.size(); i < len; i++) {
            count += entities.get(i).fzcount;
        }
        sendCount = count;
        return count;
    }

    public void getUserInfo(Consumer<UserGXTEntity> consumer) {
        submitRequestThrowError(UserGXTModel.getUserInfo(userId).map(r -> {
            if(r.status){
                return r.data;
            }else throw new HttpErrorException(r);
        }), consumer);
    }
    public void cleanData(){
        groupId = "";
        messageContent = "";
        sendCount = 0;
    }

}
