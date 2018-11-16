package com.cpigeon.app.pigeonnews.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.pigeonnews.presenter.PigeonMessagePre;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.ToastUtil;

/**
 * Created by Zhu TingYu on 2018/1/6.
 */

public class PigeonMessageFragment extends BaseMVPFragment<PigeonMessagePre> {

    public static final int TYPE_EARTH_QUAKE = 1;
    public static final int TYPE_SOLAR_STORM = 2;


    ImageView icon;
    TextView content;

    AnimationDrawable animationDrawable;

    @Override
    protected PigeonMessagePre initPresenter() {
        return new PigeonMessagePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pigeon_message_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mPresenter.type = getArguments().getInt(IntentBuilder.KEY_TYPE);

        icon = findViewById(R.id.icon);
        content = findViewById(R.id.content);
        ImageView earthAnim = findViewById(R.id.earth_anim);

        if(mPresenter.type == TYPE_EARTH_QUAKE){
            icon.setImageResource(R.mipmap.ic_earth_quake);
            earthAnim.setImageResource(R.drawable.anim_earth_quack);
            animationDrawable = (AnimationDrawable) earthAnim.getDrawable();
            animationDrawable.start();
        }else {
            icon.setImageResource(R.mipmap.ic_solar_storm);
            earthAnim.setVisibility(View.GONE);
            LinearLayout suns =  findViewById(R.id.suns_images);
            suns.setVisibility(View.VISIBLE);

            BaseViewHolder holder1 = new BaseViewHolder(findViewById(suns, R.id.img_layout_1));
            TextView textView1 = holder1.getView(R.id.title);
            textView1.setTextColor(getResources().getColor(R.color.gray_m));
            textView1.setTextSize(13);
            textView1.setText("太阳磁场像");
            holder1.setImageResource(R.id.icon, R.mipmap.ic_solar_storm_1);


            BaseViewHolder holder2 = new BaseViewHolder(findViewById(suns, R.id.img_layout_2));

            TextView textView2 = holder2.getView(R.id.title);
            textView2.setTextColor(getResources().getColor(R.color.gray_m));
            textView2.setText("太阳黑子像");
            textView2.setTextSize(13);
            holder2.setImageResource(R.id.icon, R.mipmap.ic_solar_storm_2);

            BaseViewHolder holder3 = new BaseViewHolder(findViewById(suns, R.id.img_layout_3));
            TextView textView3 = holder3.getView(R.id.title);
            textView3.setTextColor(getResources().getColor(R.color.gray_m));
            textView3.setTextSize(13);
            textView3.setText("全日面色球单色像");
            holder3.setImageResource(R.id.icon, R.mipmap.ic_solar_storm_3);



        }

        bindData();

    }

    private void bindData() {
        mPresenter.getMessage(data -> {
            content.setText(data.content);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(animationDrawable != null){
            animationDrawable.stop();
        }
    }
}
