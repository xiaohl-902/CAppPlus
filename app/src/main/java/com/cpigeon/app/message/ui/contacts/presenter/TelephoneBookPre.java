package com.cpigeon.app.message.ui.contacts.presenter;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.ContactsGroupEntity;
import com.cpigeon.app.message.ui.contacts.ContactsModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class TelephoneBookPre extends BasePresenter {

    int userId;

    public String groupName;

    public TelephoneBookPre(IView mView) {
        super(mView);
        userId = CpigeonData.getInstance().getUserId(MyApp.getInstance().getBaseContext());
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getContactsGroups(Consumer<List<ContactsGroupEntity>> consumer){
        submitRequestThrowError(ContactsModel.getContactsGroups(userId).map(r -> {
            if(r.isOk()){
                if(r.isNotDate()){
                    return Lists.newArrayList();
                }else {
                    return r.data;
                }
            }else {
                throw new HttpErrorException(r);
            }
        }),consumer);
    }

    public void addContactsGroups(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.ContactsGroupsAdd(userId, groupName).map(r ->{
                return  r;
        }),consumer);
    }

    public Consumer<String> setGroupName(){
        return s -> {
            groupName = s;
        };
    }

}
