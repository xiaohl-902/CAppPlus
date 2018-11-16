package com.cpigeon.app.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.app.R;
import com.cpigeon.app.base.videoplay.RecyclerItemBaseHolder;
import com.cpigeon.app.base.videoplay.SmallVideoHelper;
import com.cpigeon.app.entity.VideoEntity;
import com.cpigeon.app.modular.matchlive.model.bean.RaceImageOrVideo;
import com.cpigeon.app.view.ShareDialogFragment;
import com.cpigeon.app.view.playvideo.VideoPlayActivity;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GUO on 2015/12/3.
 */
public class MapVideoViewHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context = null;


    private SmallVideoHelper smallVideoHelper;
    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    ImageView thumbImage;
    ImageView share;
    ViewGroup listItemContainer;
    ImageView play;
    TextView title;
    TextView time;
    TextView weather;
    TextView locate;

    public MapVideoViewHolder(Context context, View v) {
        super(v);
        this.context = context;
        initView(v);
    }

    private void initView(View view) {
        play = view.findViewById(R.id.play);
        thumbImage = new ImageView(context);
        thumbImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listItemContainer = view.findViewById(R.id.ff);
        title = view.findViewById(R.id.title);
        time = view.findViewById(R.id.time);
        weather = view.findViewById(R.id.weather);
        share = view.findViewById(R.id.share);
        locate = view.findViewById(R.id.locate);
//        play.setOnClickListener(v -> {
//            getRecyclerBaseAdapter().notifyDataSetChanged();
//        });
    }

    public void onBind(final int position, final RaceImageOrVideo item) {
        Glide.with(context).load(item.getThumburl()).into(thumbImage);

        time.setText(item.getTime());
        weather.setText(item.getWeartherName() + " " + item.getTemperature() + "°C");
        locate.setText("坐标：" + item.getLongitude() + "/" + item.getLatitude());

        title.setText(item.getTag());//设置标签

        share.setOnClickListener(v -> {
            if(shareListener != null){
                shareListener.share(item);
            }
        });

        smallVideoHelper.addVideoPlayer(position, thumbImage, TAG, listItemContainer, play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getRecyclerBaseAdapter().notifyDataSetChanged();
                //listVideoUtil.setLoop(true);
                smallVideoHelper.setPlayPositionAndTag(position, TAG);
                gsySmallVideoHelperBuilder.setVideoTitle(item.getTag()).setUrl(item.getUrl());
                smallVideoHelper.startPlay();*/
                VideoPlayActivity.startActivity((Activity) context, itemView, item.getUrl());
            }
        });
    }


    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }

    public interface OnShareListener{
        void share(RaceImageOrVideo videoUrl);
    }

    private OnShareListener shareListener;

    public void setShareListener(OnShareListener shareListener) {
        this.shareListener = shareListener;
    }
}





