package com.cpigeon.app.modular.saigetong.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.SGTDetailsInfoEntity;
import com.cpigeon.app.entity.UserGXTEntity;
import com.cpigeon.app.modular.matchlive.model.bean.RPImages;
import com.cpigeon.app.modular.saigetong.presenter.SGTDetailsPre;
import com.cpigeon.app.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.app.modular.saigetong.view.adapter.ZHNumAdapter;
import com.cpigeon.app.utils.ChooseImageManager;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.view.ShareDialogFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 赛鸽通详情fragment
 * Created by Administrator on 2018/1/22.
 */

public class SGTDetailsFragment extends BaseMVPFragment<SGTDetailsPre> {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private ZHNumAdapter mAdapter;
    TextView tvCskh, tvGzxm, tvDzhh, tvSgys, tvDq, tvRpsj;
    private View headView;
    ShareDialogFragment shareDialogFragment;

    @Override
    protected SGTDetailsPre initPresenter() {
        return new SGTDetailsPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle(mPresenter.foodId);
        shareDialogFragment = new ShareDialogFragment();
        initView();
        showLoading();
        mPresenter.getFootInfoData(data -> {
            hideLoading();
            bindData(data);
            if(data.getDatalist() != null){
                mAdapter.setNewData(data.getDatalist());
            }
        });
    }

    private void bindData(SGTDetailsInfoEntity entity) {
        tvCskh.setText(entity.getCskh());
        tvGzxm.setText(entity.getXingming());
        tvSgys.setText(entity.getColor());
        tvDq.setText(entity.getDiqu());
        tvRpsj.setText(entity.getRpsj());
        tvDzhh.setText(entity.getRing());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }


    /**
     * 初始化RecyclerView
     */
    public void initView() {
        mAdapter = new ZHNumAdapter(getContext());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sg_headview, null, false);
        tvCskh =  headView.findViewById(R.id.tv_cskh);
        tvGzxm =  headView.findViewById(R.id.tv_gzxm);
        tvDzhh =  headView.findViewById(R.id.tv_dzhh);
        tvSgys =  headView.findViewById(R.id.tv_sgys);
        tvDq =  headView.findViewById(R.id.tv_dq);
        tvRpsj =  headView.findViewById(R.id.tv_rpsj);

        mAdapter.addHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            RPImages item =  mAdapter.getItem(position);
            shareDialogFragment.setShareType(ShareDialogFragment.TYPE_IMAGE_URL);
            shareDialogFragment.setTitle(item.getTag());
            shareDialogFragment.setDescription(item.getUpdatefootinfo());
            shareDialogFragment.setShareContent(item.getImgurl());
            shareDialogFragment.show(getActivity().getFragmentManager(),"share");
            return false;
        });
    }
}
