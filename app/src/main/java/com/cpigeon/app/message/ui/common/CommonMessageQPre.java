package com.cpigeon.app.message.ui.common;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.CommonEntity;
import com.cpigeon.app.message.adapter.CommonMessageAdapter;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;


import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class CommonMessageQPre extends BasePresenter{

    public int userId;

    public String messageContent;

    public String messageId;


    public CommonMessageQPre(IView mView) {
        super(mView);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getCommonList(Consumer<List<CommonEntity>> consumer){
        submitRequestThrowError(CommonModel.getCommons(userId).map(r -> {
            if (r.isOk()){
                if(r.isNotDate()){
                    return Lists.newArrayList();
                }else {
                    return r.data;
                }
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void addCommonMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.addCommonMessage(userId, messageContent).map(r ->{
            return r;
        }),consumer);
    }
    public void addCommonMessage1(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.addCommonMessage(userId, messageContent).doOnNext(r -> {
            if(r.status){
                
            }else throw new HttpErrorException(r);
        }),consumer);
    }


    public void modifyMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.modifyCommonMessage(userId, messageId, messageContent).map(r -> {
            return r;
        }),consumer);
    }

    public void deleteMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.deleteCommonMessage(userId, messageId).map(r -> {
            return r;
        }),consumer);
    }

    public Consumer<String> setMessageContent(){
        return s -> {
            messageContent = s;
        };
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setSelectIds(CommonMessageAdapter adapter){
        List<String> id = Lists.newArrayList();
        List<Integer> positions  = adapter.getSelectedPotion();
        for (Integer position : positions) {
            id.add(adapter.getItem(position).id);
        }
        messageId = Lists.appendStringByList(id);
    }

}
