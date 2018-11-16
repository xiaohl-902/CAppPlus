package com.cpigeon.app.message.ui.selectPhoneNumber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.event.ContactsEvent;
import com.cpigeon.app.utils.ContactsUtil;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.cpigeon.app.utils.http.GsonUtil;
import com.cpigeon.app.message.ui.selectPhoneNumber.adapter.ContactAdapter;
import com.cpigeon.app.message.ui.selectPhoneNumber.model.ContactModel;
import com.cpigeon.app.message.ui.selectPhoneNumber.pinyin.CharacterParser;
import com.cpigeon.app.message.ui.selectPhoneNumber.pinyin.PinyinComparator;
import com.cpigeon.app.message.ui.selectPhoneNumber.widget.DividerDecoration;
import com.cpigeon.app.message.ui.selectPhoneNumber.widget.TouchableRecyclerView;
import com.cpigeon.app.message.ui.selectPhoneNumber.widget.ZSideBar;

import com.google.gson.reflect.TypeToken;
import com.jiang.android.lib.adapter.expand.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * RecyclerView实现联系人列表
 */
public class SelectPhoneActivity extends BaseActivity<SelectPhonePre> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SEACH_NAME = 1;
    public static final int TYPE_SEACH_NUMBER = 2;

    private ZSideBar mZSideBar;
    private TextView mUserDialog;
    private TouchableRecyclerView mRecyclerView;
    private SearchEditText searchEditText;
    ContactModel mModel;
    private List<ContactModel.MembersEntity> mMembers = new ArrayList<>();
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private ContactAdapter mAdapter;
    private List<ContactModel.MembersEntity> mAllLists = new ArrayList<>();
    private int mPermission;
    ContactsUtil contactsUtil;


    @Override
    public int getLayoutId() {
        return R.layout.activity_select_phone_layout;
    }

    @Override
    public SelectPhonePre initPresenter() {
        return new SelectPhonePre(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("选择手机联系人");
        contactsUtil = new ContactsUtil(getActivity());
        setChooseAll();

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mZSideBar = (ZSideBar) findViewById(R.id.contact_zsidebar);
        mUserDialog = (TextView) findViewById(R.id.contact_dialog);
        mRecyclerView = (TouchableRecyclerView) findViewById(R.id.contact_member);
        searchEditText = findViewById(R.id.widget_title_bar_search);

        searchEditText.setOnSearchClickListener((view, keyword) -> {
            getNetData(TYPE_SEACH_NUMBER, keyword);
        });

        findViewById(R.id.text_btn).setOnClickListener(v -> {
            if(!mAdapter.getPhoneString().isEmpty()){
                showLoading();
                mPresenter.phoneString = mAdapter.getPhoneString();
                mPresenter.putTelephone(apiResponse -> {
                    hideLoading();
                    DialogUtils.createDialog(getActivity(),"提示",apiResponse.msg,"确定",sweetAlertDialog -> {
                        finish();
                        EventBus.getDefault().post(new ContactsEvent());
                    });
                });
            }else {
                showTips("请选择联系人", TipType.DialogError);
            }
        });
//        fillData();
        getNetData(TYPE_ALL, null);
    }

    private void setChooseAll(){
        toolbar.getMenu().clear();
        toolbar.getMenu().add("全选")
                .setOnMenuItemClickListener(item -> {
                    mAdapter.setAllChoose(mMembers, true);
                    setChooseCancel();
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void setChooseCancel(){
        toolbar.getMenu().clear();
        toolbar.getMenu().add("取消")
                .setOnMenuItemClickListener(item -> {
                    mAdapter.setAllChoose(mMembers,false);
                    setChooseAll();
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }


    public void getNetData(int type, @Nullable String keyWord) {

        //id 已经被处理过
       // JsonObject tempData = "{\"groupName\":\"中国\",\"admins\":[{\"id\":\"111221\",\"username\":\"程景瑞\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"钱黛\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"许勤颖\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"孙顺元\",\"profession\":\"teacher\"},{\"id\":\"fcd1feb5db2\",\"username\":\"朱佳\",\"profession\":\"teacher\"},{\"id\":\"bfcd1feb5db2\",\"username\":\"李茂\",\"profession\":\"teacher\"},{\"id\":\"d1feb5db2\",\"username\":\"周莺\",\"profession\":\"teacher\"},{\"id\":\"cd1feb5db2\",\"username\":\"任倩栋\",\"profession\":\"teacher\"},{\"id\":\"d1feb5db2\",\"username\":\"严庆佳\",\"profession\":\"teacher\"}],\"members\":[{\"id\":\"d1feb5db2\",\"username\":\"彭怡1\",\"profession\":\"student\"},{\"id\":\"d1feb5db2\",\"username\":\"方谦\",\"profession\":\"student\"},{\"id\":\"dd2feb5db2\",\"username\":\"谢鸣瑾\",\"profession\":\"student\"},{\"id\":\"dd2478fb5db2\",\"username\":\"孔秋\",\"profession\":\"student\"},{\"id\":\"dd24cd1feb5db2\",\"username\":\"曹莺安\",\"profession\":\"student\"},{\"id\":\"dd2478eb5db2\",\"username\":\"酆有松\",\"profession\":\"student\"},{\"id\":\"dd2478b5db2\",\"username\":\"姜莺岩\",\"profession\":\"student\"},{\"id\":\"dd2eb5db2\",\"username\":\"谢之轮\",\"profession\":\"student\"},{\"id\":\"dd2eb5db2\",\"username\":\"钱固茂\",\"profession\":\"student\"},{\"id\":\"dd2d1feb5db2\",\"username\":\"潘浩\",\"profession\":\"student\"},{\"id\":\"dd24ab5db2\",\"username\":\"花裕彪\",\"profession\":\"student\"},{\"id\":\"dd24ab5db2\",\"username\":\"史厚婉\",\"profession\":\"student\"},{\"id\":\"dd24a00d1feb5db2\",\"username\":\"陶信勤\",\"profession\":\"student\"},{\"id\":\"dd24a5db2\",\"username\":\"水天固\",\"profession\":\"student\"},{\"id\":\"dd24a5db2\",\"username\":\"柳莎婷\",\"profession\":\"student\"},{\"id\":\"dd2d1feb5db2\",\"username\":\"冯茜\",\"profession\":\"student\"},{\"id\":\"dd24a0eb5db2\",\"username\":\"吕言栋\",\"profession\":\"student\"}],\"creater\":{\"id\":\"1\",\"username\":\"褚奇清\",\"profession\":\"teacher\"}}";

        try {
            String data;
            if(type == TYPE_ALL){
                data = contactsUtil.getContactInfo();
            }else {
               data = contactsUtil.searchContacts(keyWord);
            }
            mAllLists = GsonUtil.fromJson(data,new TypeToken<List<ContactModel.MembersEntity>>(){}.getType());
            mModel = new ContactModel();
            mModel.setMembers(mAllLists);
            setUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setUI() {


        seperateLists(mModel);

        if (mAdapter == null) {
            mAdapter = new ContactAdapter(this, mMembers);
            int orientation = LinearLayoutManager.VERTICAL;
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(mAdapter);
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            mRecyclerView.addItemDecoration(headersDecor);
            mRecyclerView.addItemDecoration(new DividerDecoration(this));

            //   setTouchHelper();
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });
        } else {
            mAdapter.addAll(mMembers);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setImgChooseVisible(true);
        mZSideBar.setupWithRecycler(mRecyclerView);

    }

    private void seperateLists(ContactModel mModel) {
        mMembers.clear();
        if (mModel.getMembers() != null && mModel.getMembers().size() > 0) {
            for (int i = 0; i < mModel.getMembers().size(); i++) {
                ContactModel.MembersEntity temp = mModel.getMembers().get(i);
                if(StringValid.isStringValid(temp.getUsername()) && StringValid.isStringValid(temp.getMobile())){
                    ContactModel.MembersEntity entity = temp;
                    String pinyin = characterParser.getSelling(entity.getUsername());
                    String sortString = pinyin.substring(0, 1).toUpperCase();

                    if (sortString.matches("[A-Z]")) {
                        entity.setSortLetters(sortString.toUpperCase());
                    } else {
                        entity.setSortLetters("#");
                    }
                    mMembers.add(entity);
                }
            }
            Collections.sort(mMembers , pinyinComparator);
        }


    }


    public void deleteUser(final int position) {
        mAdapter.remove(mAdapter.getItem(position));
        showToast("删除成功");
    }

    public void showToast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

    }


}
