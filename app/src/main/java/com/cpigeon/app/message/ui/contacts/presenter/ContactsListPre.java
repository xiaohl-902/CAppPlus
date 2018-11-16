package com.cpigeon.app.message.ui.contacts.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.entity.ContactsEntity;
import com.cpigeon.app.entity.ContactsGroupEntity;
import com.cpigeon.app.message.adapter.ContactsInfoAdapter;
import com.cpigeon.app.message.ui.contacts.ContactsModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class ContactsListPre extends BasePresenter {
    int userId;
    int groupId;
    public int page = 1;
    String searchString;
    public ContactsGroupEntity contactsGroupEntity;
    public String groupName;
    String contactsIds;

    public ContactsListPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        contactsGroupEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        groupId = contactsGroupEntity.fzid;
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getContactsInGroup(Consumer<List<ContactsEntity>> consumer){

        submitRequestThrowError(ContactsModel.ContactsInGroup(userId,groupId,page,searchString).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);

    }

    public void modifyGroupName(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.ModifyContactGroupName(userId, groupId, groupName).map(apiResponse -> {
            return apiResponse;
        }),consumer);
    }

    public void deleteGroup(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.deleteContactGroup(userId, groupId),consumer);
    }

    public void deleteContacts(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.deleteContactInGroup(userId, contactsIds),consumer);
    }



    public Consumer<String> setSearchString(){
        return s -> {
            searchString = s;
        };
    }

    public Consumer<String> setGroupName(){
        return s -> {
            groupName = s;
        };
    }

    public void setSelectIds(ContactsInfoAdapter adapter){
        List<String> id = Lists.newArrayList();
        List<Integer> positions  = adapter.getSelectedPotion();
        for (Integer position : positions) {
            id.add(String.valueOf(adapter.getItem(position).id));
        }
        contactsIds = Lists.appendStringByList(id);
    }

}
