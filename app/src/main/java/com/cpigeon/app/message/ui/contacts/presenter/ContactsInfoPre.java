package com.cpigeon.app.message.ui.contacts.presenter;

import android.app.Activity;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.entity.ContactsEntity;
import com.cpigeon.app.message.ui.contacts.ContactsModel;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class ContactsInfoPre extends BasePresenter{

    int userId;

    public int groupId = 0;

    String name;
    String phoneNumber;
    String remarks;
    public boolean isSystemGroup;

    public ContactsEntity contactsEntity;


    public ContactsInfoPre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        contactsEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        isSystemGroup = getActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void addContacts(Consumer<ApiResponse> consumer){

        if(!StringValid.isStringValid(name)){
            DialogUtils.createHintDialog(getActivity(),"姓名不能为空");
            return;
        }

        if(!StringValid.phoneNumberValid(phoneNumber)){
            DialogUtils.createHintDialog(getActivity(),"手机号码无效");
            return;
        }

        if(groupId == 0){
            DialogUtils.createHintDialog(getActivity(),"请选择分组");
            return;
        }

        submitRequestThrowError(ContactsModel.ContactsAdd(userId,String.valueOf(groupId),phoneNumber,name,remarks).map(r -> {
            return r;
        }),consumer);
    }

    public void modifyContacts(Consumer<String> consumer){

        if(!StringValid.isStringValid(name)){
            DialogUtils.createHintDialog(getActivity(),"姓名不能为空");
            return;
        }

        if(!StringValid.phoneNumberValid(phoneNumber)){
            DialogUtils.createHintDialog(getActivity(),"手机号码无效");
            return;
        }

        if(groupId == 0){
            DialogUtils.createHintDialog(getActivity(),"请选择分组");
            return;
        }

        submitRequestThrowError(ContactsModel.ContactsModify(userId,contactsEntity.id, String.valueOf(groupId),phoneNumber,name,remarks).map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }


    public Consumer<String> setName(){
        return s -> {
          name = s;
        };
    }

    public Consumer<String> setPhoneNumer(){
        return s -> {
            phoneNumber = s;
        };
    }

    public Consumer<String> setRemarks(){
        return s -> {
            remarks = s;
        };
    }

}
