package com.cpigeon.app.message.ui.modifysign;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.IdCardNInfoEntity;
import com.cpigeon.app.entity.IdCardPInfoEntity;
import com.cpigeon.app.entity.PersonInfoEntity;
import com.cpigeon.app.message.adapter.PersonImageInfoAdapter;
import com.cpigeon.app.message.ui.idCard.IdCardCameraActivity;
import com.cpigeon.app.modular.usercenter.view.activity.UserInfoActivity;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.FileTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.inputfilter.PhotoUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class ModifySignFragment extends BaseMVPFragment<PersonSignPre> {


    RecyclerView recyclerView;
    PersonImageInfoAdapter adapter;

    TextView btn;

    EditText edSign;
    private int PHOTO_SUCCESS_REQUEST = 2083;

    PersonInfoEntity entity;

    List<String> imgs;
    Disposable disposable;


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
        setTitle("修改签名");
        imgs = Lists.newArrayList("idCard_P", "idCard_N", "license");
        hideSoftInput();
        initView();
        getData();
    }

    private void getData() {
        showLoading();
        mPresenter.getPersonSignInfo(r -> {
            if (r.status) {
                entity = r.data;
                    FileTool.byte2File(entity.sfzzm, getContext().getCacheDir().getPath(), imgs.get(0));
                    FileTool.byte2File(entity.sfzbm, getContext().getCacheDir().getPath(), imgs.get(1));
                    FileTool.byte2File(entity.yyzz, getContext().getCacheDir().getPath(), imgs.get(2));
                    hideLoading();
                    bindData();

            } else {
                error(r.msg);
            }
        });
    }

    private void bindData() {
        adapter.setNewData(imgs);
        if (!entity.isExamine()) {
            edSign.setText(entity.shenqingqm);
            btn.setText("签名正在审核中....");
            btn.setEnabled(false);
            adapter.setOnItemClickListener(null);
            edSign.setEnabled(false);
        } else {
            setBtn();
            edSign.setText(entity.qianming);
            edSign.setEnabled(true);
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
        }
    }

    protected void initView() {
        findViewById(R.id.ll1).setVisibility(View.GONE);
        btn = findViewById(R.id.text_btn);
        btn.setVisibility(View.VISIBLE);
        btn.setText("提交签名");
        btn.setEnabled(false);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new PersonImageInfoAdapter(getContext());
        adapter.bindToRecyclerView(recyclerView);
        adapter.addHeaderView(initHeadView());
        adapter.setNewData(Lists.newArrayList("", "", ""));
        recyclerView.requestFocus();


    }

    private void setBtn() {
        btn.setEnabled(true);
        btn.setOnClickListener(v -> {
            showTips("正在修改", TipType.LoadingShow);
            mPresenter.modifySign(r -> {
                showTips("", TipType.LoadingHide);
                if (r.status) {
                    ToastUtil.showLongToast(getContext(), "修改成功");
                    finish();
                } else {
                    error(r.msg);
                }
            });
        });
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

    protected View initHeadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_modify_sign_head_layout, recyclerView, false);
        edSign = findViewById(view, R.id.sign);
        bindUi(RxUtils.textChanges(edSign), mPresenter.setSign());
        edSign.clearFocus();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}
