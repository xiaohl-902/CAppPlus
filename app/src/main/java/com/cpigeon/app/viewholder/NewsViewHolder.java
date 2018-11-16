package com.cpigeon.app.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.base.BaseViewHolder;
import com.cpigeon.app.entity.HomeNewsEntity;
import com.cpigeon.app.entity.NewsEntity;
import com.cpigeon.app.pigeonnews.ui.NewsDetailsActivity;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/2.
 */

public class NewsViewHolder extends BaseViewHolder {

    List<Integer> newsIds = Lists.newArrayList(R.id.rl1,R.id.rl2,R.id.rl3,R.id.rl4);

    private TextView tvTitle1;
    private SimpleDraweeView img1;
    private TextView tvTitle2;
    private SimpleDraweeView img2;
    private TextView tvTitle3;
    private SimpleDraweeView img3;
    private TextView tvTitle4;
    private SimpleDraweeView img4;
    List<NewsEntity> newList;
    public NewsViewHolder(View itemView) {
        super(itemView);
        tvTitle1 = getView(R.id.text_1);
        tvTitle2 = getView(R.id.text_2);
        tvTitle3 = getView(R.id.text_3);
        tvTitle4 = getView(R.id.text_4);

        img1 = getView(R.id.img_1);
        img2 = getView(R.id.img_2);
        img3 = getView(R.id.img_3);
        img4 = getView(R.id.img_4);
    }

    public void bindData(HomeNewsEntity entity) {

        newList = entity.newList;

        for (int i = 0, len = newList.size(); i < len; i++) {
            NewsEntity newsEntity = newList.get(i);
            switch (i){
                case 0:
                    tvTitle1.setText(newsEntity.title);
                    if(isHaveImg(newsEntity)){
                        img1.setVisibility(View.VISIBLE);
                        img1.setImageURI(StringValid.isStringValid(newsEntity.imgurl) ? newsEntity.imgurl : newsEntity.pic);
                    }else img1.setVisibility(View.GONE);
                    break;
                case 1:
                    tvTitle2.setText(newsEntity.title);
                    if(isHaveImg(newsEntity)){
                        img2.setVisibility(View.VISIBLE);
                        img2.setImageURI(StringValid.isStringValid(newsEntity.imgurl) ? newsEntity.imgurl : newsEntity.pic);
                    }else img2.setVisibility(View.GONE);
                    break;
                case 2:
                    tvTitle3.setText(newsEntity.title);
                    if(isHaveImg(newsEntity)){
                        img3.setVisibility(View.VISIBLE);
                        img3.setImageURI(StringValid.isStringValid(newsEntity.imgurl) ? newsEntity.imgurl : newsEntity.pic);
                    }else img3.setVisibility(View.GONE);
                    break;
                case 3:
                    tvTitle4.setText(newsEntity.title);
                    if(isHaveImg(newsEntity)){
                        img4.setVisibility(View.VISIBLE);
                        img4.setImageURI(StringValid.isStringValid(newsEntity.imgurl) ? newsEntity.imgurl : newsEntity.pic);
                    }else img4.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private boolean isHaveImg(NewsEntity newsEntity){
        return StringValid.isStringValid(newsEntity.imgurl) || StringValid.isStringValid(newsEntity.pic);
    }

    public void setListener(Context context){
        for (int i = 0, len = newsIds.size(); i < len; i++) {
            int finalI = i;
            NewsEntity entity = newList.get(finalI);
            findViewById(newsIds.get(i)).setOnClickListener(v -> {
                IntentBuilder.Builder(context, NewsDetailsActivity.class)
                        .putExtra(IntentBuilder.KEY_DATA, StringValid.isStringValid(entity.nid) ? entity.nid : entity.id)
                        .startActivity();
            });
        }
    }
}
