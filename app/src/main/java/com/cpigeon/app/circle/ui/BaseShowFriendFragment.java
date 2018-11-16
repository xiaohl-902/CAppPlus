package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.adpter.ShowFriendAdapter;
import com.cpigeon.app.circle.presenter.FriendPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.FriendFollowEvent;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class BaseShowFriendFragment extends BaseMVPFragment<FriendPre> {

    public static final String TYPE_FANS = "fs";
    public static final String TYPE_FOLLOW = "gz";

    RecyclerView recyclerView;
    ShowFriendAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_layout;
    }

    @Override
    protected FriendPre initPresenter() {
        return new FriendPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout.setBackgroundColor(getResources().getColor(R.color.color_e6e6e6));
        adapter = new ShowFriendAdapter(mPresenter.type);

        if(mPresenter.type.equals(TYPE_FOLLOW)){
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                cancelFollow(position);
            });
        }else {
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                if(adapter.getItem(position).isIsmutual()){
                    DialogUtils.createDialogWithLeft(getContext(), "是否取消关注", sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        mPresenter.followId = adapter.getItem(position).getUserinfo().getUid();
                        mPresenter.setIsFollow(false);
                        mPresenter.setFollow(s -> {
                            adapter.getItem(position).setIsmutual(false);
                            adapter.notifyItemChanged(position);
                            ToastUtil.showShortToast(getContext(), s);
                            EventBus.getDefault().post(new FriendFollowEvent(false));
                        });
                    });
                }else {
                    follow(position);
                }
            });
        }

/*        adapter.setOnItemClickListener((adapter1, view, position) -> {
             if(adapter.getItem(position).isIsmutual()){
                 DialogUtils.createDialogWithLeft(getContext(), "是否取消关注", sweetAlertDialog -> {
                     sweetAlertDialog.dismiss();
                     mPresenter.followId = adapter.getItem(position).getUserinfo().getUid();
                     mPresenter.setIsFollow(false);
                     mPresenter.setFollow(s -> {
                         adapter.getItem(position).setIsmutual(false);
                         adapter.notifyItemChanged(position);
                         ToastUtil.showShortToast(getContext(), s);
                     });
                 });
             }else {
                 follow(position);
             }
        });*/

        setRefreshListener(() -> {
            mPresenter.page = 1;
            mPresenter.getFriends(data -> {
                adapter.setNewData(data);
                hideLoading();
            });
        });

        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.getFriends(data -> {
                if(data.isEmpty()){
                    adapter.setLoadMore(true);
                }else {
                    adapter.setLoadMore(false);
                    adapter.addData(data);
                }
            });
        },recyclerView);
        recyclerView.setAdapter(adapter);
        bindData();

    }

    private void bindData() {
        showLoading();
        mPresenter.getFriends(data -> {
            hideLoading();
            adapter.setNewData(data);
        });
    }

    private void follow(int position){
        DialogUtils.createDialogWithLeft(getContext(), "是否关注", sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            mPresenter.followId = adapter.getItem(position).getUserinfo().getUid();
            mPresenter.setIsFollow(true);
            mPresenter.setFollow(s -> {
                adapter.getItem(position).setIsmutual(true);
                adapter.notifyItemChanged(position);
                ToastUtil.showShortToast(getContext(), s);
                EventBus.getDefault().post(new FriendFollowEvent(true));
            });
        });
    }

    private void cancelFollow(int position){
        DialogUtils.createDialogWithLeft(getContext(), "是否取消关注", sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
            mPresenter.followId = adapter.getItem(position).getUserinfo().getUid();
            mPresenter.setIsFollow(false);
            mPresenter.setFollow(s -> {
                adapter.remove(position);
                ToastUtil.showShortToast(getContext(), s);
                EventBus.getDefault().post(new FriendFollowEvent(false));
                CircleMessageEntity circleMessageEntity = new CircleMessageEntity();
                circleMessageEntity.setUid(mPresenter.followId);
                CircleUpdateManager.get().updateFollow(circleMessageEntity, false);
            });
        });
    }
}
