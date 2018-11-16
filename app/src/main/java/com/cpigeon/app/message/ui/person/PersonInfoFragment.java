package com.cpigeon.app.message.ui.person;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.IdCardNInfoEntity;
import com.cpigeon.app.entity.IdCardPInfoEntity;
import com.cpigeon.app.entity.PersonInfoEntity;
import com.cpigeon.app.event.PersonInfoEvent;
import com.cpigeon.app.message.adapter.PersonImageInfoAdapter;
import com.cpigeon.app.message.ui.idCard.IdCardCameraActivity;
import com.cpigeon.app.message.ui.modifysign.PersonSignPre;
import com.cpigeon.app.message.ui.order.ui.OrderPayFragment;
import com.cpigeon.app.utils.BitmapUtil;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.FileTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.http.GsonUtil;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.utils.inputfilter.PhotoUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class PersonInfoFragment extends BaseMVPFragment<PersonSignPre> {


    public static final int TYPE_LOOK = 0;
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_UPLOAD_INFO = 2;

    public static final String TYPE_UPLOAD_INFO_HAVE_DATE = "TYPE_UPLOAD_INFO_HAVE_DATE";

    private int PHOTO_SUCCESS_REQUEST = 2083;

    int type;

    RecyclerView recyclerView;
    PersonImageInfoAdapter adapter;

    AppCompatEditText edName;
    AppCompatEditText edNumber;
    AppCompatEditText edWork;
    AppCompatEditText edSign;
    TextView hint;
    String hintMessage;

    PersonInfoEntity entity;

    List<String> imgs;

    boolean uploadInfoHaveDate;

    @Override
    protected PersonSignPre initPresenter() {
        return new PersonSignPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        uploadInfoHaveDate = getActivity().getIntent().getBooleanExtra(TYPE_UPLOAD_INFO_HAVE_DATE, false);
        type = mPresenter.type;
        hintMessage = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA);
        imgs = Lists.newArrayList("idCard_P", "idCard_N", "license");
        hideSoftInput();
        if (type == TYPE_LOOK) {
            setTitle("个人信息");
            getPersonInfo();
        } else if (type == TYPE_EDIT) {
            setTitle("编辑个人信息");
            entity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        } else if (type == TYPE_UPLOAD_INFO) {
            if (uploadInfoHaveDate) {
                setTitle("修改个人信息");
            } else {
                setTitle("提交个人信息");
            }
        }

        initView();
    }


    private void getPersonInfo() {
        showLoading();
        mPresenter.getPersonInfo(r -> {
            hideLoading();
            if (r.status) {
                entity = r.data;
                mPresenter.idCardP = FileTool.byte2File(entity.sfzzm, getContext().getCacheDir().getPath(), imgs.get(0)).getPath();
                mPresenter.idCardN = FileTool.byte2File(entity.sfzbm, getContext().getCacheDir().getPath(), imgs.get(1)).getPath();
                mPresenter.license = FileTool.byte2File(entity.yyzz, getContext().getCacheDir().getPath(), imgs.get(2)).getPath();
                bindData();
            } else {
                error(r.msg);
            }
        });
    }

    private void bindData() {
        adapter.setNewData(imgs);
        edName.setText(StringValid.isStringValid(entity.xingming) ? entity.xingming : "无");
        edNumber.setText(StringValid.isStringValid(entity.sjhm) ? entity.sjhm : "无");
        edWork.setText(StringValid.isStringValid(entity.dwmc) ? entity.dwmc : "无");
        if (edSign != null) {
            edSign.setText(StringValid.isStringValid(entity.qianming) ? entity.qianming : "无");
        }
        /*mPresenter.idCardN = imgs.get(0);
        mPresenter.idCardP = imgs.get(1);
        mPresenter.license = imgs.get(2);*/
    }

    private void initView() {

        findViewById(R.id.ll1).setVisibility(View.GONE);

        TextView btn = findViewById(R.id.text_btn);
        btn.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PersonImageInfoAdapter(getContext());
        adapter.bindToRecyclerView(recyclerView);
        adapter.addHeaderView(initHeadView());

        if (type == TYPE_LOOK) {
            adapter.setNewData(Lists.newArrayList("", "", ""));
            btn.setText("编辑");
            btn.setOnClickListener(v -> {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_TYPE, PersonInfoFragment.TYPE_EDIT)
                        .putExtra(IntentBuilder.KEY_DATA, entity)
                        .startParentActivity(getActivity(), PersonInfoFragment.class);
                finish();
            });


        } else if (type == TYPE_EDIT) {
            if (entity != null) {
                bindData(entity);
            }

            adapter.setOnItemClickListener((adapter1, view, position) -> {

                if (position == 0) {//身份证正面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_P);
                } else if (position == 1) {//身份中反面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_N);
                } else if (position == 2) {//营业执照
                    new SaActionSheetDialog(getContext())
                            .builder()
                            .addSheetItem("相册选取", OnSheetItemClickListener)
                            .addSheetItem("拍一张", OnSheetItemClickListener)
                            .show();
                }
            });

            btn.setText("确定");
            btn.setOnClickListener(v -> {
                showTips("正在修改", TipType.LoadingShow);
                mPresenter.modifyPersonInfo(r -> {
                    showTips("", TipType.LoadingHide);
                    if (r.status) {
                        PersonInfoEvent personInfoEvent = new PersonInfoEvent(TYPE_LOOK);
                        PersonInfoEntity personInfoEntity = new PersonInfoEntity();
                        personInfoEntity.xingming = mPresenter.personName;
                        personInfoEntity.sjhm = mPresenter.personPhoneNumber;
                        personInfoEntity.dwmc = mPresenter.personWork;
                        personInfoEvent.entity = personInfoEntity;
                        EventBus.getDefault().post(personInfoEvent);
                        ToastUtil.showLongToast(getContext(), "修改成功");
                        finish();
                    } else {
                        error(r.msg);
                    }
                });
            });
        } else if (type == TYPE_UPLOAD_INFO) {
            if (uploadInfoHaveDate) {
                getPersonInfo();
            } else {
                adapter.setNewData(Lists.newArrayList("", "", ""));
            }
            adapter.setOnItemClickListener((adapter1, view, position) -> {

                if (position == 0) {//身份证正面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_P);
                } else if (position == 1) {//身份中反面
                    IntentBuilder.Builder(getActivity(), IdCardCameraActivity.class)
                            .putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N)
                            .startActivity(getActivity(), IdCardCameraActivity.CODE_ID_CARD_N);
                } else if (position == 2) {//营业执照
                    new SaActionSheetDialog(getContext())
                            .builder()
                            .addSheetItem("相册选取", OnSheetItemClickListener)
                            .addSheetItem("拍一张", OnSheetItemClickListener)
                            .show();
                }
            });

            if (uploadInfoHaveDate) {
                btn.setText("修改");
                btn.setOnClickListener(v -> {
                    mPresenter.uploadPersonInfo(r -> {
                        showLoading("正在修改");
                        hideLoading();
                        LogUtil.print(r.toJsonString());
                        if (r.status) {
                            DialogUtils.createDialog(getContext(), r.msg, sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                finish();
                            });
                        } else {
                            error(r.msg);
                        }
                    });
                });
            } else {
                btn.setText("提交");
                btn.setOnClickListener(v -> {
                    showLoading("正在提交");
                    mPresenter.uploadPersonInfo(r -> {
                        hideLoading();
                        LogUtil.print(r.toJsonString());
                        if (r.status) {
                            DialogUtils.createDialogWithLeft(getContext(), "个人信息已经提交成功，是否去支付开通鸽信通"
                                    ,sweetAlertDialog -> {
                                        sweetAlertDialog.dismiss();
                                        finish();
                                    }
                                    ,sweetAlertDialog -> {
                                        mPresenter.getGXTOrder(order -> {
                                            if(order.status){
                                                IntentBuilder.Builder()
                                                        .putExtra(IntentBuilder.KEY_DATA, order.data)
                                                        .startParentActivity(getActivity(), OrderPayFragment.class);
                                                finish();
                                            }else error(order.msg);
                                        });
                                    });
                        } else {
                            error(r.msg);
                        }
                    });
                });
            }


        }
    }

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 2:
                    goCamera();//相机
                    break;
                case 1:
                    goGallry();//相册
                    break;
            }
        }
    };

    private void goGallry() {
        BaseActivity baseActivity = (BaseActivity) getContext();
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(baseActivity, PHOTO_SUCCESS_REQUEST);
    }

    private void goCamera() {
        PhotoUtil.photo(this, uri -> {
            mPresenter.license = PhotoUtil.getPath(getActivity(), uri);
        });
    }


    private View initHeadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_person_info_head_layout, recyclerView, false);
        edName = findViewById(view, R.id.name);
        edNumber = findViewById(view, R.id.phone_numbers);
        edWork = findViewById(view, R.id.work);

        if (type == TYPE_LOOK) {
            edName.setFocusable(false);
            edNumber.setFocusable(false);
            edWork.setFocusable(false);

        } else {
            bindUi(RxUtils.textChanges(edName), mPresenter.setPersonName());
            bindUi(RxUtils.textChanges(edNumber), mPresenter.setPersonPhoneNumber());
            bindUi(RxUtils.textChanges(edWork), mPresenter.setPersonWork());
            if (type == TYPE_UPLOAD_INFO) {
                findViewById(view, R.id.rl_sign).setVisibility(View.VISIBLE);
                edSign = findViewById(view, R.id.sign);
                bindUi(RxUtils.textChanges(edSign), mPresenter.setSign());
                if (uploadInfoHaveDate) {
                    hint = findViewById(view, R.id.hint);
                    hint.setVisibility(View.VISIBLE);
                    hint.setText(hintMessage);
                }
            }

        }

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_with_button_layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
            if (IdCardCameraActivity.CODE_ID_CARD_P == requestCode) {
                IdCardPInfoEntity idCardInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                mPresenter.name = idCardInfoEntity.name;
                mPresenter.sex = idCardInfoEntity.sex;
                mPresenter.address = idCardInfoEntity.address;
                mPresenter.familyName = idCardInfoEntity.nation;
                mPresenter.idCardNumber = idCardInfoEntity.id;
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 1, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(idCardInfoEntity.frontimage));
                mPresenter.idCardP = idCardInfoEntity.frontimage;

                edName.setText(idCardInfoEntity.name);
            } else if (IdCardCameraActivity.CODE_ID_CARD_N == requestCode) {
                IdCardNInfoEntity idCardNInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                mPresenter.organization = idCardNInfoEntity.authority;
                mPresenter.idCardDate = idCardNInfoEntity.valid_date;
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 2, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(idCardNInfoEntity.backimage));
                mPresenter.idCardN = idCardNInfoEntity.backimage;
            }

        }

        if (requestCode == PHOTO_SUCCESS_REQUEST) {
            if (data != null && data.hasExtra(MultiImageSelectorActivity.EXTRA_RESULT)) {
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                mPresenter.license = path.get(0);
                AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 3, R.id.icon);
                imageView.setImageBitmap(BitmapFactory.decodeFile(path.get(0)));
            }
        }

        if (requestCode == PhotoUtil.CAMERA_SUCCESS && resultCode == -1) {
            AppCompatImageView imageView = (AppCompatImageView) adapter.getViewByPosition(recyclerView, 3, R.id.icon);
            imageView.setImageBitmap(BitmapFactory.decodeFile(mPresenter.license));
        }
    }

    private void bindData(PersonInfoEntity entity) {
        edName.setText(entity.xingming != null ? entity.xingming : "");
        edNumber.setText(entity.sjhm != null ? entity.sjhm : "");
        edWork.setText(entity.dwmc != null ? entity.dwmc : "");
        if (edSign != null) {
            edSign.setText(entity.qianming != null ? entity.qianming : "");
        }
        adapter.setNewData(imgs);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PersonInfoEvent event) {
        if (event.type == TYPE_LOOK) {
            bindData(event.entity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
