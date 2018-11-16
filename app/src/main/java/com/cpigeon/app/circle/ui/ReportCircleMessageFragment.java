package com.cpigeon.app.circle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.circle.adpter.ReportCircleMessageAdapter;
import com.cpigeon.app.circle.presenter.CircleReportPre;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.ReportCircleContentEntity;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/2/3.
 */

public class ReportCircleMessageFragment extends BaseMVPFragment<CircleReportPre> {

    RecyclerView recyclerView;
    ReportCircleMessageAdapter adapter;
    List<String> reportContent = Lists.newArrayList("有不当广告和传销"
            ,"色情、低俗或血腥暴力"
            ,"反动言论"
            ,"其他");

    public static void startReportCircleMessageFragment(Activity activity, int messageId){
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, messageId)
                .startParentActivity(activity, ReportCircleMessageFragment.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    protected CircleReportPre initPresenter() {
        return new CircleReportPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("举报");

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportCircleMessageAdapter();
        adapter.bindToRecyclerView(recyclerView);

        List<ReportCircleContentEntity> data = Lists.newArrayList();

        for(int i = 0, len =  reportContent.size(); i < len; i++){
            ReportCircleContentEntity entity = new ReportCircleContentEntity();
            entity.content = reportContent.get(i);
            data.add(entity);
        }
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            adapter.setSingleItem(data.get(position), position);
        });
        adapter.setNewData(data);
        adapter.setImgChooseVisible(true);
        adapter.addHeaderView(initHead());
        adapter.addFooterView(initFood());

        toolbar.getMenu().clear();
        toolbar.getMenu().add("提交").setOnMenuItemClickListener(item -> {

            if(adapter.getSelectedEntity().isEmpty()){
                DialogUtils.createHintDialog(getActivity(), "请选择举报原因");
                return false;
            }
            showLoading();
            mPresenter.content = adapter.getSelectedEntity().get(0).content + mPresenter.content;
            mPresenter.reportMessage(s -> {
                hideLoading();
                DialogUtils.createDialogWithLeft(getContext(), "您已经举报成功，谢谢您的支持",sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    finish();
                });
            });
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        recyclerView.requestFocus();
    }

    private View initHead() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_message_report_head_layout, recyclerView, false);
        TextView textView = view.findViewById(R.id.content);
        textView.setText("举报原因");
        return view;
    }

    private View initFood() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_message_report_foot_layout, recyclerView, false);
        TextView textView = view.findViewById(R.id.content);
        textView.setText("具体描述（选项）");
        EditText content = view.findViewById(R.id.ed_content);
        bindUi(RxUtils.textChanges(content), mPresenter.setContent());
        return view;
    }
}
