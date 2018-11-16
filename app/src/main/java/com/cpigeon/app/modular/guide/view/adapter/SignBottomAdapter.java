package com.cpigeon.app.modular.guide.view.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.guide.model.bean.GiftBagInfo;
import com.cpigeon.app.modular.guide.presenter.SignPresenter;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ScreenTool;
import com.cpigeon.app.utils.SweetAlertDialogUtil;
import com.plattysoft.leonids.ParticleSystem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Zhu TingYu on 2017/12/28.
 */

public class SignBottomAdapter extends BaseQuickAdapter<GiftBagInfo, BaseViewHolder> {

    List<Integer> icons;
    int size;
    private static final int GIF_SIZE = 4;
    Animation animation;
    Activity activity;
    private SignPresenter mPresenter;
    private SweetAlertDialog mSweetAlertDialog;

    public SignBottomAdapter(Activity activity, SignPresenter mPresenter, SweetAlertDialog mSweetAlertDialog) {
        super(R.layout.item_sign_bottom_layout, Lists.newArrayList());
        icons = Lists.newArrayList(R.mipmap.ic_gray_box_1
                , R.mipmap.ic_gray_box_2
                , R.mipmap.ic_gray_box_3
                , R.mipmap.ic_gray_box_4);
        size = ScreenTool.getScreenWidth(MyApp.getInstance().getBaseContext()) / 4;
        animation = AnimationUtils.loadAnimation(MyApp.getInstance().getBaseContext(), R.anim.anim_sign_box_rock);
        this.activity = activity;
        this.mPresenter = mPresenter;
        this.mSweetAlertDialog = mSweetAlertDialog;
    }

    @Override
    protected void convert(BaseViewHolder holder, GiftBagInfo item) {

        ImageView imageView = holder.getView(R.id.img);

        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(size, ViewGroup.LayoutParams.MATCH_PARENT));
        int ImgSize = 0;
        int imgId = 0;
        switch (holder.getAdapterPosition()) {
            case 0:
                imgId = R.mipmap.ic_blue_box_1;
                ImgSize = 20 + GIF_SIZE;
                break;
            case 1:
                imgId = R.mipmap.ic_blue_box_2;
                ImgSize = 24 + GIF_SIZE;
                break;
            case 2:
                imgId = R.mipmap.ic_blue_box_3;
                ImgSize = 28 + GIF_SIZE;
                break;
            case 3:
                imgId = R.mipmap.ic_blue_box_4;
                ImgSize = 32 + GIF_SIZE;
                break;
        }
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ScreenTool.dip2px( ImgSize), ScreenTool.dip2px(ImgSize)));
        holder.itemView.setOnClickListener(v -> {
            Log.d("qiandao", "convert: dianji--1");
        });
        if (item.isChoose()) {
            holder.setImageResource(R.id.img, imgId);
            holder.itemView.setOnClickListener(v -> {
                imageView.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ParticleSystem s = new ParticleSystem(activity, 30, R.drawable.vector_ic_start, 800)
                                .setSpeedModuleAndAngleRange(0.1f, 0.2f, 225, 315)
                                .setRotationSpeed(180)
                                .setAcceleration(0.00005f, 270)
                                .setScaleRange(0.8f, 1.2f)
                                .setFadeOut(200)
                                .setSpeedRange(0.1f, 0.25f);
                        s.oneShot(v, 30);

                        mPresenter.getGiftBagData(item.getGid(), o -> {
                            if (o.getErrorCode() == 0) {
                                EventBus.getDefault().post(o.getData());
                            } else {
                                mSweetAlertDialog = SweetAlertDialogUtil.showDialog(mSweetAlertDialog, o.getMsg(), SignBottomAdapter.this.activity);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            });
        } else {
            holder.setImageResource(R.id.img, icons.get(holder.getAdapterPosition()));
        }
    }
}

