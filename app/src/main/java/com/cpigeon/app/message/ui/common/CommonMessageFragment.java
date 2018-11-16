package com.cpigeon.app.message.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.MultiSelectEntity;
import com.cpigeon.app.message.adapter.CommonMessageAdapter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;

import java.util.ArrayList;


/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class CommonMessageFragment extends BaseMVPFragment<CommonMessageQPre> {


    boolean isSendMessage;

    RelativeLayout bottomRelativeLayout;
    LinearLayout bottomLinearLayout;

    CommonMessageAdapter adapter;
    RecyclerView recyclerView;

    AppCompatImageView bottomIcon;
    TextView bottomText;

    TextView bottomText2;

    @Override
    protected CommonMessageQPre initPresenter() {
        return new CommonMessageQPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_with_button_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        bindError();

        mPresenter.userId = CpigeonData.getInstance().getUserId(getContext());

        isSendMessage = getActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
        setTitle("短语库");
        initView();
        if (!isSendMessage) {
            setToolbarChooseMenu();
            setBottomViewAdd();
        } else {
            toolbar.getMenu().clear();
            toolbar.getMenu().add("添加").setOnMenuItemClickListener(item -> {
                showAddMessageDialog();
                return false;
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            initSelectMessage();
            adapter.setOnCheckboxClickListener(position -> {
                adapter.setSingleItem(adapter.getItem(position), position);
            });
        }

        showLoading();

        bindData();

        setRefreshListener(() -> {
            bindData();
        });
    }

    private void initSelectMessage() {
        bottomLinearLayout.setVisibility(View.GONE);
        bottomText2.setVisibility(View.VISIBLE);
        bottomText2.setText("确定");
        bottomText2.setOnClickListener(v -> {

            if(!adapter.getSelectedPotion().isEmpty()){
                Intent intent = new Intent();
                intent.putExtra(IntentBuilder.KEY_DATA
                        , adapter.getItem(adapter.getSelectedPotion().get(0)).dxnr);
                getActivity().setResult(0, intent);
                finish();
            }else {
                DialogUtils.createHintDialog(getContext(),"请至少选择一个");
            }


        });
    }

    private void setToolbarChooseMenu() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("选择")
                .setOnMenuItemClickListener(item -> {
                    setToolbarCancelMenu();
                    adapter.setImgChooseVisible(true);
                    if (!isSendMessage) {
                        setBottomViewDelete();
                        adapter.setOnCheckboxClickListener(position -> {
                            adapter.setMultiSelectItem(adapter.getItem(position), position);
                        });
                    } else {
                        adapter.setOnCheckboxClickListener(position -> {
                            adapter.setSingleItem(adapter.getItem(position), position);
                        });
                    }
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    private void setToolbarCancelMenu() {
        toolbar.getMenu().clear();
        toolbar.getMenu().add("取消")
                .setOnMenuItemClickListener(item -> {
                    setToolbarChooseMenu();
                    setBottomViewAdd();
                    adapter.setImgChooseVisible(false);
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        adapter = new CommonMessageAdapter();
        adapter.setOnEditClickListener(position -> {
            mPresenter.messageId = adapter.getItem(position).id;
            showEditMessageDialog(getContext(), adapter.getItem(position).dxnr, position);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.bindToRecyclerView(recyclerView);
        if (isSendMessage) {
            adapter.setImgChooseVisible(true);
        }
        addItemDecorationLine(recyclerView);


        bottomRelativeLayout = findViewById(R.id.rl1);
        bottomLinearLayout = findViewById(R.id.ll1);

        bottomIcon = findViewById(R.id.icon);
        bottomText = findViewById(R.id.title);
        bottomText2 = findViewById(R.id.text_btn);

    }


    private void bindData() {
        showLoading();
        mPresenter.getCommonList(commonEntities -> {
            hideLoading();
            adapter.setNewData(commonEntities);
            if(isSendMessage){
                adapter.setImgChooseVisible(true);
            }
        });
    }

    private void setBottomViewAdd() {
        bottomIcon.setBackgroundResource(R.drawable.ic_message_add);
        bottomText.setText("添加短语");
        bottomRelativeLayout.setOnClickListener(v -> {
            showAddMessageDialog();
        });
    }

    private void setBottomViewDelete() {
        bottomIcon.setBackgroundResource(R.drawable.ic_message_delete);
        bottomText.setText("删除短语");
        bottomRelativeLayout.setOnClickListener(v -> {
            DialogUtils.createDialogWithLeft(getContext(), "是否要删除选中的短语"
                    , sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    }
                    , sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        mPresenter.setSelectIds(adapter);
                        mPresenter.deleteMessage(apiResponse -> {
                            if (apiResponse.status) {
                                adapter.deleteChoose();
                            } else {
                                error(apiResponse.msg);
                            }
                        });
                    });
        });

    }

    private void showAddMessageDialog() {
        AlertDialog dialogPrompt = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_message_layout, null);

        TextView title = findViewById(view, R.id.title);
        AppCompatEditText content = findViewById(view, R.id.content);

        TextView btnLeft = findViewById(view, R.id.btn_left);
        TextView btnRight = findViewById(view, R.id.btn_right);


        title.setText("新建短语");
        btnRight.setOnClickListener(v -> {
            mPresenter.addCommonMessage(r -> {
                dialogPrompt.dismiss();
                if (r.status) {
                    showTips("已经成功添加信息", TipType.Dialog);
                    bindData();
                } else {
                    error(r.msg);
                }
            });
        });


        btnLeft.setOnClickListener(v -> {
            dialogPrompt.dismiss();
        });

        bindUi(RxUtils.textChanges(content), mPresenter.setMessageContent());

        dialogPrompt.setView(view);
        dialogPrompt.show();

    }

    private void showEditMessageDialog(Context context, String contentString, int position) {
        AlertDialog dialogPrompt = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_message_layout, null);

        TextView title = view.findViewById(R.id.title);
        AppCompatEditText content = view.findViewById(R.id.content);

        TextView btnLeft = view.findViewById(R.id.btn_left);
        TextView btnRight = view.findViewById(R.id.btn_right);

        content.setText(contentString);
        content.setSelection(contentString.length());
        title.setText("编辑短语");

        btnRight.setOnClickListener(v -> {
            mPresenter.setMessageContent(content.getText().toString());
            mPresenter.modifyMessage(apiResponse -> {
                dialogPrompt.dismiss();
                if (apiResponse.status) {
                    showTips(apiResponse.msg, TipType.Dialog);
                    adapter.getItem(position).dxnr = content.getText().toString();
                    adapter.closeSwipe(position);
                    adapter.notifyItemChanged(position);
                } else {
                    error(apiResponse.msg);
                }
            });
        });


        btnLeft.setOnClickListener(v -> {
            dialogPrompt.dismiss();
        });

        dialogPrompt.setView(view);
        dialogPrompt.show();

    }

}
