package com.cpigeon.app.circle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.CircleUpdateManager;
import com.cpigeon.app.circle.adpter.CircleMessageDetailsCommentsAdapter;
import com.cpigeon.app.circle.presenter.CircleMessagePre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.CircleDetailsReplayEntity;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.event.CircleMessageDetailsEvent;
import com.cpigeon.app.pigeonnews.ui.InputCommentDialog;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.ScreenTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/5/23.
 */

public class CircleMessageDetailsCommentFragment extends BaseMVPFragment<CircleMessagePre> {

    RecyclerView recyclerView;
    CircleMessageDetailsCommentsAdapter adapter;

    @Override
    protected CircleMessagePre initPresenter() {
        return ((CircleMessageDetailsNewActivity) getActivity()).getPre();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setBackgroundColor(getResources().getColor(R.color.white));

        adapter = new CircleMessageDetailsCommentsAdapter(mPresenter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            CircleMessageEntity.CommentListBean comment = adapter.getData().get(position);
            InputCommentDialog dialog = new InputCommentDialog();
            dialog.setHint(CpigeonData.getInstance().getUserInfo().getNickname()
                    + " 回复 " + comment.getUser().getNickname());
            dialog.setPushClickListener(editText -> {
                showLoading();
                mPresenter.messageId = mPresenter.circleList.get(0).getMid();
                mPresenter.commentId = comment.getId();
                mPresenter.previousId = comment.getId();
                mPresenter.commentContent = editText.getText().toString();
                mPresenter.addComment(newComment -> {
                    hideLoading();
                    List<CircleDetailsReplayEntity> list = mPresenter.circleList.get(0).getCommentList().get(position).getHflist();
                    list.add(list.size(), adapter.getReplayEntity(newComment));
                    dialog.dismiss();
                    adapter.setNewData(mPresenter.getDetailsComment());
                    ((CircleMessageDetailsNewActivity) getActivity()).postCircleEvent();
                });
            });
            dialog.show(getActivity().getFragmentManager(), "InputCommentDialog");
        });

        adapter.bindToRecyclerView(recyclerView);

        adapter.setNewData(mPresenter.getDetailsComment());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CircleMessageDetailsEvent event) {
        if (event.type == CircleMessageDetailsEvent.TYPE_COMMENT) {
            adapter.setNewData(mPresenter.circleList.get(0).getCommentList());
            recyclerView.scrollToPosition(adapter.getData().size() - 1);
        }
    }
}
