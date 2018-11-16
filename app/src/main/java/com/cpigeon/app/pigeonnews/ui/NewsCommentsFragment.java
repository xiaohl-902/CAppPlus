package com.cpigeon.app.pigeonnews.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.NewsCommentEntity;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.pigeonnews.adpter.NewsCommentAdapter;
import com.cpigeon.app.pigeonnews.presenter.NewsCommentsPre;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.viewholder.NewsCommentViewHolder;

/**
 * Created by Zhu TingYu on 2018/1/9.
 */

public class NewsCommentsFragment extends BaseMVPFragment<NewsCommentsPre> {

    RecyclerView recyclerView;
    NewsCommentAdapter adapter;
    String usersNickName;
    Animation animation;


    @Override
    protected NewsCommentsPre initPresenter() {
        return new NewsCommentsPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news_details_comment_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        animation = AnimationUtils.loadAnimation(MyApp.getInstance().getBaseContext(), R.anim.anim_sign_box_rock);

        getNickName();
        setTitle("全部评论");
        NewsCommentViewHolder viewHolder = new NewsCommentViewHolder(findViewById(R.id.bottom_comment), getActivity());
        viewHolder.isCenterComment();
        viewHolder.setListener(new NewsCommentViewHolder.OnViewClickListener() {
            @Override
            public void commentPushClick(EditText editText) {
                showLoading();
                mPresenter.content = editText.getText().toString();
                mPresenter.addNewsComment(data -> {
                    adapter.addData(0, data);
                    recyclerView.smoothScrollToPosition(0);
                    hideLoading();
                    viewHolder.dialog.closeDialog();
                });
            }

            @Override
            public void thumbClick() {

            }

            @Override
            public void commentClick() {

            }
        });

        recyclerView = findViewById(R.id.list);
        addItemDecorationLine(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsCommentAdapter(mPresenter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            NewsCommentEntity entity = adapter.getItem(position);
            InputCommentDialog dialog = new InputCommentDialog();
            dialog.setHint("回复 " + entity.nicheng+ ":");
            dialog.setPushClickListener(content -> {
                mPresenter.content = content.getText().toString();
                mPresenter.commentId = entity.id;
                mPresenter.replyId = entity.id;
                mPresenter.replyComment(s -> {
                    NewsCommentEntity reply = new NewsCommentEntity();
                    reply.nicheng = usersNickName;
                    reply.content = mPresenter.content;
                    if(entity.reply == null){
                        entity.reply = Lists.newArrayList();
                    }
                    entity.reply.add(reply);
                    entity.replycount += 1;
                    entity.isreply = true;
                    adapter.notifyItemChanged(position);
                    dialog.closeDialog();
                });
            });
            dialog.show(getActivity().getFragmentManager(), "InputComment");
        });
        adapter.setListener(new NewsCommentAdapter.OnCommunicationListener() {
            @Override
            public void thumb(NewsCommentEntity entity, int position) {
                showLoading();
                mPresenter.commentId = entity.id;
                mPresenter.thumbNewsComment(data -> {
                    hideLoading();
                    if(data.isThumb()){
                        entity.dianzan += 1;
                        entity.setThumb();
                    }else {
                        entity.dianzan -=1;
                        entity.setCancelThumb();
                    }
                    adapter.notifyItemChanged(position);
                    adapter.getViewByPosition(position,R.id.image_thumb).startAnimation(animation);
                });
            }

            @Override
            public void comment(NewsCommentEntity entity, int position) {
            }
        });
        adapter.setOnLoadMoreListener(() -> {
            mPresenter.page++;
            mPresenter.getNewsComments(data -> {
                if (data.isEmpty()) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(data);
                    adapter.loadMoreComplete();
                }
            });
        }, recyclerView);
        recyclerView.setAdapter(adapter);


        bindData();
    }

    private void getNickName() {

        if(CpigeonData.getInstance().getUserInfo() != null){

            usersNickName = CpigeonData.getInstance().getUserInfo().getNickname();
        }


        if(!StringValid.isStringValid(usersNickName)){
            CpigeonData.DataHelper.getInstance().updateUserInfo(new CpigeonData.DataHelper.OnDataHelperUpdateLisenter<UserInfo.DataBean>() {
                @Override
                public void onUpdated(UserInfo.DataBean data) {
                    usersNickName = data.getNickname();
                }

                @Override
                public void onError(int errortype, String msg) {

                }
            });
        }

    }

    private void bindData() {
        showLoading();
        mPresenter.getNewsComments(data -> {
            hideLoading();
            adapter.setNewData(data);
        });
    }
}
