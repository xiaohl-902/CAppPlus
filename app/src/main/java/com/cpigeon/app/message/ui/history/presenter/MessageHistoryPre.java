package com.cpigeon.app.message.ui.history.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.MessageEntity;
import com.cpigeon.app.message.ui.history.MessageHistoryModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class MessageHistoryPre extends BasePresenter {

    int userId;
    public int date = 1;
    List<MessageEntity> data;

    public MessageHistoryPre(IView mView) {
        super(mView);
        userId = CpigeonData.getInstance().getUserId(getActivity());
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getMessageHistoryList(Consumer<List<MessageEntity>> consumer){
        submitRequestThrowError(MessageHistoryModel.MessageHistory(userId, date).map(r -> {
            if(r.isOk()){
                if(r.isHaveDate()){
                    this.data = r.data;
                }else {
                    this.data = Lists.newArrayList();
                }
                return data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public int getSendMessageCountInDate(){
        int count = 0;
        if(data != null){
            for(MessageEntity messageEntity : data){
                count = count + messageEntity.fscount;
            }
        }
        return count;
    }


}
