package com.cpigeon.app.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.app.R;
import com.cpigeon.app.base.videoplay.RecyclerItemBaseHolder;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.entity.CircleMessageEntity;

/**
 * Created by Zhu TingYu on 2018/2/2.
 */

public class CircleVideoViewHolder extends RecyclerItemBaseHolder {
    public final static String TAG_ALL = "TAG_ALL";
    public final static String TAG_FOLLOW = "TAG_FOLLOW";
    public final static String TAG_MY = "TAG_MY";

    protected Context context = null;

    public String tag;


    private SmallVideoHelper smallVideoHelper;
    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    ImageView thumbImage;
    ViewGroup listItemContainer;
    ImageView play;

    public CircleVideoViewHolder(Context context, View v) {
        super(v);
        this.context = context;
        initView(v);
    }

    private void initView(View view) {
        play = view.findViewById(R.id.play);
        thumbImage = new ImageView(context);
        listItemContainer = view.findViewById(R.id.ff);
    }

    public void onBind(final int position, final CircleMessageEntity.PictureBean item) {
        Glide.with(context).load(item.getThumburl()).into(thumbImage);

        smallVideoHelper.addVideoPlayer(position, thumbImage, tag, listItemContainer, play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecyclerBaseAdapter().notifyDataSetChanged();
                //listVideoUtil.setLoop(true);
                smallVideoHelper.setPlayPositionAndTag(position, tag);
                gsySmallVideoHelperBuilder.setVideoTitle("").setUrl(item.getUrl());
                smallVideoHelper.startPlay();
            }
        });
    }


    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }

    public void setTagString(String tag) {
        this.tag = tag;
    }
}
