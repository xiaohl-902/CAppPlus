package com.cpigeon.app.message.ui.contacts;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.ContactsEntity;
import com.cpigeon.app.entity.ContactsGroupEntity;
import com.cpigeon.app.utils.http.PigeonHttpUtil;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.cpigeon.app.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class ContactsModel {

    public static Observable<ApiResponse<List<ContactsGroupEntity>>> getContactsGroups(int userId) {
        return PigeonHttpUtil.<ApiResponse<List<ContactsGroupEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<ContactsGroupEntity>>>() {
                }.getType())
                .url(R.string.api_contact_group)
                .addParameter("u", userId)
                .request();
    }

    public static Observable<ApiResponse> ContactsGroupsAdd(int userId, String groupName) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_contact_group_add)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzmc", groupName)
                .request();
    }

    public static Observable<ApiResponse> ContactsAdd(int userId, String groupId, String phoneNumber, String name, String remarks) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_contact_add)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzid", groupId)
                .addBody("sjhm", phoneNumber)
                .addBody("xingming", name)
                .addBody("beizhu", remarks)
                .request();
    }

    public static Observable<ApiResponse> ContactsModify(int userId,int contactId, String groupId,
                                                      String phoneNumber, String name, String remarks) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_modify_contacts_info)
                .addQueryString("u", String.valueOf(userId))
                .addBody("id", String.valueOf(contactId))
                .addBody("fzid", groupId)
                .addBody("sjhm", phoneNumber)
                .addBody("xingming", name)
                .addBody("beizhu", remarks)
                .request();
    }

    public static Observable<ApiResponse<List<ContactsEntity>>> ContactsInGroup(int userId, int groupId, int page, String search) {
        return PigeonHttpUtil.<ApiResponse<List<ContactsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<ContactsEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_contacts_in_group)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzid", String.valueOf(groupId))
                .addBody("p", String.valueOf(page))
                .addBody("s", search)
                .request();
    }

    public static Observable<ApiResponse<ContactsEntity>> NumberOfContactsInGroup(int userId, String groupIds) {
        return PigeonHttpUtil.<ApiResponse<ContactsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<ContactsEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_contacts_in_group)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzid", groupIds)
                .addBody("type","s")
                .request();
    }

    public static Observable<ApiResponse> ModifyContactGroupName(int userId, int groupIds, String groupName) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_modify_contacts_group_name)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzid", String.valueOf(groupIds))
                .addBody("fzmc",groupName)
                .request();
    }

    public static Observable<ApiResponse> deleteContactGroup(int userId, int groupIds) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_delete_contacts_group)
                .addQueryString("u", String.valueOf(userId))
                .addBody("fzid", String.valueOf(groupIds))
                .request();
    }

    public static Observable<ApiResponse> deleteContactInGroup(int userId, String contactIds) {
        return PigeonHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_delete_contacts_in_group)
                .addQueryString("u", String.valueOf(userId))
                .addBody("ids", contactIds)
                .request();
    }


}
