package com.cpigeon.app.modular.guide.view.viewdao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.http.LogUtil;
import com.cpigeon.app.view.ImageView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class WelcomeFragment extends BaseFragment {

    List<Integer> images = Lists.newArrayList(R.mipmap.ic_welcome_1
            ,R.mipmap.ic_welcome_2
            ,R.mipmap.ic_welcome_3);

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_welcome_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        int type = getArguments().getInt(IntentBuilder.KEY_TYPE);
        AppCompatImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(images.get(type));
        LogUtil.print("welcome :" + type);

        TextView textView = findViewById(R.id.btn);
        if(type == 2){
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(v -> {
                Intent intent;
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                finish();
                SharedPreferencesTool.Save(getContext(), "guide_version", CommonTool.getVersionName(getContext()));
            });
        }
    }
}
