package com.cpigeon.app.message.ui.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.ContactsGroupEntity;
import com.cpigeon.app.message.ui.contacts.presenter.TelephoneBookPre;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.message.ui.selectPhoneNumber.SelectPhoneActivity;

import java.util.ArrayList;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class SelectContactsFragment extends BaseContactsListFragment<TelephoneBookPre> {


    public static final int TYPE_SEND_MESSAGE = 1;

    public static final int TYPE_PHONE_SELECT = 2;

    public static final int TYPE_CONTACTS_ADD = 3;
    protected int type;

    @Override
    public void finishCreateView(Bundle state) {
        super.finishCreateView(state);
        type = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_TYPE, 0);
        setTitle("选择群组");

        bottomLinearLayout.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);
        btn.setText("确定");
        btn.setOnClickListener(v -> {
            if (!adapter.getSelectedPotion().isEmpty()) {
                if (type == TYPE_SEND_MESSAGE) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(IntentBuilder.KEY_DATA
                            , (ArrayList<? extends Parcelable>) adapter.getSelectedEntity());
                    getActivity().setResult(0, intent);
                    finish();
                } else if (type == TYPE_PHONE_SELECT) {
                    IntentBuilder.Builder(getSupportActivity(), SelectPhoneActivity.class)
                            .putExtra(IntentBuilder.KEY_DATA, adapter.getItem(adapter.getSelectedPotion().get(0)).fzid)
                            .startActivity();
                    finish();
                } else if (type == TYPE_CONTACTS_ADD) {
                    Intent intent = new Intent();
                    intent.putExtra(IntentBuilder.KEY_DATA, adapter.getSelectedEntity().get(0));
                    getActivity().setResult(0, intent);
                    finish();
                }
            } else {
                showTips("请选择一个分组", TipType.DialogError);
            }
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (type == TYPE_SEND_MESSAGE) {
                adapter.setMultiSelectItem(adapter.getItem(position), position);
            } else {
                adapter.setSingleItem(adapter.getItem(position), position);
            }
        });


    }

    @Override
    protected void bindData() {
        showLoading();
        mPresenter.getContactsGroups(data -> {
            hideLoading();
            if (type != TYPE_SEND_MESSAGE) {
                for (int i = 0; i < data.size();) {
                    if (data.get(i).isSystemGroup()) {
                        data.remove(i);
                        continue;
                    }
                    i++;
                }
            }
            adapter.setNewData(data);
            adapter.setImgChooseVisible(true);
            if(adapter.getFooterLayoutCount() == 0){
                adapter.addFooterView(initFoodView());
            }
        });
    }

    public View initFoodView() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_select_cantacts_foot_layout, recyclerView, false);
        view.setOnClickListener(v -> {
            showAddMessageDialog();
        });
        return view;
    }

    private void showAddMessageDialog() {
        AlertDialog dialogPrompt = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_message_layout, null);

        TextView title = findViewById(view, R.id.title);
        EditText content = findViewById(view, R.id.content);

        TextView btnLeft = findViewById(view, R.id.btn_left);
        TextView btnRight = findViewById(view, R.id.btn_right);

        title.setText("新建群组");

        bindUi(RxUtils.textChanges(content), mPresenter.setGroupName());

        btnLeft.setOnClickListener(v -> {
            dialogPrompt.dismiss();
        });

        btnRight.setOnClickListener(v -> {
            if (mPresenter.groupName.length() < 3) {
                ToastUtil.showLongToast(getContext(), "分组名称长度不能小于3位");
                return;
            }

            mPresenter.addContactsGroups(r -> {
                dialogPrompt.dismiss();
                if (r.status) {
                    bindData();
                    showTips("添加成功", TipType.Dialog);
                } else {
                    error(r.msg);
                }
            });
        });

        dialogPrompt.setView(view);
        dialogPrompt.show();
    }

    @Override
    protected TelephoneBookPre initPresenter() {
        return new TelephoneBookPre(this);
    }
}
