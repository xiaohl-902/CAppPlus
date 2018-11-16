package com.cpigeon.app.circle.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.circle.adpter.ChooseImageAdapter;
import com.cpigeon.app.circle.presenter.PushCircleMessagePre;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.ChooseImageEntity;
import com.cpigeon.app.event.CircleMessageEvent;
import com.cpigeon.app.utils.BitmapUtil;
import com.cpigeon.app.utils.BitmapUtils;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/16.
 */

public class PushCircleMessageFragment extends BaseMVPFragment<PushCircleMessagePre> {

    public static final int CODE_CHOOSE_LOCATION = 0x123;
    public static final int CODE_VIDEO = 0x234;

    RecyclerView recyclerView;
    ChooseImageAdapter adapter;

    TextView content;
    TextView visible;
    TextView location;

    View water;

   /* TextView wTime;
    TextView wLocation;*/


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_push_circle_message_layout;
    }

    @Override
    protected PushCircleMessagePre initPresenter() {
        return new PushCircleMessagePre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        initWater();

        setTitle("说说");
        content = findViewById(R.id.content);
        location = findViewById(R.id.location);
        visible = findViewById(R.id.visibility);

        bindUi(RxUtils.textChanges(content),mPresenter.setMessage());
        bindUi(RxUtils.textChanges(location),mPresenter.setLocation());
        bindUi(RxUtils.textChanges(visible),mPresenter.setShowType());


        toolbar.getMenu().clear();
        toolbar.getMenu().add("发表")
                .setOnMenuItemClickListener(item -> {
                    showLoading();
                    mPresenter.pushMessage(b -> {
                        hideLoading();
                        RxUtils.delayed(500,aLong -> {
                            EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_ALL));
                        });
                        EventBus.getDefault().post(new CircleMessageEvent(BaseCircleMessageFragment.TYPE_MY));
                        ToastUtil.showLongToast(MyApp.getInstance().getBaseContext(),"发布成功");
                        finish();
                    });
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        findViewById(R.id.rl_user_location).setOnClickListener(v -> {
            IntentBuilder.Builder().
                    startParentActivity(getActivity(), ChooseLocationFragment.class, CODE_CHOOSE_LOCATION);
        });

        findViewById(R.id.rl_user_msg_visibility).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String[] items = new String[]{getString(R.string.string_circle_message_show_type_public)
                    , getString(R.string.string_circle_message_show_type_friend)
                    , getString(R.string.string_circle_message_show_type_person)};/*设置列表的内容*/
            builder.setItems(items, new DialogInterface.OnClickListener() {/*设置列表的点击事件*/
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    visible.setText(items[which]);
                }
            });
            builder.setCancelable(true);
            builder.show();
        });

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new ChooseImageAdapter(getActivity(),mPresenter);
        adapter.setType(ChooseImageAdapter.TYPE_ALL);
        adapter.setNewData(Lists.newArrayList());
        recyclerView.setAdapter(adapter);

        showLoading();

        new LocationManager(getActivity()).setLocationListener(aMapLocation -> {
            hideLoading();
            mPresenter.la = aMapLocation.getLatitude();
            mPresenter.lon = aMapLocation.getLongitude();
            mPresenter.lo = aMapLocation.getProvince() + aMapLocation.getCity();
        }).star();
    }

    private void initWater() {
        water = LayoutInflater.from(getContext()).inflate(R.layout.water_push_circle_layout,null);
        /*wTime  = findViewById(water, R.id.time);
        wLocation = findViewById(water, R.id.location);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            List<String> images = Lists.newArrayList();
            List<ChooseImageEntity> entities = Lists.newArrayList();
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if(requestCode == PictureMimeType.ofImage()){
                //wTime.setText(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATETIME));
                for (LocalMedia localMedia : selectList) {
                    images.add(localMedia.getCompressPath());
                }
                /*if(StringValid.isStringValid(mPresenter.location)){
                    wLocation.setText(mPresenter.location);
                    wLocation.setVisibility(View.VISIBLE);
                }else wLocation.setVisibility(View.GONE);*/
                images = BitmapUtils.addWaters(images, water, DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATETIME));

                for (String path : images) {
                    ChooseImageEntity entity = new ChooseImageEntity();
                    entity.url = path;
                    entities.add(entity);
                }

                if(!entities.isEmpty()){
                    adapter.setType(ChooseImageAdapter.TYPE_PICTURE);
                    mPresenter.messageType = PushCircleMessagePre.TYPE_PICTURE;
                    adapter.addData(entities);
                    mPresenter.imgs = adapter.getImgs();
                }
            }/*else if(requestCode == PictureMimeType.ofVideo()){
                for (LocalMedia localMedia : selectList) {
                    ChooseImageEntity entity = new ChooseImageEntity();
                    entity.url = localMedia.getPath();
                    entities.add(entity);
                }
                if(!entities.isEmpty()){

                }
            }*/

        }

        if(requestCode == CODE_VIDEO){
            if(data != null && data.hasExtra("video_path")){
                ChooseImageEntity entity = new ChooseImageEntity();
                entity.url = data.getStringExtra("video_path");
                adapter.setType(ChooseImageAdapter.TYPE_VIDEO);
                mPresenter.messageType = PushCircleMessagePre.TYPE_VIDEO;
                adapter.addData(Lists.newArrayList(entity));
                mPresenter.video = adapter.getImgs().get(0);
            }
        }

        if(requestCode == CODE_CHOOSE_LOCATION){
            if(data != null && data.hasExtra(IntentBuilder.KEY_DATA)){
                location.setText(data.getStringExtra(IntentBuilder.KEY_DATA));
                //wLocation.setText(data.getStringExtra(IntentBuilder.KEY_DATA));
            }
        }
    }
}
